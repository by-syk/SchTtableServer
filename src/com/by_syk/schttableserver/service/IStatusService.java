package com.by_syk.schttableserver.service;

import com.by_syk.schttableserver.bean.StatusBean;
import com.by_syk.schttableserver.vo.StatusVo;

public interface IStatusService {
    boolean updateStatus(StatusBean statusBean);
    
    StatusVo getStatus(String statusId);
}
