package com.by_syk.schttableserver.core;

import java.io.File;

import com.by_syk.schttableserver.bean.StatusBean;
import com.by_syk.schttableserver.config.Config;
import com.by_syk.schttableserver.core.cdut.CdutGather;
import com.by_syk.schttableserver.core.cmc.CmcGather;
import com.by_syk.schttableserver.core.neuq.NeuqGather;
import com.by_syk.schttableserver.core.nju.NjuGather;
import com.by_syk.schttableserver.util.ExtraUtil;
import com.by_syk.schttableserver.util.StringUtil;

/**
 * 课表页面处理核心基类
 */
public abstract class BaseGather {
    /**
     * 缓存课表页面的目录
     */
    protected String htmlDir = null;
    
    protected static final String COURSE_PAGE_FILE_NAME = "index.html";
    
    public BaseGather(Config config) {
        loadConfig(config);
    }
    
    /**
     * 加载配置参数
     * 
     * @param config
     */
    protected void loadConfig(Config config) {
        htmlDir = config.getHtmlDir();
    }
    
    /**
     * 检查课表页面是否已缓存
     * 
     * @param userKey
     * @return
     */
    public boolean isCoursePageSaved(String userKey) {
        if (userKey == null) {
            return false;
        }
        
        File htmlDirFile = new File(htmlDir);
        if (!htmlDirFile.exists()) {
            return false;
        }
        
        File htmlFile = new File(htmlDirFile, "timetable_" + userKey + ".html");
        return htmlFile.exists();
    }
    
    /**
     * 检查课表页面封装包是否已缓存
     * 
     * @param userKey
     * @return
     */
    public boolean isCoursePagePackSaved(String userKey) {
        if (userKey == null) {
            return false;
        }
        
        File htmlDirFile = new File(htmlDir);
        if (!htmlDirFile.exists()) {
            return false;
        }
        
        File zipFile = new File(htmlDirFile, "timetable_" + userKey + ".zip");
        return zipFile.exists();
    }
    
    /**
     * 缓存原课表页面+封装包
     * 
     * @param userKey
     * @param coursePageData
     * @return
     */
    public boolean saveCoursePage(String userKey, String coursePage) {
        if (userKey == null || StringUtil.isEmpty(coursePage)) {
            return false;
        }
        
        File htmlDir = new File(this.htmlDir);
        if (!htmlDir.exists()) {
            htmlDir.mkdirs();
        }
        File htmlFile = new File(htmlDir, "timetable_" + userKey + ".html");
        File zipFile = new File(htmlDir, "timetable_" + userKey + ".zip");
        boolean ok = ExtraUtil.saveFile(coursePage, htmlFile);
        return ok && pack(zipFile, coursePage);
    }
    
    /**
     * 按用户 KEY 获取缓存的原课表页面
     * 
     * @param userKey
     * @return
     */
    public String getSavedCoursePage(String userKey) {
        if (userKey == null) {
            return null;
        }
        
        File htmlDirFile = new File(htmlDir);
        if (!htmlDirFile.exists()) {
            return null;
        }
        File htmlFile = new File(htmlDirFile, "timetable_" + userKey + ".html");
        return ExtraUtil.readFile(htmlFile);
    }
    
    /**
     * 按用户 KEY 获取缓存的原课表页面
     * 
     * @param userKey
     * @return
     */
    public byte[] getSavedCoursePagePack(String userKey) {
        if (userKey == null) {
            return null;
        }
        
        File htmlDirFile = new File(htmlDir);
        if (!htmlDirFile.exists()) {
            return null;
        }
        File zipFile = new File(htmlDirFile, "timetable_" + userKey + ".zip");
        return ExtraUtil.getFileBytes(zipFile);
    }
    
    /**
     * 封装课表页面（比如去掉多余代码，CSS链接修复等），用于独立展示
     * 
     * @param zipFile
     * @param coursePage
     * @return
     */
    protected abstract boolean pack(File zipFile, String coursePage);
    
    public static BaseGather chooseGather(Config config, String schoolCode, StatusBean statusBean) {
        if (config == null || StringUtil.isEmpty(schoolCode)) {
            statusBean.setCode(StatusBean.CODE_ERR_SCHOOL);
            return null;
        }
        
        if (config.getCdutSchoolCode().equals(schoolCode)) {
            return new CdutGather(config);
        } else if (config.getNeuqSchoolCode().equals(schoolCode)) {
            return new NeuqGather(config);
        } else if (config.getCmcSchoolCode().equals(schoolCode)) {
            return new CmcGather(config);
        } else if (config.getNjuSchoolCode().equals(schoolCode)) {
            return new NjuGather(config);
        }
        
        statusBean.setCode(StatusBean.CODE_ERR_SCHOOL);
        return null;
    }
}
