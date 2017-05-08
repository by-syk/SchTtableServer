package com.by_syk.schttableserver.core.gdou;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.by_syk.graphiccr.core.GraphicCTranslator;
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
 * 广东海洋大学
 */
public class GdouSpider extends BaseSpider {
    private String urlHost;
    private String urlIndex;
    private String urlLogin;
    private String urlGraphicC;
    private String urlHome;
    private int term; // 学期
    private Date weekOrder1StartDate;

    private String tmpCoursePage = null;
    
    public GdouSpider(Config config) {
        super(config);
    }

    @Override
    protected void loadConfig(Config config) {
        urlHost = config.getGdouHost();
        urlIndex = urlHost + config.getGdouIndex();
        urlLogin = urlHost + config.getGdouLogin();
        urlGraphicC = urlHost + config.getGdouGraphicC();
        urlHome = urlHost + config.getGdouHome();
        term = config.getGdouTerm();
        weekOrder1StartDate = DateUtil.parseDate(config.getGdouWeekOrder1StartDate(), "yyyyMMdd");
    }

    @Override
    public boolean init(String stuNo, String pwd, String userKey, StatusBean statusBean) {
        System.out.println("GdouSpider - init");
        
        super.stuNo = stuNo;
        super.pwd = pwd;
        super.userKey = userKey;
        super.tokenCookie = null;
        if (StringUtil.isEmpty(stuNo) || StringUtil.isEmpty(pwd) || StringUtil.isEmpty(userKey)) {
            statusBean.setCode(StatusBean.CODE_ERR_LOGIN);
            return false;
        }
        
        try {
            String cookie = null;
            for (int i = 0; i < 3; ++i) {
                // 取到Cookie
                if (cookie == null) {
                    cookie = HttpUtil.getReturnCookie(urlIndex);
                }
                
                String pageData = HttpUtil.get(urlIndex, cookie);
                Pattern pattern = Pattern.compile("<input[^>]*name=\"__VIEWSTATE\" value=\"([^\"]+)\"");
                Matcher matcher = pattern.matcher(pageData);
                if (!matcher.find()) {
                    statusBean.setCode(StatusBean.CODE_ERR_LOGIN);
                    return false;
                }
                String viewState = matcher.group(1);
                
                // 取到验证码并识别
                File graphicCFile = File.createTempFile("graphicc", "gif");
                HttpUtil.downloadFile(urlGraphicC, cookie, graphicCFile.getPath());
                String code = GraphicCTranslator.translate(graphicCFile, GraphicCTranslator.TYPE_2);
                System.out.println("GraphicCR: " + code);
                
                // 登录，使Cookie有效化
                Map<String, String> map = new HashMap<>();
                map.put("__VIEWSTATE", viewState);
                map.put("txtUserName", stuNo);
                map.put("TextBox2", pwd);
                map.put("txtSecretCode", "1234"); // 验证码
                map.put("Textbox1", "");
                map.put("Button1", "");
                map.put("lbLanguage", "");
                map.put("hidPdrs", "");
                map.put("hidsc", "");
                map.put("RadioButtonList1", URLEncoder.encode("学生", "GBK")); // 角色（GBK编码）
                String data = HttpUtil.post(urlLogin, map, cookie);
                
                if (data.contains("<title>正方教务管理系统</title>")) {
                    statusBean.setCode(StatusBean.CODE_SUCCESS);
                    tokenCookie = cookie;
                    return true;
                }
                if (data.contains("用户名不存在或未按照要求参加教学活动！！")) {
                    statusBean.setCode(StatusBean.CODE_ERR_ACCOUNT);
                    return false;
                }
                if (data.contains("密码错误！！")) {
                    statusBean.setCode(StatusBean.CODE_ERR_PASSWORD);
                    return false;
                }
                if (!data.contains("验证码不正确！！")) { // 非验证码识别失败，不再继续尝试
                    statusBean.setCode(StatusBean.CODE_ERR_LOGIN);
                    return false;
                }
            }
            statusBean.setCode(StatusBean.CODE_ERR_GRAPHIC_CODE);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        statusBean.setCode(StatusBean.CODE_ERR_LOGIN);
        return false;
    }

    @Override
    public String grabCoursePage(StatusBean statusBean) {
        System.out.println("GdouSpider - grabCoursePage");
        
        if (!isInited()) {
            statusBean.setCode(StatusBean.CODE_ERR_GRAB_COURSES);
            return null;
        }
        
        if (tmpCoursePage != null) {
            return tmpCoursePage;
        }
        
        try {
            String text = HttpUtil.get(urlHome + "?xh=" + stuNo, tokenCookie);
            Pattern pattern = Pattern.compile("<a href=\"([^\"]+)\"[^>]*>学生个人课表</a>");
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                // Like "xskbcx.aspx?xh=xxxxxxxxxx&xm=？？？&gnmkdm=N121602"
                String coursePageUrl = urlHost + "/" + matcher.group(1);
                matcher = Pattern.compile("&xm=(.+?)&").matcher(coursePageUrl);
                if (matcher.find()) {
                    coursePageUrl = coursePageUrl.replace(matcher.group(1),
                            URLEncoder.encode(matcher.group(1), "GBK"));
                    
                    Map<String, String> headerMap = new HashMap<>();
                    headerMap.put("Cookie", tokenCookie);
                    headerMap.put("Referer", urlHome); // !!!
                    tmpCoursePage = HttpUtil.get(coursePageUrl, headerMap);
                    return tmpCoursePage;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        statusBean.setCode(StatusBean.CODE_ERR_GRAB_COURSES);
        return null;
    }

    @Override
    public boolean grabUserInfo(KeyBean keyBean, StatusBean statusBean) {
        System.out.println("GdouSpider - grabUserInfo");
        
        if (keyBean == null) {
            statusBean.setCode(StatusBean.CODE_ERR_GRAB_USER_INFO);
            return false;
        }
        
        String coursePage = grabCoursePage(statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return false;
        }
        
        Pattern pattern = Pattern.compile("<span id=\"Label6\">姓名：(.+?)</span>.+?学院：(.+?)</span>.+?专业：(.+?)</span>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(coursePage);
        if (matcher.find()) {
            keyBean.setAcademy(matcher.group(2).replace("学院", ""));
            keyBean.setMajor(matcher.group(3));
            keyBean.setUserName(matcher.group(1));
            return true;
        }
        
        statusBean.setCode(StatusBean.CODE_ERR_GRAB_USER_INFO);
        return false;
    }

    @Override
    public List<CourseBean> grabCourses(StatusBean statusBean) {
        System.out.println("GdouSpider - grabCourses");
        
        List<CourseBean> list = new ArrayList<>();
        
        String coursePage = grabCoursePage(statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return list;
        }
        
        coursePage = coursePage.replaceAll("\r?\n", "");
        
        int weekNum = 0; // 周数
        Pattern pattern = Pattern.compile("第(?:\\d{1,2}-)*(\\d{1,2})周");
        Matcher matcher = pattern.matcher(coursePage);
        while (matcher.find()) {
            int num = Integer.parseInt(matcher.group(1));
            if (num > weekNum) {
                weekNum = num;
            }
        }
        
        int dayCourseNum = 0; // 每天课程节数
        pattern = Pattern.compile(">第(\\d{1,2})节</td>");
        matcher = pattern.matcher(coursePage);
        while (matcher.find()) {
            int num = Integer.parseInt(matcher.group(1));
            if (num > dayCourseNum) {
                dayCourseNum = num;
            }
        }
        
        int[] weekDaysCourseNum = new int[7]; // 记录周一到周日每天处理过的节数
        
        pattern = Pattern.compile("<td[^>]*>第(\\d{1,2})节</td>.+?</tr>");
        matcher = pattern.matcher(coursePage);
        while (matcher.find()) { // 每一行
            int courseOrder = Integer.parseInt(matcher.group(1));
            
            Pattern pattern1 = Pattern.compile("<td[^>]*align=\"Center\"( rowspan=\"(\\d)\")*.+?</td>");
            Matcher matcher1 = pattern1.matcher(matcher.group(0));
            int curWeekday = -1; // 今天周几（周一为0）
            while (matcher1.find()) { // 每一行中每一格
                for (int i = 0; i < 7; ++i) {
                    if (weekDaysCourseNum[i] < courseOrder) {
                        curWeekday = i;
                        break;
                    }
                }
                if (matcher1.group(2) != null) {
                    weekDaysCourseNum[curWeekday] = weekDaysCourseNum[curWeekday]
                            + Integer.parseInt(matcher1.group(2));
                } else {
                    weekDaysCourseNum[curWeekday] = weekDaysCourseNum[curWeekday] + 1;
                }
                
                Pattern pattern2 = Pattern.compile(">([^<]*)<br>周(一|二|三|四|五|六|日)第(\\d{1,2}),?(\\d{1,2})*节"
                        + "\\{第([^\\}]*)周\\}<br>([^<]*)<br>([^<]*)<");
                Matcher matcher2 = pattern2.matcher(matcher1.group(0));
                while (matcher2.find()) {
                    for (Integer integer : parseWeeks(matcher2.group(5))) {
                        CourseBean courseBean = new CourseBean();
                        courseBean.setUserKey(userKey);
                        courseBean.setName(matcher2.group(1));
                        courseBean.setRoomAbbr(matcher2.group(7));
                        courseBean.setLecturer(matcher2.group(6));
//                        courseBean.setCourseOrder(courseOrder);
                        courseBean.setCourseOrder(Integer.parseInt(matcher2.group(3)));
                        if (matcher2.group(4) != null && Integer.parseInt(matcher2.group(4))
                                == Integer.parseInt(matcher2.group(3)) + 1) {
                            courseBean.setCourseNum(2);
                        } else {
                            courseBean.setCourseNum(1);
                        }
//                        courseBean.setDate(DateUtil.addDate(weekOrder1StartDate,
//                                (integer - 1) * 7 + curWeekday));
                        courseBean.setDate(DateUtil.addDate(weekOrder1StartDate,
                                (integer - 1) * 7 + parseWeekday(matcher2.group(2)) - 1));
                        courseBean.setWeekOrder(integer);
                        courseBean.setTerm(term);
                        list.add(courseBean);
                    }
                }
                
                pattern2 = Pattern.compile(">([^<]*)<br>"
                        + "\\{第([^\\|]*)周\\|(\\d)节/周\\}<br>([^<]*)<br>([^<]*)<");
                matcher2 = pattern2.matcher(matcher1.group(0));
                while (matcher2.find()) {
                    for (Integer integer : parseWeeks(matcher2.group(2))) {
                        CourseBean courseBean = new CourseBean();
                        courseBean.setUserKey(userKey);
                        courseBean.setName(matcher2.group(1));
                        courseBean.setRoomAbbr(matcher2.group(5));
                        courseBean.setLecturer(matcher2.group(4));
                        courseBean.setCourseOrder(courseOrder);
                        courseBean.setCourseNum(Integer.parseInt(matcher2.group(3)));
                        courseBean.setDate(DateUtil.addDate(weekOrder1StartDate,
                                (integer - 1) * 7 + curWeekday));
                        courseBean.setWeekOrder(integer);
                        courseBean.setTerm(term);
                        list.add(courseBean);
                    }
                }
            }
        }
        
        CourseUtil.fixCourseList(list, weekOrder1StartDate, weekNum, dayCourseNum);
        System.out.println("GdouSpider - grabCourses DONE " + list.size());
        return list;
    }

    @Override
    public boolean checkTerm(StatusBean statusBean) {
        System.out.println("GdouSpider - checkTerm");
        
        String coursePage = grabCoursePage(statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return false;
        }
        
        int term = 0; // 学期
        Pattern pattern = Pattern.compile("<option selected=\"selected\" value=\"(\\d{4})-\\d{4}\".+?"
                + "<option selected=\"selected\" value=\"(\\d{1})\"", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(coursePage);
        if (matcher.find()) {
            term = Integer.parseInt(matcher.group(1)) * 10
                    + Integer.parseInt(matcher.group(2));
        }
        
        return term == this.term;
    }
    
    /**
     * 第1-9周
     * 第1-1周|单周
     * 第6-6周|双周
     */
    private static List<Integer> parseWeeks(String text) {
        List<Integer> list = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return list;
        }
        
        boolean allWeeks = !text.contains("单") && !text.contains("双");
        int singleOrDouble = text.contains("单") ? 1 : 0;
        Matcher matcher = Pattern.compile("(\\d{1,2})-(\\d{1,2})").matcher(text);
        if (matcher.find()) {
            for (int i = Integer.parseInt(matcher.group(1)), end = Integer.parseInt(matcher.group(2)); i <= end; ++i) {
                if (allWeeks || i % 2 == singleOrDouble) {
                    list.add(i);
                }
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
