package com.by_syk.schttableserver.core.cdut;

import java.io.File;

import com.by_syk.schttableserver.config.Config;
import com.by_syk.schttableserver.core.BaseGather;
import com.by_syk.schttableserver.util.ExtraUtil;

/**
 * 成都理工大学
 */
public class CdutGather extends BaseGather {
    public CdutGather(Config config) {
        super(config);
    }
    
    @Override
    protected boolean pack(File zipFile, String coursePage) {
        System.out.println("CdutGather - pack");
        
        return ExtraUtil.zip(zipFile, new String[]{COURSE_PAGE_FILE_NAME},
                new String[]{coursePage});
    }
    
//    private static String highlightToday(String coursePage) {
//        if (coursePage == null) {
//            return null;
//        }
//        
//        Calendar calendar = Calendar.getInstance();
//        int courses_start =  ((7 + calendar.get(Calendar.DAY_OF_WEEK) - 2) % 7) * 12 + 1;
//        int courses_end =  courses_start + 11;
//        
//        // 回到周一
//        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//        if (calendar.getTimeInMillis() > System.currentTimeMillis()) { // 矫正
//            calendar.add(Calendar.DAY_OF_MONTH, -7);
//        }
//        String date = DateUtil.getDateStr(calendar, "MM/dd");
//        
//        Pattern p = Pattern.compile("(?<=<tr>\r?\r?\n)<td[^>]+>\\d{2}周<br/>" + date + ".+?(?=</tr>)", Pattern.DOTALL);
//        Matcher m = p.matcher(coursePage);
//        int courses = 0;
//        if (m.find()) {
//            String weekHtml = m.group(0);
//            StringBuilder sbNewWeekHtml = new StringBuilder();
//            for (String line : weekHtml.split("\r?\n")) {
//                p = Pattern.compile("<td\\s+class='fontcss'.*?(colspan='(\\d{1,2})')*>(.+?)([A-Z0-9]{2})*(<br><font[^>]*>(.*?)</font>)*</td>");
//                m = p.matcher(line);
//                if (m.find()) {
//                    courses += m.group(2) == null ? 1 : Integer.parseInt(m.group(2));
//                    if (courses >= courses_start && courses <= courses_end) {
//                        line = "<td bgcolor=\"#dddddd\"" + line.substring(3);
//                    }
//                }
//                sbNewWeekHtml.append(line).append("\n");
//            }
//            return coursePage.replace(weekHtml, sbNewWeekHtml);
//        }
//        
//        return coursePage;
//    }
}
