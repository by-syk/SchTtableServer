package com.by_syk.schttableserver.core.neuq;

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
import com.by_syk.schttableserver.util.TokenCookie;
import com.by_syk.schttableserver.util.net.HttpUtil;

public class NeuqSpider extends BaseSpider {
    private String urlHost;
    private String urlIndex;
    private String urlLogin;
    private String urlLogin1;
    private String urlCourses;
    private int term; // 学期
    private Date weekOrder1StartDate;

    private String tmpCoursePage = null;
    
    public NeuqSpider(Config config) {
        super(config);
        
        urlIndex = "http://jwxt.neuq.edu.cn";
        urlLogin = "http://jwxt.neuq.edu.cn/xtgl/login_cxCheckYh.html";
        urlLogin1 = "http://jwxt.neuq.edu.cn/xtgl/login_login.html";
        urlCourses = "http://jwxt.neuq.edu.cn/kbcx/xskbcx_cxXsKb.html?gnmkdmKey=N253508";
    }

    @Override
    protected void loadConfig(Config config) {
        urlHost = config.getNeuqHost();
        urlIndex = urlHost + config.getNeuqIndex();
        urlLogin = urlHost + config.getNeuqLogin();
        urlLogin1 = urlHost + config.getNeuqLogin1();
        urlCourses = urlHost + config.getNeuqCourses();
        term = config.getNeuqTerm();
        weekOrder1StartDate = DateUtil.parseDate(config.getNeuqWeekOrder1StartDate(), "yyyyMMdd");
    }
    
    public static void main(String[] args) {
        NeuqSpider spider = new NeuqSpider(new Config());
        spider.init("5142102", "xfhy521only", "1", new StatusBean());
        System.out.println(spider.grabCoursePage(new StatusBean()));;
    }

    @Override
    public boolean init(String stuNo, String pwd, String userKey, StatusBean statusBean) {
        System.out.println("NeuqSpider - init");
        
        super.stuNo = stuNo;
        super.pwd = pwd;
        super.userKey = userKey;
        super.tokenCookie = null;
        if (StringUtil.isEmpty(stuNo) || StringUtil.isEmpty(pwd) || StringUtil.isEmpty(userKey)) {
            statusBean.setCode(StatusBean.CODE_ERR_LOGIN);
            return false;
        }
        
        try {
            Map<String, String> map = new HashMap<>();
            map.put("yhm", stuNo);
            map.put("mm", pwd);
            map.put("yzm", ""); // 验证码（多次登录失败需要验证码）
            
            TokenCookie tokenCookie = new TokenCookie();
            String data = HttpUtil.postReturnCookie(urlLogin, map, tokenCookie);
            if (data.contains("用户名或密码不正确")) {
                statusBean.setCode(StatusBean.CODE_ERR_ACCT_OR_PWD);
                return false;
            }
            if (data.contains("\"success\"")) {
                System.out.println(tokenCookie.get());
                data = HttpUtil.postReturnCookie(urlLogin1, map, tokenCookie.get(), tokenCookie);
                if (data.contains("登录超时")) {
                    statusBean.setCode(StatusBean.CODE_SUCCESS);
                    super.tokenCookie = tokenCookie.get();
                    System.out.println(tokenCookie.get());
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        statusBean.setCode(StatusBean.CODE_ERR_LOGIN);
        return false;
    }

    @Override
    public String grabCoursePage(StatusBean statusBean) {
        System.out.println("NeuqSpider - grabCoursePage");
        
        if (!isInited()) {
            statusBean.setCode(StatusBean.CODE_ERR_GRAB_COURSES);
            return null;
        }
        
        if (tmpCoursePage != null) {
            return tmpCoursePage;
        }
        
        try {
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Cookie", tokenCookie);
            return HttpUtil.get("http://jwxt.neuq.edu.cn/xtgl/index_initMenu.html?jsdm=xs&_t=" + System.currentTimeMillis(), headerMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        statusBean.setCode(StatusBean.CODE_ERR_GRAB_COURSES);
        return null;
    }

    @Override
    public boolean grabUserInfo(KeyBean keyBean, StatusBean statusBean) {
        System.out.println("NeuqSpider - grabUserInfo");
        
        if (keyBean == null) {
            statusBean.setCode(StatusBean.CODE_ERR_GRAB_USER_INFO);
            return false;
        }
        
        String coursePage = grabCoursePage(statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return false;
        }
        
        Pattern pattern = Pattern.compile("院系:(.+?)(&nbsp;)+专业:(.+?)(&nbsp;)+班级:(.+?)(&nbsp;)+学号:(.+?)(&nbsp;)+姓名:(.+?)</td>");
        Matcher matcher = pattern.matcher(coursePage);
        if (matcher.find()) {
            keyBean.setAcademy(matcher.group(1).replace("学院", ""));
            keyBean.setMajor(matcher.group(3));
            keyBean.setUserName(matcher.group(9));
            return true;
        }
        
        statusBean.setCode(StatusBean.CODE_ERR_GRAB_USER_INFO);
        return false;
    }

    @Override
    public List<CourseBean> grabCourses(StatusBean statusBean) {
        System.out.println("NeuqSpider - grabCourses");
        
        List<CourseBean> list = new ArrayList<>();
        
        String coursePage = grabCoursePage(statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return list;
        }
        
        int weekNum = 0; // 周数
        Pattern pattern = Pattern.compile("(\\d{1,2})周");
        Matcher matcher = pattern.matcher(coursePage);
        while (matcher.find()) {
            int num = Integer.parseInt(matcher.group(1));
            if (num > weekNum) {
                weekNum = num;
            }
        }
        
        int dayCourseNum = 0; // 每天课程数
        pattern = Pattern.compile("\\d{1,2} ?<br[^>]*> ?/ ?<br[^>]*> ?(\\d{1,2})");
        matcher = pattern.matcher(coursePage);
        while (matcher.find()) {
            int num = Integer.parseInt(matcher.group(1));
            if (num > dayCourseNum) {
                dayCourseNum = num;
            }
        }
        
        pattern = Pattern.compile("<td valign=\"top\" style=[^>]+(>.+?)</td>");
        matcher = pattern.matcher(coursePage);
        int grids = 0;
        while (matcher.find()) { // 7天 * 6堂 = 42次匹配
            ++grids;
            String coreText = matcher.group(1);
            if (!">&nbsp;".equals(coreText)) {
                Pattern pattern1 = Pattern.compile(">(.+?)<br[^>]+>(.+?)<br[^>]+>(.+?)<br[^>]+>(.+?)周  (\\d)节");
                Matcher matcher1 = pattern1.matcher(coreText);
                while (matcher1.find()) {
                    for (Integer integer : parseWeeks(matcher1.group(4))) {
                        CourseBean courseBean = new CourseBean();
                        courseBean.setUserKey(userKey);
                        courseBean.setName(matcher1.group(1));
                        courseBean.setRoomAbbr(matcher1.group(3));
                        courseBean.setLecturer(matcher1.group(2));
                        courseBean.setCourseOrder(((grids - 1) / 7 + 1) * 2 - 1);
                        courseBean.setCourseNum(2);
                        courseBean.setDate(DateUtil.addDate(weekOrder1StartDate,
                                (integer - 1) * 7 + (grids - 1) % 7));
                        courseBean.setWeekOrder(integer);
                        courseBean.setTerm(term);
                        list.add(courseBean);
                    }
                }
            }
        }
        
        CourseUtil.fixCourseList(list, weekOrder1StartDate, weekNum, dayCourseNum);
        System.out.println("NeuqSpider - grabCourses DONE " + list.size());
        return list;
    }

    @Override
    public boolean checkTerm(StatusBean statusBean) {
        System.out.println("NeuqSpider - checkTerm");
        
        String coursePage = grabCoursePage(statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return false;
        }
        
        int term = 0; // 学期
        Pattern pattern = Pattern.compile("东北大学秦皇岛分校(\\d{4})-\\d{4}学年第(.)学期学生课表");
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
     * 1-12
     * 1.3-6
     * 1-2.4-6
     */
    private static List<Integer> parseWeeks(String text) {
        List<Integer> list = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return list;
        }
        
        for (String unit : text.split("\\.")) {
            String[] numStartEnd = unit.split("-");
            int start = Integer.parseInt(numStartEnd[0]);
            list.add(start);
            if (numStartEnd.length == 1) {
                continue;
            } else {
                for (int i = start + 1, end = Integer.parseInt(numStartEnd[1]); i <= end; ++i) {
                    list.add(i);
                }
            }
        }
        
        return list;
    }
}
