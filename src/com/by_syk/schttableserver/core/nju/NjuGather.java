package com.by_syk.schttableserver.core.nju;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.by_syk.schttableserver.config.Config;
import com.by_syk.schttableserver.core.BaseGather;
import com.by_syk.schttableserver.util.ExtraUtil;

/**
 * 南京大学
 */
public class NjuGather extends BaseGather {
    private String urlHost;
    
    public NjuGather(Config config) {
        super(config);
    }
    
    @Override
    protected void loadConfig(Config config) {
        super.loadConfig(config);
        
        urlHost = config.getNjuHost();
    }
    
    @Override
    protected boolean pack(File zipFile, String coursePage) {
        System.out.println("NjuGather - pack");
        
        coursePage = purify(coursePage);
        coursePage = fixLinks(coursePage);
        return ExtraUtil.zip(zipFile, new String[]{COURSE_PAGE_FILE_NAME},
                new String[]{coursePage});
    }
    
    private String purify(String coursePage) {
        if (coursePage == null) {
            return null;
        }
        
        Pattern pattern = Pattern.compile("<html>.+?</html>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(coursePage);
        if (matcher.find()) {
            coursePage = matcher.group(0);
        }
        
        pattern = Pattern.compile("<script type=\"text/javascript\">.+?<script>", Pattern.DOTALL);
        matcher = pattern.matcher(coursePage);
        if (matcher.find()) {
            coursePage = matcher.replaceAll("");
        }
        
        pattern = Pattern.compile("<TD colspan=\"2\" align=\"center\" style=\"color:blue; \">\\s+.+?</TD>", Pattern.DOTALL);
        matcher = pattern.matcher(coursePage);
        if (matcher.find()) {
            coursePage = matcher.replaceAll("");
        }
        
        coursePage = coursePage.replaceAll("<a href=\"[^\"]*\"", "<a href=\"javascript:void(0);\"");
        
        return coursePage;
    }
    
    private String fixLinks(String coursePage) {
        if (coursePage == null) {
            return null;
        }
        
        return coursePage.replaceAll("<link href=\"", "<link href=\"" + urlHost + "/");
    }
}
