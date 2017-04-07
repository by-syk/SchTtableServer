package com.by_syk.schttableserver.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.by_syk.schttableserver.bean.StatusBean;

public class StatusVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    
    private int code = StatusBean.CODE_UNDEFINED;
    
    private String desc;
    
    private long time;
    
    public static Map<Integer, String> codeDescMap = new HashMap<>();
    
    static {
        codeDescMap.put(StatusBean.CODE_UNDEFINED, "无效状态码");
        codeDescMap.put(StatusBean.CODE_SUCCESS, "成功");
        codeDescMap.put(StatusBean.CODE_WAIT, "正在处理，稍后再查询结果");
        codeDescMap.put(StatusBean.CODE_ERR, "出错");
        codeDescMap.put(StatusBean.CODE_ERR_LOGIN, "登录失败");
        codeDescMap.put(StatusBean.CODE_ERR_ACCOUNT, "登录失败，学号不正确");
        codeDescMap.put(StatusBean.CODE_ERR_PASSWORD, "登录失败，密码不正确");
        codeDescMap.put(StatusBean.CODE_ERR_ACCT_OR_PWD, "登录失败，学号或密码有误");
        codeDescMap.put(StatusBean.CODE_ERR_GRAPHIC_CODE, "登录失败，无法识别验证码");
        codeDescMap.put(StatusBean.CODE_ERR_GRAB_USER_INFO, "抓取用户信息失败");
        codeDescMap.put(StatusBean.CODE_ERR_GRAB_COURSES, "抓取课表数据失败");
        codeDescMap.put(StatusBean.CODE_ERR_TERM, "学期与预期不符，不再解析");
        codeDescMap.put(StatusBean.CODE_ERR_REGEX_COURSES, "解析课表数据失败");
        codeDescMap.put(StatusBean.CODE_ERR_DAO, "数据入库错误");
        codeDescMap.put(StatusBean.CODE_ERR_USER, "无效用户");
        codeDescMap.put(StatusBean.CODE_ERR_EVIL_USER, "黑名单用户");
        codeDescMap.put(StatusBean.CODE_ERR_SCHOOL, "无效学校或不被支持");
    }
    
    public StatusVo() {}
    
    public StatusVo(StatusBean statusBean) {
        parse(statusBean);
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
    
    public void parse(StatusBean statusBean) {
        if (statusBean == null) {
            return;
        }
        
        setId(statusBean.getId());
        setCode(statusBean.getCode());
        setDesc(codeDescMap.get(statusBean.getCode()));
        setTime(statusBean.getTime().getTime());
    }
}
