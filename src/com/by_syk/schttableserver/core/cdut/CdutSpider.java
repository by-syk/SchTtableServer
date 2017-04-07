package com.by_syk.schttableserver.core.cdut;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.by_syk.schttableserver.bean.CourseBean;
import com.by_syk.schttableserver.bean.KeyBean;
import com.by_syk.schttableserver.bean.StatusBean;
import com.by_syk.schttableserver.config.Config;
import com.by_syk.schttableserver.core.BaseSpider;
import com.by_syk.schttableserver.util.DateUtil;
import com.by_syk.schttableserver.util.StringUtil;
import com.by_syk.schttableserver.util.TokenCookie;
import com.by_syk.schttableserver.util.net.HttpUtil;

public class CdutSpider extends BaseSpider {
    private String urlHost;
    private String urlLogin;
    private String urlHome;
    
    private String tmpCoursePage = null;
    
    public CdutSpider(Config config) {
        super(config);
    }

    @Override
    protected void loadConfig(Config config) {
        urlHost = config.getCdutHost();
        urlLogin = urlHost + config.getCdutLogin();
        urlHome = urlHost + config.getCdutHome();
    }

    @Override
    public boolean init(String stuNo, String password, String userKey, StatusBean statusBean) {
        System.out.println("CdutSpider - init");
        
        super.stuNo = stuNo;
        super.pwd = password;
        super.userKey = userKey;
        super.tokenCookie = null;
        if (StringUtil.isEmpty(stuNo) || StringUtil.isEmpty(password) || StringUtil.isEmpty(userKey)) {
            statusBean.setCode(StatusBean.CODE_ERR_LOGIN);
            return false;
        }
        
        try {
            long sign = (new Date()).getTime();
            String pwd = CdutMd5.hex_md5(stuNo + sign + CdutMd5.hex_md5(password.trim()));
            
            Map<String, String> map = new HashMap<>();
            map.put("Action", "Login");
            map.put("userName", stuNo);
            map.put("pwd", pwd);
            map.put("sign", String.valueOf(sign));
            
            // httpURLConnection.getHeaderField("Set-Cookie")
//            return HttpUtil.postReturnCookie(urlLogin, map);
            TokenCookie tokenCookie = new TokenCookie();
            String data = HttpUtil.postReturnCookie(urlLogin, map, tokenCookie);
            if ("0".equals(data)) {
                statusBean.setCode(StatusBean.CODE_SUCCESS);
                super.tokenCookie = tokenCookie.get();
                return true;
            } else if ("4".equals(data)) {
                statusBean.setCode(StatusBean.CODE_ERR_ACCT_OR_PWD);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        statusBean.setCode(StatusBean.CODE_ERR_LOGIN);
        return false;
    }

    @Override
    public String grabCoursePage(StatusBean statusBean) {
        System.out.println("CdutSpider - grabCoursePage");
        
        if (!isInited()) {
            statusBean.setCode(StatusBean.CODE_ERR_GRAB_COURSES);
            return null;
        }
        
        if (tmpCoursePage != null) {
            return tmpCoursePage;
        }
        
        try {
            String text = HttpUtil.get(urlHome, tokenCookie);
            Pattern pattern = Pattern.compile("<a[^>]+window.open\\(\"([^\"]+)\"[^>]+>查看课表</a>");
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                // 课表页链接
                // Like "Classroom/ProductionSchedule/StuProductionSchedule.aspx?termid=201601&stuID=xxxxxxxxxxxx"
                String coursePageUrl = urlHost + "/" + matcher.group(1);
                // 取到课表页
                // httpURLConnection.setRequestProperty("Cookie", tokenCookie);
                tmpCoursePage = HttpUtil.get(coursePageUrl, tokenCookie);
                return tmpCoursePage;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        statusBean.setCode(StatusBean.CODE_ERR_GRAB_COURSES);
        return null;
    }

    @Override
    public boolean grabUserInfo(KeyBean keyBean, StatusBean statusBean) {
        System.out.println("CdutSpider - grabUserInfo");
        
        if (keyBean == null) {
            statusBean.setCode(StatusBean.CODE_ERR_GRAB_USER_INFO);
            return false;
        }
        
        String coursePage = grabCoursePage(statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return false;
        }
        
        Pattern pattern = Pattern.compile("学号:(\\d{12,13})\\s+姓名:([^\\s]+)\\s+班级:(\\d{10,11})\\s+学院/专业:(.+)学院(\\d{4})([^\\s]+)\\s+年级:(\\d{4})\\s+生成日期:(\\d{4})", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(coursePage);
        if (matcher.find()) {
            keyBean.setAcademy(matcher.group(4));
            keyBean.setMajor(matcher.group(6));
            keyBean.setUserName(matcher.group(2));
            return true;
        }
        
        statusBean.setCode(StatusBean.CODE_ERR_GRAB_USER_INFO);
        return false;
    }

    @Override
    public List<CourseBean> grabCourses(StatusBean statusBean) {
        System.out.println("CdutSpider - grabCourses");
        
        List<CourseBean> list = new ArrayList<>();
        
        String coursePage = grabCoursePage(statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return list;
        }
        
        int year = -1; // 所属年份（可能跨年）
        int term = 0; // 学期
        Pattern pattern = Pattern.compile("<td\\s+class='title nob'.+?\\((\\d{4})-\\d{4}学年第(.)学期\\)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(coursePage);
        if (matcher.find()) {
            year = Integer.parseInt(matcher.group(1));
            term = year * 10;
            if ("一".equals(matcher.group(2))) {
                term += 1;
            } else if ("二".equals(matcher.group(2))) {
                term += 2;
                year += 1;
            }
        }
        
//        pattern = Pattern.compile("学号:(\\d{12,13})\\s+姓名:([^\\s]+)\\s+班级:(\\d{10,11})\\s+学院/专业:(.+)学院(\\d{4})([^\\s]+)\\s+年级:(\\d{4})\\s+生成日期:(\\d{4})", Pattern.DOTALL);
//        matcher = pattern.matcher(pageText);
//        if (matcher.find()) {
//            studentNo = matcher.group(1); // 学号
//            year = Integer.parseInt(matcher.group(8));
//        }
        
        String detailText = null; // 页面底部课程信息
        pattern = Pattern.compile("<table class='tab2'>(.+?)</table>", Pattern.DOTALL);
        matcher = pattern.matcher(coursePage);
        if (matcher.find()) {
            detailText = matcher.group(1);
        }
        
        // 断言 懒惰 无换行
        pattern = Pattern.compile("(?<=<tr>\r?\n)<td[^>]+>(\\d{2})周<br/>(\\d{2}/\\d{2})-(\\d{2}/\\d{2}).+?(?=</tr>)", Pattern.DOTALL);
        matcher = pattern.matcher(coursePage);
        String dateWeekStartStr = "";
        while (matcher.find()) {
            int weekOrder = Integer.parseInt(matcher.group(1));
            Date dateWeekStart;
            if (matcher.group(2).compareTo(dateWeekStartStr) < 0) { // 01/02 12/26
                ++year;
            }
            dateWeekStartStr = matcher.group(2);
            dateWeekStart = DateUtil.parseDate(year + "/" + dateWeekStartStr, "yyyy/MM/dd");
            
            Pattern pattern1 = Pattern.compile("<td\\s+class='fontcss'[^>]*?(?:colspan='(\\d{1,2})'[^>]*?)?>"
                    + "(?:<u>)?([^<]*?)(?:</u>)?([A-Z0-9]{2})?(?:<br/><font[^>]*>(.*?)</font>)?</td>");
            Matcher matcher1 = pattern1.matcher(matcher.group(0));
            int grids = 0;
            int classes = 0;
            while (matcher1.find()) {
                int n = (matcher1.group(1) == null ? 1 : Integer.parseInt(matcher1.group(1)));
                grids += n;
                if (grids % 12 == 5) {
                    continue;
                }
                n = n == 12 ? 11 : n;
                
                CourseBean courseBean = new CourseBean();
                courseBean.setUserKey(userKey);
                courseBean.setDate(dateWeekStart);
                courseBean.setWeekOrder(weekOrder);
                courseBean.setTerm(term);
                String nameAbbr = matcher1.group(2);
                if (!"&nbsp;".equals(nameAbbr)) {
                    courseBean.setNameAbbr(nameAbbr);
                    String nameCode = matcher1.group(3);
                    if (detailText != null && nameCode != null) {
                        Pattern pattern2 = Pattern.compile("\\(" + nameAbbr + "\\)(.+?) \\(ID.+?"
                                + nameCode.charAt(0) + "\\d{1}" + nameCode.charAt(1) + "\\(.+?师\\[([^\\]]+)\\]", Pattern.DOTALL);
                        Matcher matcher2 = pattern2.matcher(detailText);
                        if (matcher2.find()) {
                            courseBean.setName(matcher2.group(1));
                            courseBean.setLecturer(matcher2.group(2));
                        }
                    }
                    courseBean.setRoomAbbr(matcher1.group(4));
                } else {
                    courseBean.setSleep(true);
                }
                courseBean.setCourseOrder((classes + 1) % 11 != 0 ? ((classes + 1) % 11) : 11);
                courseBean.setCourseNum(n);
                
                classes += n;
                if (classes % 11 == 0) {
                    dateWeekStart = DateUtil.addDate(dateWeekStart, 1);
                }
                
                // 如果和上堂一样没课，则合并
                if (courseBean.isSleep() && list.size() > 0) {
                    CourseBean courseBeanPrev = list.get(list.size() - 1);
                    if (courseBeanPrev.isSleep() && courseBeanPrev.getDate().getTime() == courseBean.getDate().getTime()) {
                        courseBeanPrev.setCourseNum(courseBeanPrev.getCourseNum() + 1);
                        continue;
                    }
                }
                list.add(courseBean);
            }
        }
        
        System.out.println("CdutSpider - grabCourses DONE " + list.size());
        if (list.isEmpty()) {
            statusBean.setCode(StatusBean.CODE_ERR_REGEX_COURSES);
        }
        return list;
    }

    @Override
    public boolean checkTerm(StatusBean statusBean) {
        System.out.println("CdutSpider - checkTerm");
        
        // 无需检查，课表与时间一一对应。点赞！
        return true;
    }
}
