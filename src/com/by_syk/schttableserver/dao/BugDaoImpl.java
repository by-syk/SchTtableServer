package com.by_syk.schttableserver.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.by_syk.schttableserver.bean.BugBean;

@Repository("bugDao")
@Transactional
public class BugDaoImpl implements IBugDao {
    @Autowired
    private SessionFactory sessionFactory;
    
    @Override
    public boolean add(BugBean bean) {
        if (bean == null) {
            return false;
        }
        
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(bean);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
}
