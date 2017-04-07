package com.by_syk.schttableserver.core;

import java.util.List;

import com.by_syk.schttableserver.bean.CourseBean;
import com.by_syk.schttableserver.bean.KeyBean;
import com.by_syk.schttableserver.bean.StatusBean;
import com.by_syk.schttableserver.config.Config;
import com.by_syk.schttableserver.core.cdut.CdutSpider;
import com.by_syk.schttableserver.util.StringUtil;

/**
 * 课表抓取核心基类
 */
public abstract class BaseSpider {
    /**
     * 学号
     */
    protected String stuNo = null;
    
    /**
     * 密码
     */
    protected String pwd = null;
    
    /**
     * 用户 KEY
     */
    protected String userKey = null;
    
    /**
     * 登录成功的 Cookie
     */
    protected String tokenCookie = null;
    
    public BaseSpider(Config config) {
        loadConfig(config);
    }
    
    /**
     * 检查初始化完成（必要参数是否正确、是否登录成功拿到 Cookie）
     */
    public boolean isInited() {
        return !StringUtil.isEmpty(stuNo) && !StringUtil.isEmpty(pwd)
                && !StringUtil.isEmpty(userKey) && tokenCookie != null;
    }
    
    /**
     * 加载配置参数
     * 
     * @param config
     */
    protected abstract void loadConfig(Config config);
    
    /**
     * 初始化（登录，拿到 Cookie）
     * 
     * 以下方法应当在该方法之后调用：
     * {@link #grabCoursePage(boolean, StatusBean)}
     * {@link #grabUserInfo(KeyBean, StatusBean)}
     * {@link #grabCourses(String, StatusBean)}
     * {@link #checkTerm()}
     * 
     * @param stuNo 学号
     * @param password 密码
     * @param userKey
     * @param statusBean 不能为 null
     * @return
     */
    public abstract boolean init(String stuNo, String password, String userKey, StatusBean statusBean);
    
    /**
     * 抓取课表页面
     * （需要考虑到单独显示之用，必要时可以合并多个页面）
     * 
     * @param statusBean 不能为 null
     * @return
     */
    public abstract String grabCoursePage(StatusBean statusBean);
    
    /**
     * 抓取学生信息
     * 
     * @param keyBean
     * @param statusBean 不能为 null
     * @return
     */
    public abstract boolean grabUserInfo(KeyBean keyBean, StatusBean statusBean);
    
    /**
     * 抓取并解析课表数据
     * 
     * @param userKey
     * @param statusBean 不能为 null
     * @return
     */
    public abstract List<CourseBean> grabCourses(StatusBean statusBean);
    
    /**
     * 检查学期是否与配置相同
     * 
     * @param statusBean 不能为 null
     * @return
     */
    public abstract boolean checkTerm(StatusBean statusBean);
    
    public static BaseSpider chooseSpider(Config config, String schoolCode, StatusBean statusBean) {
        if (config == null || StringUtil.isEmpty(schoolCode)) {
            statusBean.setCode(StatusBean.CODE_ERR_SCHOOL);
            return null;
        }
        
        if (config.getCdutSchoolCode().equals(schoolCode)) {
            return new CdutSpider(config);
        }/* else if (config.getNeuqSchoolCode().equals(schoolCode)) {
            return new NeuqSpider(config);
        } else if (config.getCmcSchoolCode().equals(schoolCode)) {
            return new CmcSpider(config);
        }*/
        
        statusBean.setCode(StatusBean.CODE_ERR_SCHOOL);
        return null;
    }
}
