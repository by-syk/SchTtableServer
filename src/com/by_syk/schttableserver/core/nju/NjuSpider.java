package com.by_syk.schttableserver.core.nju;

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
import com.by_syk.schttableserver.core.CourseUtil;
import com.by_syk.schttableserver.util.DateUtil;
import com.by_syk.schttableserver.util.StringUtil;
import com.by_syk.schttableserver.util.net.HttpUtil;

/**
 * 南京大学
 */
public class NjuSpider extends BaseSpider {
    private String urlHost;
    private String urlIndex;
    private String urlLogin;
    private String urlUserInfo;
    private String urlCourses;
    private int term; // 学期
    private Date weekOrder1StartDate;
    private int weeks; // 周数
    private int dayCourseNum;
    
    private String tmpCoursePage = null;
    
    public NjuSpider(Config config) {
        super(config);
    }

    @Override
    protected void loadConfig(Config config) {
        urlHost = config.getNjuHost();
        urlIndex = urlHost + config.getNjuIndex();
        urlLogin = urlHost + config.getNjuLogin();
        urlUserInfo = urlHost + config.getNjuUserInfo();
        urlCourses = urlHost + config.getNjuCourses();
        term = config.getNjuTerm();
        weekOrder1StartDate = DateUtil.parseDate(config.getNjuWeekOrder1StartDate(), "yyyyMMdd");
        weeks = config.getNjuWeeks();
        dayCourseNum = config.getNjuDayCourseNum();
    }
    
    @Override
    public boolean init(String stuNo, String pwd, String userKey, StatusBean statusBean) {
        System.out.println("NjuSpider - init");
        
        super.stuNo = stuNo;
        super.pwd = pwd;
        super.userKey = userKey;
        super.tokenCookie = null;
        if (StringUtil.isEmpty(stuNo) || StringUtil.isEmpty(pwd) || StringUtil.isEmpty(userKey)) {
            statusBean.setCode(StatusBean.CODE_ERR_LOGIN);
            return false;
        }
        
        try {
            // 取到Cookie
            String cookie = HttpUtil.getReturnCookie(urlIndex);
            
            // 登录，使Cookie有效化
            Map<String, String> map = new HashMap<>();
            map.put("userName", stuNo);
            map.put("password", pwd);
            map.put("returnUrl", "null");
            String data = HttpUtil.post(urlLogin, map, cookie);
            if (data.contains("欢迎您：")) {
                statusBean.setCode(StatusBean.CODE_SUCCESS);
                super.tokenCookie = cookie;
                return true;
            }
            if (data.contains("用户名或密码错误！")) {
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
        System.out.println("NjuSpider - grabCoursePage");
        
        if (!isInited()) {
            statusBean.setCode(StatusBean.CODE_ERR_GRAB_COURSES);
            return null;
        }
        
        if (tmpCoursePage != null) {
            return tmpCoursePage;
        }
        
        try {
            return HttpUtil.get(urlCourses + "?method=currentTermCourse", tokenCookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        statusBean.setCode(StatusBean.CODE_ERR_GRAB_COURSES);
        return null;
    }

    @Override
    public boolean grabUserInfo(KeyBean keyBean, StatusBean statusBean) {
        System.out.println("NjuSpider - grabUserInfo");
        
        if (keyBean == null) {
            statusBean.setCode(StatusBean.CODE_ERR_GRAB_USER_INFO);
            return false;
        }
        
        boolean result = false;
        try {
            String pageText = HttpUtil.get(urlUserInfo + "?method=searchAllList", tokenCookie);
            Pattern pattern = Pattern.compile("<td[^>]*>姓名</td>\r?\n\\s*<td[^>]*>([^<]*)<");
            Matcher matcher = pattern.matcher(pageText);
            if (matcher.find()) {
                keyBean.setUserName(matcher.group(1));
                result = true;
            }
            pattern = Pattern.compile("<td[^>]*>所在院系</TD>\r?\n\\s*<td[^>]*>([^<]*)<");
            matcher = pattern.matcher(pageText);
            if (matcher.find()) {
                keyBean.setAcademy(matcher.group(1).replace("学院", ""));
                result = true;
            }
            pattern = Pattern.compile("<td[^>]*>所在专业</TD>\r?\n\\s*<td[^>]*>([^<]*)<");
            matcher = pattern.matcher(pageText);
            if (matcher.find()) {
                keyBean.setMajor(matcher.group(1));
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (!result) {
            statusBean.setCode(StatusBean.CODE_ERR_GRAB_USER_INFO);
        }
        return result;
    }

    @Override
    public List<CourseBean> grabCourses(StatusBean statusBean) {
        System.out.println("NjuSpider - grabCourses");
        
        List<CourseBean> list = new ArrayList<>();
        
        String coursePage = grabCoursePage(statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return list;
        }
        
        coursePage = coursePage.replaceAll("\r?\n", "");
        
//        int weekNum = 0; // 周数
//        pattern = Pattern.compile("(\\d{1,2})周");
//        matcher = pattern.matcher(pageText);
//        while (matcher.find()) {
//            int num = Integer.parseInt(matcher.group(1));
//            if (num > weekNum) {
//                weekNum = num;
//            }
//        }
        
        Pattern pattern = Pattern.compile("<tr[^>]*class=\"TABLE_TR_0.\"\\s*>\\s*<td[^>]*>.+?</td>\\s*<!--.+?<td[^>]*>[^<]*</td>\\s*"
                + "<td[^>]*>([^<]*)</td>\\s*<td[^>]*>([^<]*)</td>\\s*<td[^>]*>([^<]*)</td>\\s*"
                + "<td[^>]*>\\s*(周.+?)\\s*</td>");
        Matcher matcher = pattern.matcher(coursePage);
        while (matcher.find()) {
            Pattern pattern1 = Pattern.compile("周(一|二|三|四|五|六|日)\\s+第(\\d{1,2})(-(\\d{1,2}))*节\\s+([^<]*)周([^<]*)");
            Matcher matcher1 = pattern1.matcher(matcher.group(4));
            while (matcher1.find()) {
                for (Integer integer : parseWeeks(matcher1.group(5), weeks)) {
                    CourseBean courseBean = new CourseBean();
                    courseBean.setUserKey(userKey);
                    courseBean.setName(matcher.group(1));
                    courseBean.setRoomAbbr(matcher1.group(6).replaceAll("\t", "").trim());
                    courseBean.setRoom(matcher.group(2) + " " + courseBean.getRoomAbbr());
                    courseBean.setLecturer(matcher.group(3));
                    courseBean.setCourseOrder(Integer.parseInt(matcher1.group(2)));
                    if (matcher1.group(4) != null) {
                        courseBean.setCourseNum(Integer.parseInt(matcher1.group(4))
                                - Integer.parseInt(matcher1.group(2)) + 1);
                    } else {
                        courseBean.setCourseNum(1);
                    }
                    courseBean.setDate(DateUtil.addDate(weekOrder1StartDate,
                            (integer - 1) * 7 + parseWeekday(matcher1.group(1)) - 1));
                    courseBean.setWeekOrder(integer);
                    courseBean.setTerm(term);
                    list.add(courseBean);
                }
            }
        }
        
        CourseUtil.fixCourseList(list, weekOrder1StartDate, weeks, dayCourseNum);
        System.out.println("NjuSpider - grabCourses DONE " + list.size());
        return list;
    }

    @Override
    public boolean checkTerm(StatusBean statusBean) {
        System.out.println("NjuSpider - checkTerm");
        
        String coursePage = grabCoursePage(statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return false;
        }
        
        int term = 0; // 学期
        Pattern pattern = Pattern.compile(">(\\d{4})-\\d{4}学年第(.)学期<");
        Matcher matcher = pattern.matcher(coursePage);
        if (matcher.find()) {
            term = Integer.parseInt(matcher.group(1)) * 10;
            if ("一".equals(matcher.group(2))) {
                term += 1;
            } else if ("二".equals(matcher.group(2))) {
                term += 2;
            }
        }
        
        return term == this.term;
    }
    
    /**
     * 2
     * 6-13
     * 单
     * 双
     */
    private static List<Integer> parseWeeks(String text, int weekNum) {
        List<Integer> list = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return list;
        }
        
        if (text.equals("单")) {
            for (int i = 1; i <= weekNum; i += 2) {
                list.add(i);
            }
            return list;
        } else if (text.equals("双")) {
            for (int i = 2; i <= weekNum; i += 2) {
                list.add(i);
            }
            return list;
        }
        
        String[] numStartEnd = text.split("-");
        int start = Integer.parseInt(numStartEnd[0]);
        list.add(start);
        if (numStartEnd.length == 1) {
            return list;
        } else {
            for (int i = start + 1, end = Integer.parseInt(numStartEnd[1]); i <= end; ++i) {
                list.add(i);
            }
        }
        
        return list;
    }
    
    /**
     * 一 二 三 四 五 六 日
     */
    private static int parseWeekday(String text) {
        if (text == null || text.length() == 0) {
            return -1;
        }
        
        switch (text) {
        case "一":
            return 1;
        case "二":
            return 2;
        case "三":
            return 3;
        case "四":
            return 4;
        case "五":
            return 5;
        case "六":
            return 6;
        case "日":
            return 7;
        }
        
        return -1;
    }
}
