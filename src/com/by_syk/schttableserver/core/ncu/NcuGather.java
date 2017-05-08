package com.by_syk.schttableserver.core.ncu;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.by_syk.schttableserver.config.Config;
import com.by_syk.schttableserver.core.BaseGather;
import com.by_syk.schttableserver.util.ExtraUtil;

/**
 * 南昌大学
 */
public class NcuGather extends BaseGather {
    private String urlHost;
    
    public NcuGather(Config config) {
        super(config);
    }

    @Override
    protected void loadConfig(Config config) {
        super.loadConfig(config);
        
        urlHost = config.getCmcHost();
    }
    
    @Override
    protected boolean pack(File zipFile, String coursePage) {
        System.out.println("NcuGather - pack");
        
        coursePage = purify(coursePage);
        coursePage = fixLinks(coursePage);
        return ExtraUtil.zip(zipFile, new String[]{COURSE_PAGE_FILE_NAME},
                new String[]{coursePage});
    }

    private String purify(String coursePage) {
        if (coursePage == null) {
            return null;
        }
        
        Pattern pattern = Pattern.compile("<body>.+?<div class=\"Nsb_layout_r\">", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(coursePage);
        if (matcher.find()) {
            coursePage = matcher.replaceAll("<body><div class=\"Nsb_layout_r\">");
        }
        
        return coursePage;
    }
    
    public String fixLinks(String coursePage) {
        if (coursePage == null) {
            return null;
        }
        
        return coursePage.replaceAll("<link href=\"/jsxsd/", "<link href=\"" + urlHost + "/")
                /*.replaceAll("src=\"/jsxsd/", "src=\"" + urlHost + "/")*/;
    }
}
