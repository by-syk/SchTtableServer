package com.by_syk.schttableserver.vo;

import java.io.Serializable;

import com.by_syk.schttableserver.bean.SchoolTodoBean;

public class SchoolTodoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    // 学校名称
    private String name;
    
    // 已确定不支持
    private boolean deprecated;

    public SchoolTodoVo() {}
    
    public SchoolTodoVo(SchoolTodoBean bean) {
        parse(bean);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeprecated() {
        return deprecated;
    }
    
    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public void parse(SchoolTodoBean bean) {
        if (bean == null) {
            return;
        }
        
        setName(bean.getName());
        setDeprecated(bean.isDeprecated());
    }
}
