package com.by_syk.schttableserver.dao;

import com.by_syk.schttableserver.bean.StatusBean;

public interface IStatusDao {
    boolean addOrUpdate(StatusBean statusBean);
    
    StatusBean get(String id);
}
