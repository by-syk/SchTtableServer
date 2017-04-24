package com.by_syk.schttableserver.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SQL:
CREATE TABLE course(
  user_key CHAR(36) NOT NULL,
  name VARCHAR(64),
  name_abbr VARCHAR(32),
  school_area INT NOT NULL DEFAULT 0,
  room VARCHAR(32),
  room_abbr VARCHAR(16),
  lecturer VARCHAR(128),
  course_order INT NOT NULL,
  course_num INT NOT NULL DEFAULT 1,
  date DATE NOT NULL,
  week INT NOT NULL,
  term INT NOT NULL,
  sleep TINYINT(1) NOT NULL DEFAULT 0,
  conflict INT NOT NULL DEFAULT 0,
  grab_date TIMESTAMP NOT NULL,
  PRIMARY KEY(user_key, course_order, date, conflict)
);
 *
 * 课程
 * 
 * @author By_syk
 */
@Entity
@Table(name = "course")
public class CourseBean implements Serializable {
    // Composite-id class must implement Serializable
    private static final long serialVersionUID = 1L;

    // 用户KEY
    @Id
    @Column(name = "user_key", nullable = false)
    private String userKey;
    
    // 课程名
    @Column(name = "name")
    private String name;
    
    // 课程名缩写
    @Column(name = "name_abbr")
    private String nameAbbr;
    
    // 校区序号
    @Column(name = "school_area")
    private int schoolArea;
    
    // 教室名
    @Column(name = "room")
    private String room;
    
    // 教室名缩写
    @Column(name = "room_abbr")
    private String roomAbbr;
    
    // 讲师名
    @Column(name = "lecturer")
    private String lecturer;
    
    // 节次（1~
    @Id
    @Column(name = "course_order")
    private int courseOrder;
    
    // 节数（1~
    @Column(name = "course_num")
    private int courseNum;
    
    // 日期（精确到日）
    @Id
    @Column(name = "date", nullable = false)
    private Date date;
    
    // 周次
    @Column(name = "week")
    private int weekOrder;
    
    // 学期（5位）
    // 20161 = 2016-2017学年第1学期
    @Column(name = "term")
    private int term;
    
    // 无课
    @Column(name = "sleep")
    private boolean sleep;
    
    // 重课标记，默认为0，非零表示有冲突的课
    @Id
    @Column(name = "conflict")
    private int conflict;
    
    // 数据抓取日期
    @Column(name = "grab_date", nullable = false)
    private Date grabDate;
    
    public CourseBean() {
        setGrabDate(new Date());
    }
    
    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameAbbr() {
        return nameAbbr;
    }

    public void setNameAbbr(String nameAbbr) {
        this.nameAbbr = nameAbbr;
    }

    public int getSchoolArea() {
        return schoolArea;
    }

    public void setSchoolArea(int schoolArea) {
        this.schoolArea = schoolArea;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getRoomAbbr() {
        return roomAbbr;
    }

    public void setRoomAbbr(String roomAbbr) {
        this.roomAbbr = roomAbbr;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public int getCourseOrder() {
        return courseOrder;
    }

    public void setCourseOrder(int courseOrder) {
        this.courseOrder = courseOrder;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(int courseNum) {
        this.courseNum = courseNum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getWeekOrder() {
        return weekOrder;
    }

    public void setWeekOrder(int weekOrder) {
        this.weekOrder = weekOrder;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public boolean isSleep() {
        return sleep;
    }

    public void setSleep(boolean sleep) {
        this.sleep = sleep;
    }

    public int getConflict() {
        return conflict;
    }

    public void setConflict(int conflict) {
        this.conflict = conflict;
    }

    public Date getGrabDate() {
        return grabDate;
    }

    public void setGrabDate(Date grabDate) {
        this.grabDate = grabDate;
    }

    public CourseBean copy() {
        CourseBean bean = new CourseBean();
        bean.setUserKey(userKey);
        bean.setName(name);
        bean.setNameAbbr(nameAbbr);
        bean.setSchoolArea(schoolArea);
        bean.setRoom(room);
        bean.setRoomAbbr(roomAbbr);
        bean.setLecturer(lecturer);
        bean.setCourseOrder(courseOrder);
        bean.setCourseNum(courseNum);
        bean.setDate(date);
        bean.setWeekOrder(weekOrder);
        bean.setTerm(term);
        bean.setSleep(sleep);
        bean.setConflict(conflict);
        return bean;
    }
}
