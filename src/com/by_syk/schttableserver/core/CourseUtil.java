package com.by_syk.schttableserver.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.by_syk.schttableserver.bean.CourseBean;
import com.by_syk.schttableserver.util.DateUtil;
import com.by_syk.schttableserver.util.ExtraUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CourseUtil {
    /**
     * 去重补缺：
     * 去除导致重课的数据
     * 补上缺失的课程数据并按无课处理
     * 
     * 如何确定 CourseBean.courseOrder、courseNum ？
     * 连堂课合并，但不能跨天。如高数课跨1~3节，则 courseOrder = 1，courseNum = 3。
     * 
     * @param dataList 不能为null，无数据也不处理
     * @param weekOrder1StartDate
     * @param weekNum
     * @param dayCourseNum
     * @return
     */
    public static boolean fixCourseList(List<CourseBean> dataList, Date weekOrder1StartDate, int weekNum, int dayCourseNum) {
        if (dataList == null || dataList.isEmpty()) {
            return false;
        }
        
        // 按时间线排序
//        Collections.sort(dataList, new Comparator<CourseBean>() {
//            @Override
//            public int compare(CourseBean c1, CourseBean c2) {
//                // JDK7 中的 Collections.sort() 方法实现中，如果两个值是相等的，那么 compare() 方法需要返回0，
//                // 否则可能会在排序时抛错，而JDK6是没有这个限制的。
//                if (c1.getDate().getTime() == c2.getDate().getTime()
//                        && c1.getCourseOrder() == c2.getCourseOrder()
//                        && c1.getCourseNum() == c2.getCourseNum()) {
//                    return 0;
//                }
//                return (c1.getDate().getTime() <= c2.getDate().getTime()
//                        && c1.getCourseOrder() <= c2.getCourseOrder()
//                        && c1.getCourseNum() >= c2.getCourseNum()) ? -1 : 1;
//            }
//        });
        timelineSort(dataList);
        
        // 去重
        
        List<CourseBean> conflictList = new ArrayList<>();
        int courseEndOrder = dataList.get(0).getCourseOrder() + dataList.get(0).getCourseNum() - 1;
        for (int i = 1; i < dataList.size(); ++i) {
            CourseBean curBean = dataList.get(i);
            if (curBean.getDate().getTime() != dataList.get(i - 1).getDate().getTime()) {
                courseEndOrder = curBean.getCourseOrder() + curBean.getCourseNum() - 1;
                continue;
            }
            int curCourseEndOrder = curBean.getCourseOrder() + curBean.getCourseNum() - 1;
            if (curBean.getCourseOrder() <= courseEndOrder) { // 冲突产生
                if (curCourseEndOrder > courseEndOrder) {
                    CourseBean conflictBean = curBean.copy();
                    conflictBean.setCourseNum(courseEndOrder - curBean.getCourseOrder() + 1);
                    conflictBean.setConflict((int) (1000 * Math.random()));
                    conflictList.add(conflictBean);
                    curBean.setCourseOrder(courseEndOrder + 1);
                    curBean.setCourseNum(curCourseEndOrder - courseEndOrder);
                    courseEndOrder = curCourseEndOrder;
                } else {
                    dataList.remove(i);
                    curBean.setConflict((int) (1000 * Math.random()));
                    conflictList.add(curBean);
                    --i;
                }
            } else {
                courseEndOrder = curCourseEndOrder;
            }
        }
        
        // TODO DEBUG
        System.out.println("Conflict:");
        try {
            for (CourseBean bean : conflictList) {
                System.out.println((new ObjectMapper()).writeValueAsString(bean));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        
        // 补缺
        
        CourseBean templateBean = getTemplateBean(dataList);
        
        List<CourseBean> addList = new ArrayList<>();
        List<Long> dayList = parseDays(weekOrder1StartDate, weekNum * 7); // 整天无课的日子
        List<Integer> courseArr = ExtraUtil.getIntArr(1, dayCourseNum);
        CourseBean curBean = dataList.get(0);
        for (int i = 0, len = dataList.size(); i <= len; ++i) {
            CourseBean courseBean = null;
            if (i < len) {
                courseBean = dataList.get(i);
            }
            if (i == len || courseBean.getDate().getTime() != curBean.getDate().getTime()) {
                for (Entry<Integer, Integer> entry : parseCourses(courseArr).entrySet()) {
                    CourseBean bean = templateBean.copy();
                    bean.setCourseOrder(entry.getKey());
                    bean.setCourseNum(entry.getValue());
                    bean.setDate(curBean.getDate());
                    bean.setWeekOrder(curBean.getWeekOrder());
                    addList.add(bean);
                }
                dayList.remove((Long) curBean.getDate().getTime());
                courseArr = ExtraUtil.getIntArr(1, dayCourseNum);
                curBean = courseBean;
            }
            if (i < len) {
                for (int j = courseBean.getCourseOrder(); j < courseBean.getCourseOrder() + courseBean.getCourseNum(); ++j) {
                    courseArr.remove((Integer) j);
                }
            }
        }
        
        for (Long time : dayList) {
            CourseBean bean = templateBean.copy();
            bean.setCourseOrder(1);
            bean.setCourseNum(dayCourseNum);
            bean.setDate(new Date(time));
            bean.setWeekOrder(DateUtil.getDays(weekOrder1StartDate.getTime(), time) / 7 + 1);
            addList.add(bean);
        }
        
        dataList.addAll(conflictList);
        dataList.addAll(addList);
        
        return true;
    }
    
    private static CourseBean getTemplateBean(List<CourseBean> dataList) {
        CourseBean courseBean = new CourseBean();
        if (dataList == null || dataList.isEmpty()) {
            return courseBean;
        }
        
        CourseBean bean1 = dataList.get(0);
        courseBean.setUserKey(bean1.getUserKey());
        courseBean.setSchoolArea(bean1.getSchoolArea());
        courseBean.setTerm(bean1.getTerm());
        courseBean.setSleep(true);
        
        return courseBean;
    }
    
    private static List<Long> parseDays(Date startDate, int days) {
        List<Long> list = new ArrayList<>();
        if (startDate == null) {
            return list;
        }
        
        for (int i = 0; i < days; ++i) {
            list.add(DateUtil.addDate(startDate, i).getTime());
        }
        return list;
    }
    
    /**
     * 1,2,5,6,7,8 -> {1:2},{5:4}
     */
    private static Map<Integer, Integer> parseCourses(List<Integer> courses) {
        Map<Integer, Integer> map = new HashMap<>();
        if (courses.isEmpty()) {
            return map;
        }
        
        Collections.sort(courses);
        
        Integer start = courses.get(0);
        map.put(start, 1);
        for (int i = 1, len = courses.size(); i < len; ++i) {
            if (courses.get(i) == courses.get(i - 1) + 1) {
                map.put(start, map.get(start) + 1);
            } else {
                start = courses.get(i);
                map.put(start, 1);
            }
        }
        
        return map;
    }
    
    /**
     * 按时间线排序
     * 
     * @param dataList
     */
    private static void timelineSort(List<CourseBean> dataList) {
        if (dataList == null) {
            return;
        }
        
        long start = System.currentTimeMillis();
        
        for (int i = dataList.size() - 2; i >= 0; --i) {
            for (int j = 0; j <= i; ++j) {
                CourseBean bean1 = dataList.get(j);
                CourseBean bean2 = dataList.get(j + 1);
                if (bean1.getDate().getTime() < bean2.getDate().getTime()) {
                    continue;
                } else if (bean1.getDate().getTime() == bean2.getDate().getTime()) {
                    if (bean1.getCourseOrder() < bean2.getCourseOrder()) {
                        continue;
                    } else if (bean1.getCourseOrder() == bean2.getCourseOrder()) {
                        if (bean1.getCourseNum() >= bean2.getCourseNum()) {
                            continue;
                        }
                    }
                }
                dataList.set(j, bean2);
                dataList.set(j + 1, bean1);
            }
        }
        
        System.out.println("Cost(timelineSort): " + (System.currentTimeMillis() - start) + "ms");
    }
}
