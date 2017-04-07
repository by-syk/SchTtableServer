package com.by_syk.schttableserver.vo;

import java.io.Serializable;
import java.util.Calendar;

import com.by_syk.schttableserver.bean.CourseBean;
import com.by_syk.schttableserver.bean.SchoolBean;
import com.by_syk.schttableserver.bean.TimetableBean;

public class CourseVo implements Serializable {
    private static final long serialVersionUID = 1L;

    // 课程名
    private String name;
    
    // 教室名
    private String room;
    
    // 教室名缩写
    private String roomAbbr;

    // 讲师名
    private String lecturer;
    
    // 节次（1~
    private int courseOrder;
    
    // 时段
    private int interval = INTERVAL_DAY;
    
    // 时段内节次（1~
    private int intervalCourseOrder;
    
    // 上课时间
    private long timeStart;

    // 下课时间
    private long timeEnd;

    // 日期（精确到日）
    private long date;
    
    // 周次
    private int weekOrder;
    
    // 无课
    private boolean sleep;
    
    // 该时间段有重课未显示
    private boolean merge;
    
    public static final int INTERVAL_DAY = 0; // 全天
    public static final int INTERVAL_MORNING = 1; // 上午
    public static final int INTERVAL_AFTERNOON = 2; // 下午
    public static final int INTERVAL_EVENING = 3; // 晚上
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getIntervalCourseOrder() {
        return intervalCourseOrder;
    }

    public void setIntervalCourseOrder(int intervalCourseOrder) {
        this.intervalCourseOrder = intervalCourseOrder;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }
    
    public void setTimeStart(long date, int timeStart) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.set(Calendar.HOUR_OF_DAY, timeStart / 100);
        calendar.set(Calendar.MINUTE, timeStart % 100);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        this.timeStart = calendar.getTimeInMillis();
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setTimeEnd(long date, int timeEnd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.set(Calendar.HOUR_OF_DAY, timeEnd / 100);
        calendar.set(Calendar.MINUTE, timeEnd % 100);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        this.timeEnd = calendar.getTimeInMillis();
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getWeekOrder() {
        return weekOrder;
    }

    public void setWeekOrder(int weekOrder) {
        this.weekOrder = weekOrder;
    }

    public boolean isSleep() {
        return sleep;
    }

    public void setSleep(boolean sleep) {
        this.sleep = sleep;
    }

    public boolean isMerge() {
        return merge;
    }

    public void setMerge(boolean merge) {
        this.merge = merge;
    }

    public void parse(CourseBean courseBean, TimetableBean timetableBean, SchoolBean schoolBean, int courseOrderIndex) {
        int courseOrder = 0;
        if (courseBean != null) {
            if (courseBean.getName() != null) {
                setName(courseBean.getName());
            } else {
                setName(courseBean.getNameAbbr());
            }
            if (courseBean.getRoom() != null) {
                setRoom(courseBean.getRoom());
                if (courseBean.getRoomAbbr() == null) {
                    courseBean.setRoomAbbr(courseBean.getRoom());
                }
            } else if (courseBean.getRoomAbbr() != null) {
                setRoomAbbr(courseBean.getRoomAbbr());
                setRoom(courseBean.getRoomAbbr());
            }
            setRoomAbbr(courseBean.getRoomAbbr());
            setLecturer(courseBean.getLecturer());
            courseOrder = courseBean.getCourseOrder() + courseOrderIndex;
            setCourseOrder(courseOrder);
            setDate(courseBean.getDate().getTime());
            setWeekOrder(courseBean.getWeekOrder());
            setSleep(courseBean.isSleep());
            if (timetableBean != null) {
                setTimeStart(courseBean.getDate().getTime(), timetableBean.getTimeStart());
                setTimeEnd(courseBean.getDate().getTime(), timetableBean.getTimeEnd());
            }
        }
        if (schoolBean != null) {
            if (courseOrder > schoolBean.getDayCourseNum1() + schoolBean.getDayCourseNum2()) {
                setInterval(INTERVAL_EVENING);
                setIntervalCourseOrder(courseOrder - schoolBean.getDayCourseNum1()
                        - schoolBean.getDayCourseNum2());
            } else if (courseOrder > schoolBean.getDayCourseNum1()) {
                setInterval(INTERVAL_AFTERNOON);
                setIntervalCourseOrder(courseOrder - schoolBean.getDayCourseNum1());
            } else if (courseOrder > 0) {
                setInterval(INTERVAL_MORNING);
                setIntervalCourseOrder(courseOrder);
            }
        }
    }
}
