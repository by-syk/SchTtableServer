package com.by_syk.schttableserver.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.by_syk.schttableserver.bean.KeyBean;

@Repository("keyDao")
@Transactional
public class KeyDaoImpl implements IKeyDao {
    @Autowired
    private SessionFactory sessionFactory;
    
    @Override
    public boolean add(KeyBean keyBean) {
        if (keyBean == null) {
            return false;
        }
        
        if (contains(keyBean.getSchoolCode(), keyBean.getStuNo(), keyBean.getPassword())) {
            return false;
        }
        
        Session session = sessionFactory.getCurrentSession();
        session.save(keyBean);
        return true;
    }

    @Override
    public boolean update(KeyBean keyBean) {
        if (keyBean == null) {
            return false;
        }
        
        Session session = sessionFactory.getCurrentSession();
        session.update(keyBean);
        return true;
    }

    @Override
    public boolean addOrUpdate(KeyBean keyBean) {
        if (keyBean == null) {
            return false;
        }
        
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(keyBean);
        return true;
    }

    @Override
    public boolean contains(String userKey) {
        if (userKey == null) {
            return false;
        }
        
        String hql = "SELECT COUNT(*) FROM KeyBean WHERE userKey = :userKey";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("userKey", userKey);
        
        return ((Long) query.uniqueResult()) > 0L;
    }

    @Override
    public boolean contains(String schoolCode, String stuNo, String password) {
        if (schoolCode == null || stuNo == null) {
            return false;
        }
        
        String hql = "SELECT COUNT(*) FROM KeyBean WHERE schoolCode = :schoolCode"
                + " AND stuNo = :stuNo AND password = :password";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("schoolCode", schoolCode);
        query.setString("stuNo", stuNo);
        query.setString("password", password);
        
        return ((Long) query.uniqueResult()) > 0L;
    }
    
    @Override
    public KeyBean get(String userKey) {
        if (userKey == null) {
            return null;
        }
        
        String hql = "FROM KeyBean WHERE userKey = :userKey";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("userKey", userKey);
        List list = query.list();
        if (list != null && !list.isEmpty()) {
            return (KeyBean) list.get(0);
        }
        return null;
    }

    @Override
    public KeyBean get(String schoolCode, String stuNo, String password) {
        if (schoolCode == null || stuNo == null || password == null) {
            return null;
        }
        
        String hql = "FROM KeyBean WHERE schoolCode = :schoolCode"
                + " AND stuNo = :stuNo AND password = :password";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("schoolCode", schoolCode);
        query.setString("stuNo", stuNo);
        query.setString("password", password);
        List list = query.list();
        if (list != null && !list.isEmpty()) {
            return (KeyBean) list.get(0);
        }
        return null;
    }

    @Override
    public boolean delete(KeyBean keyBean) {
        if (keyBean == null) {
            return false;
        }
        
        Session session = sessionFactory.getCurrentSession();
        session.delete(keyBean);
        return true;
    }

    @Override
    public boolean delete(String schoolCode, String stuNo) {
        if (schoolCode == null || stuNo == null) {
            return false;
        }
        
        String hql = "DELETE FROM KeyBean WHERE schoolCode = :schoolCode"
                + " AND stuNo = :stuNo";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("schoolCode", schoolCode);
        query.setString("stuNo", stuNo);
        
        return query.executeUpdate() > 1;
    }

    @Override
    public Map<String, Long> countSchoolUser() {
//        String hql = "SELECT s.name, COUNT(*) FROM KeyBean AS k"
//                + " LEFT JOIN SchoolBean AS s WITH k.schoolCode = s.code"
//                + " WHERE k.userName IS NOT NULL AND s.area = 0"
//                + " GROUP BY k.schoolCode"
//                + " ORDER BY col_1_0_ DESC"; // col_1_0_ 即 COUNT(*)
        String hql = "SELECT schoolCode, COUNT(*) FROM KeyBean"
                + " WHERE userName IS NOT NULL"
                + " GROUP BY schoolCode"
                + " ORDER BY col_1_0_ DESC"; // col_1_0_ 即 COUNT(*)
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        
        Map<String, Long> map = new HashMap<>();
        List list = query.list();
        if (list == null || list.size() == 0) {
            return map;
        }
        
        for (int i = 0, len = list.size(); i < len; ++i) {
            Object[] tempObj = (Object[]) list.get(i);
            map.put((String) tempObj[0], (Long) tempObj[1]);
        }
        return map;
    }
}
