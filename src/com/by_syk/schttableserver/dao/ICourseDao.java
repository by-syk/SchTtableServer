package com.by_syk.schttableserver.dao;

import java.util.Date;
import java.util.List;

import com.by_syk.schttableserver.bean.CourseBean;

public interface ICourseDao {
    boolean add(CourseBean courseBean) throws Exception;
    
    boolean add(List<CourseBean> beanList) throws Exception;
    
    boolean delete(String userKey);
    
    List<CourseBean> getList(String userKey, Date dateStart, int days);
    
    CourseBean get(String userKey, Date date, int courseOrder);
    
    int getCount(String userKey, Date dateStart, int days);
    
    Date getTermStartDate(String userKey);

    Date getTermEndDate(String userKey);
}
