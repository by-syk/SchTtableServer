package com.by_syk.schttableserver.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.by_syk.schttableserver.bean.StatusBean;

@Repository("statusDao")
@Transactional
public class StatusDaoImpl implements IStatusDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean addOrUpdate(StatusBean statusBean) {
        if (statusBean == null) {
            return false;
        }
        
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(statusBean);
        return true;
    }

    @Override
    public StatusBean get(String id) {
        if (id == null) {
            return null;
        }
        
        String hql = "FROM StatusBean WHERE id = :id";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("id", id);
        List list = query.list();
        if (list != null && !list.isEmpty()) {
            return (StatusBean) list.get(0);
        }
        return null;
    }
}
