package com.by_syk.schttableserver.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.by_syk.schttableserver.bean.CourseBean;
import com.by_syk.schttableserver.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository("courseDao")
@Transactional
public class CourseDaoImpl implements ICourseDao {
    @Autowired
    private SessionFactory sessionFactory;
    
    @Override
    public boolean add(CourseBean courseBean) throws Exception {
        if (courseBean == null) {
            return false;
        }
        
        Session session = sessionFactory.getCurrentSession();
        session.save(courseBean);
        return true;
    }
    
    @Override
    public boolean add(List<CourseBean> beanList) throws Exception {
        if (beanList == null) {
            return false;
        }
        
        Session session = sessionFactory.getCurrentSession();
        // 异常抛出在方法上，不允许任何一条数据插入出错
        for (CourseBean bean : beanList) {
//            System.out.println((new ObjectMapper()).writeValueAsString(bean));
            session.save(bean);
        }
        return true;
    }
    
    @Override
    public boolean delete(String userKey) {
        if (userKey == null) {
            return false;
        }
        
        String hql = "DELETE CourseBean WHERE userKey = :userKey";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("userKey", userKey);
        query.executeUpdate();
        
        return true;
    }

    @Override
    public List<CourseBean> getList(String userKey, Date dateStart, int days) {
        if (userKey == null || dateStart == null || days <= 0) {
            return null;
        }
        
        String hql = "FROM CourseBean WHERE userKey = :userKey"
                + " AND date >= :dateStart AND date < :dateEnd"
                + " ORDER BY date, courseOrder";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("userKey", userKey);
        query.setDate("dateStart", dateStart);
        query.setDate("dateEnd", DateUtil.addDate(dateStart, days));
        
        return (List<CourseBean>) query.list();
    }

    @Override
    public CourseBean get(String userKey, Date date, int courseOrder) {
        if (userKey == null || date == null || courseOrder <= 0) {
            return null;
        }
        
        String hql = "FROM CourseBean WHERE userKey = :userKey"
                + " AND date = :date AND courseOrder = :courseOrder";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("userKey", userKey);
        query.setDate("date", date);
        query.setInteger("courseOrder", courseOrder);
        List list = query.list();
        if (list != null && !list.isEmpty()) {
            return (CourseBean) list.get(0);
        }
        return null;
    }

    @Override
    public int getCount(String userKey, Date dateStart, int days) {
        if (userKey == null || dateStart == null || days <= 0) {
            return -1;
        }
        
        String hql = "SELECT SUM(courseNum) FROM CourseBean WHERE userKey = :userKey"
                + " AND date >= :dateStart AND date < :dateEnd";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("userKey", userKey);
        query.setDate("dateStart", dateStart);
        query.setDate("dateEnd", DateUtil.addDate(dateStart, days));
        
        Long sum = (Long) query.uniqueResult();
        return sum != null ? sum.intValue() : 0;
    }
    
    @Override
    public Date getTermStartDate(String userKey) {
        if (userKey == null) {
            return null;
        }
        
        String hql = "SELECT MIN(date) FROM CourseBean WHERE userKey = :userKey";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("userKey", userKey);
        return (Date) query.uniqueResult();
    }

    @Override
    public Date getTermEndDate(String userKey) {
        if (userKey == null) {
            return null;
        }
        
        String hql = "SELECT MAX(date) FROM CourseBean WHERE userKey = :userKey";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("userKey", userKey);
        return (Date) query.uniqueResult();
    }
}
