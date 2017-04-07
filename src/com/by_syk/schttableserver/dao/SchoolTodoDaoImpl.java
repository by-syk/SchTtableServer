package com.by_syk.schttableserver.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.by_syk.schttableserver.bean.SchoolTodoBean;

@Repository("schoolTodoDao")
@Transactional
public class SchoolTodoDaoImpl implements ISchoolTodoDao {
    @Autowired
    private SessionFactory sessionFactory;
    
    @Override
    public List<SchoolTodoBean> getAll() {
        String hql = "FROM SchoolTodoBean ORDER BY time DESC";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        
        return (List<SchoolTodoBean>) query.list();
    }
}
