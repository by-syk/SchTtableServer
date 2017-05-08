package com.by_syk.schttableserver.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.by_syk.schttableserver.bean.AppVerBean;
import com.by_syk.schttableserver.util.StringUtil;

@Repository("appVerDao")
@Transactional
public class AppVerDaoImpl implements IAppVerDao {
    @Autowired
    private SessionFactory sessionFactory;
    
    @Override
    public List<AppVerBean> getAll(String pkgName) {
        if (StringUtil.isEmpty(pkgName)) {
            return null;
        }
        
        String hql = "FROM AppVerBean WHERE pkgName = :pkgName ORDER BY verCode DESC";
        
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("pkgName", pkgName);
        
        return (List<AppVerBean>) query.list();
    }
}
