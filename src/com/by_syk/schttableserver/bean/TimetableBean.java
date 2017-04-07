package com.by_syk.schttableserver.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SQL:
CREATE TABLE timetable(
  _id INT AUTO_INCREMENT PRIMARY KEY,
  school_code CHAR(5) NOT NULL,
  school_area INT NOT NULL DEFAULT 0,
  dst TINYINT(1) NOT NULL DEFAULT 0,
  course_order INT NOT NULL,
  time_start INT NOT NULL,
  time_end INT NOT NULL
);
 *
 * 作息时间表
 * 
 * @author By_syk
 */
@Entity
@Table(name = "timetable")
public class TimetableBean {
    @Id @GeneratedValue
    @Column(name = "_id")
    private int id;
    
    // 学校代码
    @Column(name = "school_code", unique = true, nullable = false)
    private String schoolCode;
    
    // 校区序号
    @Column(name = "school_area")
    private int schoolArea;
    
    // 执行夏季作息时间
    @Column(name = "dst")
    private boolean dst;
    
    // 节次
    @Column(name = "course_order")
    private int courseOrder;
    
    // 上课时间（格式：HHmm）
    @Column(name = "time_start")
    private int timeStart;

    // 下课时间（格式：HHmm）
    @Column(name = "time_end")
    private int timeEnd;

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public int getSchoolArea() {
        return schoolArea;
    }

    public void setSchoolArea(int schoolArea) {
        this.schoolArea = schoolArea;
    }

    public boolean isDst() {
        return dst;
    }

    public void setDst(boolean dst) {
        this.dst = dst;
    }

    public int getCourseOrder() {
        return courseOrder;
    }

    public void setCourseOrder(int courseOrder) {
        this.courseOrder = courseOrder;
    }

    public int getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(int timeStart) {
        this.timeStart = timeStart;
    }

    public int getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(int timeEnd) {
        this.timeEnd = timeEnd;
    }
}