package com.by_syk.schttableserver.core.gdou;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.by_syk.schttableserver.config.Config;
import com.by_syk.schttableserver.core.BaseGather;
import com.by_syk.schttableserver.util.ExtraUtil;

/**
 * 广东海洋大学
 */
public class GdouGather extends BaseGather {
    private String urlHost;
    
    public GdouGather(Config config) {
        super(config);
    }

    @Override
    protected void loadConfig(Config config) {
        super.loadConfig(config);
        
        urlHost = config.getCmcHost();
    }
    
    @Override
    protected boolean pack(File zipFile, String coursePage) {
        System.out.println("GdouGather - pack");
        
        coursePage = purify(coursePage);
        coursePage = fixLinks(coursePage);
        return ExtraUtil.zip(zipFile, new String[]{COURSE_PAGE_FILE_NAME},
                new String[]{coursePage});
    }

    private String purify(String coursePage) {
        if (coursePage == null) {
            return null;
        }
        
        Pattern pattern = Pattern.compile("<option selected=\"selected\" value=\"(\\d{4}-\\d{4})\".+?"
                + "<option selected=\"selected\" value=\"(\\d{1})\"", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(coursePage);
        if (matcher.find()) {
            coursePage = coursePage.replace("学年第", matcher.group(1) + "学年第" + matcher.group(2));
        }
        
        pattern = Pattern.compile("<select.+?</select>", Pattern.DOTALL);
        matcher = pattern.matcher(coursePage);
        if (matcher.find()) {
            coursePage = matcher.replaceAll("");
        }
        
        return coursePage;
    }
    
    private String fixLinks(String coursePage) {
        if (coursePage == null) {
            return null;
        }
        
        return coursePage.replaceAll("href=\"style", "href=\"" + urlHost + "/style")
                .replaceAll("gb2312", "utf-8");
    }
}
