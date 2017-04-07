package com.by_syk.schttableserver.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SQL:
CREATE TABLE school(
  code CHAR(5) NOT NULL,
  area INT NOT NULL DEFAULT 0,
  name VARCHAR(32),
  stu_no_len INT NOT NULL DEFAULT 32,
  stu_no_regex VARCHAR(64),
  day_course_num1 INT NOT NULL,
  day_course_num2 INT NOT NULL,
  day_course_num3 INT NOT NULL,
  url VARCHAR(128),
  sort varchar(128),
  enabled TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (code, area)
);
 *
 * 支持学校
 * 
 * @author By_syk
 */
@Entity
@Table(name = "school")
public class SchoolBean implements Serializable {
    // Composite-id class must implement Serializable
    private static final long serialVersionUID = 1L;

    // 学校代码
    @Id
    @Column(name = "code", nullable = false)
    private String code;
    
    // 学校名称
    @Column(name = "name")
    private String name;
    
    // 校区序号
    @Id
    @Column(name = "area")
    private int area;
    
    // 学号长度
    @Column(name = "stu_no_len")
    private int stuNoLen;

    // 学号字符限制匹配，为null则默认匹配数字
    @Column(name = "stu_no_regex")
    private String stuNoRegex;

    // 上午课程节数
    @Column(name = "day_course_num1")
    private int dayCourseNum1;

    // 下午课程节数
    @Column(name = "day_course_num2")
    private int dayCourseNum2;

    // 晚上课程节数
    @Column(name = "day_course_num3")
    private int dayCourseNum3;

    // 学校教务网站链接
    @Column(name = "url")
    private String url;

    // 排序依据
    @Column(name = "sort")
    private String sort;

    // 是否可用
    @Column(name = "enabled")
    private boolean enabled;
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getStuNoLen() {
        return stuNoLen;
    }

    public void setStuNoLen(int stuNoLen) {
        this.stuNoLen = stuNoLen;
    }

    public String getStuNoRegex() {
        return stuNoRegex;
    }

    public void setStuNoRegex(String stuNoRegex) {
        this.stuNoRegex = stuNoRegex;
    }

    public int getDayCourseNum1() {
        return dayCourseNum1;
    }

    public void setDayCourseNum1(int dayCourseNum1) {
        this.dayCourseNum1 = dayCourseNum1;
    }

    public int getDayCourseNum2() {
        return dayCourseNum2;
    }

    public void setDayCourseNum2(int dayCourseNum2) {
        this.dayCourseNum2 = dayCourseNum2;
    }

    public int getDayCourseNum3() {
        return dayCourseNum3;
    }

    public void setDayCourseNum3(int dayCourseNum3) {
        this.dayCourseNum3 = dayCourseNum3;
    }

    public int getDayCourseNum() {
        return dayCourseNum1 + dayCourseNum2 + dayCourseNum3;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
