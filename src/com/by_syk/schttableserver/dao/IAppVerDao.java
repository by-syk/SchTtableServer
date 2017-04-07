package com.by_syk.schttableserver.dao;

import java.util.List;

import com.by_syk.schttableserver.bean.AppVerBean;

public interface IAppVerDao {
    List<AppVerBean> getAll(String pkgName);
}
