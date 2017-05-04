package com.by_syk.schttableserver.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SQL:
CREATE TABLE bug(
  _id INT AUTO_INCREMENT PRIMARY KEY,
  school_code CHAR(5) NOT NULL,
  stu_no VARCHAR(16) NOT NULL,
  course_date DATE NOT NULL,
  course_order INT NOT NULL,
  description VARCHAR(512),
  time TIMESTAMP NOT NULL
);
 *
 * BUG反馈
 * 
 * @author By_syk
 */
@Entity
@Table(name = "bug")
public class BugBean {
    @Id @GeneratedValue
    @Column(name = "_id")
    private int id;
    
    // 学校代码
    @Column(name = "school_code", nullable = false)
    private String schoolCode;
    
    // 学号
    @Column(name = "stu_no", nullable = false)
    private String stuNo;
    
    // 课程日期（精确到日）
    @Column(name = "course_date", nullable = false)
    private Date courseDate;

    // 课程节次（1~
    @Column(name = "course_order")
    private int courseOrder;

    // BUG描述
    @Column(name = "description")
    private String desc;
    
    // 提交时间
    @Column(name = "time", nullable = false)
    private Date time;
    
    public BugBean() {
        setTime(new Date());
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public Date getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(Date courseDate) {
        this.courseDate = courseDate;
    }

    public int getCourseOrder() {
        return courseOrder;
    }

    public void setCourseOrder(int courseOrder) {
        this.courseOrder = courseOrder;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
