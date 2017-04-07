package com.by_syk.schttableserver.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.by_syk.schttableserver.bean.TimetableBean;

@Repository("timetableDao")
@Transactional
public class TimetableDaoImpl implements ITimetableDao {
    @Autowired
    private SessionFactory sessionFactory;
    
    @Override
    public List<TimetableBean> get(String schoolCode) {
        if (schoolCode == null || schoolCode.length() != 5) {
            return null;
        }
        
        String hql = "FROM TimetableBean WHERE schoolCode = :schoolCode"
                + " ORDER BY courseOrder";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("schoolCode", schoolCode);
        
        return (List<TimetableBean>) query.list();
    }
}
