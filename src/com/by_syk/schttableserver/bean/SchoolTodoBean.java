package com.by_syk.schttableserver.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SQL:
CREATE TABLE schooltodo(
  name VARCHAR(32) PRIMARY KEY,
  deprecated TINYINT(1) NOT NULL DEFAULT 0,
  time TIMESTAMP NOT NULL
);
 *
 * 计划支持的学校
 * 
 * @author By_syk
 */
@Entity
@Table(name = "schooltodo")
public class SchoolTodoBean {
    // 学校名称
    @Id
    @Column(name = "name")
    private String name;
    
    // 已确定不支持
    @Column(name = "deprecated")
    private boolean deprecated;
    
    @Column(name = "time", nullable = false)
    private Date time;
    
    public SchoolTodoBean() {
        setTime(new Date());
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
    
    public void setDeprecacted(boolean deprecated) {
        this.deprecated = deprecated;
    }
    
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
