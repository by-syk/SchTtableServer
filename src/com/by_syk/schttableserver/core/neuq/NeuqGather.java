package com.by_syk.schttableserver.core.neuq;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.by_syk.schttableserver.config.Config;
import com.by_syk.schttableserver.core.BaseGather;

public class NeuqGather extends BaseGather {
    private String urlHost;
    
    public NeuqGather(Config config) {
        super(config);
    }
    
    @Override
    protected void loadConfig(Config config) {
        super.loadConfig(config);
        
        urlHost = config.getNeuqHost();
    }
    
    @Override
    protected boolean pack(File zipFile, String coursePage) {
        System.out.println("NeuqGather - pack");
        
        return false;
    }
    
    private String purify(String coursePage) {
        if (coursePage == null) {
            return null;
        }
        
        //Pattern pattern = Pattern.compile("<table.+?class=tableborder>(.+?</form>\r?\n\\s*</td>\r?\n\\s*</tr>)", Pattern.DOTALL);
        Pattern pattern = Pattern.compile("<td[^>]+>学年学期.+?value=\"打印\"></td>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(coursePage);
        if (matcher.find()) {
            coursePage = matcher.replaceAll("");
        }
        
        pattern = Pattern.compile("-->\r?\n\\s*<tr>.+?退出系统.+?</tr>", Pattern.DOTALL);
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
        
        return coursePage.replaceAll("<link href=\"", "<link href=\"" + urlHost + "/");
    }
}
