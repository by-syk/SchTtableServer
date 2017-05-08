package com.by_syk.schttableserver.core.ncu;

import java.io.File;
import java.io.IOException;
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
 * 南昌大学
 */
public class NcuSpider extends BaseSpider {
    private String urlHost;
    private String urlIndex;
    private String urlLogin;
    private String urlGraphicC;
    private String urlCourses;
    private String urlUserInfo;
    private int term; // 学期
    private Date weekOrder1StartDate;
    
    private String tmpCoursePage = null;
    
    public NcuSpider(Config config) {
        super(config);
    }

    @Override
    protected void loadConfig(Config config) {
        urlHost = config.getNcuHost();
        urlIndex = urlHost + config.getNcuIndex();
        urlLogin = urlHost + config.getNcuLogin();
        urlGraphicC = urlHost + config.getNcuGraphicC();
        urlUserInfo = urlHost + config.getNcuUserInfo();
        urlCourses = urlHost + config.getNcuCourses();
        term = config.getNcuTerm();
        weekOrder1StartDate = DateUtil.parseDate(config.getNcuWeekOrder1StartDate(), "yyyyMMdd");
    }

    @Override
    public boolean init(String stuNo, String pwd, String userKey, StatusBean statusBean) {
        System.out.println("NcuSpider - init");
        
        super.stuNo = stuNo;
        super.pwd = pwd;
        super.userKey = userKey;
        super.tokenCookie = null;
        if (StringUtil.isEmpty(stuNo) || StringUtil.isEmpty(pwd) || StringUtil.isEmpty(userKey)) {
            statusBean.setCode(StatusBean.CODE_ERR_LOGIN);
            return false;
        }
        
        String encrypted = EncodeUtil.encodeInp(stuNo) + "%%%" + EncodeUtil.encodeInp(pwd);
        
        try {
            String cookie = null;
            for (int i = 0; i < 3; ++i) {
                // 取到Cookie
                if (cookie == null) {
                    cookie = HttpUtil.getReturnCookie(urlIndex);
                }
                
                // 取到验证码并识别
                File graphicCFile = File.createTempFile("graphicc", ".jpg");
                HttpUtil.downloadFile(urlGraphicC, cookie, graphicCFile.getPath());
                String code = GraphicCTranslator.translate(graphicCFile, GraphicCTranslator.TYPE_5);
                System.out.println("GraphicCR: " + code);
                
                // 登录，使Cookie有效化
                Map<String, String> map = new HashMap<>();
                map.put("encoded", encrypted);
                map.put("RANDOMCODE", code); // 验证码
                String data = HttpUtil.post(urlLogin, map, cookie);
                
                if (data.contains("<title>学生个人中心</title>")) {
                    statusBean.setCode(StatusBean.CODE_SUCCESS);
                    tokenCookie = cookie;
                    return true;
                }
                if (data.contains("用户名或密码错误")) {
                    statusBean.setCode(StatusBean.CODE_ERR_ACCT_OR_PWD);
                    return false;
                }
                if (!data.contains("验证码错误!!")) { // 非验证码识别失败，不再继续尝试
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
        System.out.println("NcuSpider - grabCoursePage");
        
        if (!isInited()) {
            statusBean.setCode(StatusBean.CODE_ERR_GRAB_COURSES);
            return null;
        }
        
        if (tmpCoursePage != null) {
            return tmpCoursePage;
        }
        
        try {
            return HttpUtil.get(urlCourses, tokenCookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        statusBean.setCode(StatusBean.CODE_ERR_GRAB_COURSES);
        return null;
    }

    @Override
    public boolean grabUserInfo(KeyBean keyBean, StatusBean statusBean) {
        System.out.println("NcuSpider - grabUserInfo");
        
        if (keyBean == null) {
            statusBean.setCode(StatusBean.CODE_ERR_GRAB_USER_INFO);
            return false;
        }
        
        boolean result = false;
        try {
            String pageText = HttpUtil.get(urlUserInfo, tokenCookie);
            Pattern pattern = Pattern.compile("<td[^>]*>姓名</td>\r?\n\\s*<td[^>]*>&nbsp;([^<]*)<");
            Matcher matcher = pattern.matcher(pageText);
            if (matcher.find()) {
                keyBean.setUserName(matcher.group(1));
                result = true;
            }
            pattern = Pattern.compile("<td[^>]*>院系：([^<]*)</td>");
            matcher = pattern.matcher(pageText);
            if (matcher.find()) {
                keyBean.setAcademy(matcher.group(1).replace("学院", ""));
                result = true;
            }
            pattern = Pattern.compile("<td[^>]*>专业：([^<]*)</td>");
            matcher = pattern.matcher(pageText);
            if (matcher.find()) {
                keyBean.setMajor(matcher.group(1));
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (!result) {
            statusBean.setCode(StatusBean.CODE_ERR_GRAB_USER_INFO);
        }
        return result;
    }

    @Override
    public List<CourseBean> grabCourses(StatusBean statusBean) {
        System.out.println("NcuSpider - grabCourses");
        
        List<CourseBean> list = new ArrayList<>();
        
        String coursePage = grabCoursePage(statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return list;
        }
        
        coursePage = coursePage.replaceAll("\r?\n", "");
        
        int weekNum = 0; // 周数
        Pattern pattern = Pattern.compile("(\\d{1,2})\\(.?周\\)");
        Matcher matcher = pattern.matcher(coursePage);
        while (matcher.find()) {
            int num = Integer.parseInt(matcher.group(1));
            if (num > weekNum) {
                weekNum = num;
            }
        }
        
        int dayCourseNum = 0; // 每天课程数
        pattern = Pattern.compile("<th[^>]*>\\s*\\d*(\\d{2})<br>");
        matcher = pattern.matcher(coursePage);
        while (matcher.find()) {
            int num = Integer.parseInt(matcher.group(1));
            if (num > dayCourseNum) {
                dayCourseNum = num;
            }
        }
        
        pattern = Pattern.compile("<tr>\\s*<th[^>]*>\\s*((\\d{2})\\d*)<br>.+?</tr>");
        matcher = pattern.matcher(coursePage);
        while (matcher.find()) { // 一行
            int courseOrder = Integer.parseInt(matcher.group(2));
            int courseNum = matcher.group(1).length() / 2;
            Pattern pattern1 = Pattern.compile("<div[^>]*class=\"kbcontent\"[^>]*(>.+?)</div>");
            Matcher matcher1 = pattern1.matcher(matcher.group(0));
            int grids = 0;
            while (matcher1.find()) { // 一格
                ++grids;
                String coreText = matcher1.group(1);
                if (!"&nbsp;".equals(coreText)) {
                    Pattern pattern2 = Pattern.compile(">([^<]*?)<br/><font title='老师'>(.+?)</font>"
                            + "<br/><font title='周次\\(节次\\)'>(.+?)</font><br/>(?:<font title='教室'>(.+?)</font>)?");
                    Matcher matcher2 = pattern2.matcher(coreText);
                    while (matcher2.find()) {
                        for (Integer integer : parseWeeks(matcher2.group(3))) {
                            CourseBean courseBean = new CourseBean();
                            courseBean.setUserKey(userKey);
                            courseBean.setName(matcher2.group(1));
                            courseBean.setRoom(matcher2.group(4));
                            courseBean.setLecturer(parseLecturers(matcher2.group(2)));
                            courseBean.setCourseOrder(courseOrder);
                            courseBean.setCourseNum(courseNum);
                            courseBean.setDate(DateUtil.addDate(weekOrder1StartDate,
                                    (integer - 1) * 7 + grids - 1));
                            courseBean.setWeekOrder(integer);
                            courseBean.setTerm(term);
                            list.add(courseBean);
                        }
                    }
                }
            }
        }
        
        CourseUtil.fixCourseList(list, weekOrder1StartDate, weekNum, dayCourseNum);
        System.out.println("NcuSpider - grabCourses DONE " + list.size());
        return list;
    }

    @Override
    public boolean checkTerm(StatusBean statusBean) {
        System.out.println("NcuSpider - checkTerm");
        
        String coursePage = grabCoursePage(statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return false;
        }
        
        int term = 0; // 学期
        Pattern pattern = Pattern.compile("<option\\s*value=\"(\\d{4})-\\d{4}-(\\d)\"\\s*selected=\"selected\">");
        Matcher matcher = pattern.matcher(coursePage);
        if (matcher.find()) {
            term = Integer.parseInt(matcher.group(1)) * 10
                    + Integer.parseInt(matcher.group(2));
        }
        
        return term == this.term;
    }
    
    /**
     * 2(周)
     * 1-11(周)
     * 10(双周)
     * 9(单周)
     */
    private static List<Integer> parseWeeks(String text) {
        List<Integer> list = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return list;
        }
        
        if (text.endsWith("(周)")) {
            String[] numStartEnd = text.substring(0, text.indexOf("(")).split("-");
            int start = Integer.parseInt(numStartEnd[0]);
            list.add(start);
            if (numStartEnd.length > 1) {
                for (int i = start + 1, end = Integer.parseInt(numStartEnd[1]); i <= end; ++i) {
                    list.add(i);
                }
            }
        } else if (text.endsWith("(双周)")) {
            int end = Integer.parseInt(text.substring(0, text.indexOf("(")));
            for (int i = 2; i <= end; i += 2) {
                list.add(i);
            }
        } else if (text.endsWith("(单周)")) {
            int end = Integer.parseInt(text.substring(0, text.indexOf("(")));
            for (int i = 1; i <= end; i += 2) {
                list.add(i);
            }
        }
        
        return list;
    }
    
    /**
     * 盛军庆讲师,史建伍讲师,洪一江教授
     * 简少卿其他中级,赵大显副教授
     */
    private static String parseLecturers(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        return text.replaceAll("教授|副教授|讲师|助教|其他正高级|其他副高级|其他中级|其他初级|未评级", "");
    }
}
