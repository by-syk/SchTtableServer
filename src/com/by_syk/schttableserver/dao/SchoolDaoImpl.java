package com.by_syk.schttableserver.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.by_syk.schttableserver.bean.SchoolBean;

@Repository("schoolDao")
@Transactional
public class SchoolDaoImpl implements ISchoolDao {
    @Autowired
    private SessionFactory sessionFactory;
    
    @Override
    public List<SchoolBean> getAll() {
        String hql = "FROM SchoolBean WHERE area = 0 AND enabled = true ORDER BY sort";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        
        return (List<SchoolBean>) query.list();
    }

    @Override
    public SchoolBean get(String code, int area) {
        if (code == null) {
            return null;
        }
        
        String hql = "FROM SchoolBean WHERE code = :code AND area = :area";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("code", code);
        query.setInteger("area", area);
        List list = query.list();
        if (list != null && !list.isEmpty()) {
            return (SchoolBean) list.get(0);
        }
        return null;
    }

    @Override
    public SchoolBean get(String code) {
        return get(code, 0);
    }
}
