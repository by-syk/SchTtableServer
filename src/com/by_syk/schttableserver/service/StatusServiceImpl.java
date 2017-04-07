package com.by_syk.schttableserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.by_syk.schttableserver.bean.StatusBean;
import com.by_syk.schttableserver.dao.IStatusDao;
import com.by_syk.schttableserver.vo.StatusVo;

@Service("statusService")
public class StatusServiceImpl implements IStatusService {
    @Autowired
    @Qualifier("statusDao")
    private IStatusDao statusDao;
    
    @Override
    public boolean updateStatus(StatusBean statusBean) {
        if (statusBean == null) {
            return false;
        }
        
        return statusDao.addOrUpdate(statusBean);
    }

    @Override
    public StatusVo getStatus(String statusId) {
        StatusBean statusBean = statusDao.get(statusId);
        return new StatusVo(statusBean);
    }
}
