package com.by_syk.schttableserver.service;

import java.util.List;

import com.by_syk.schttableserver.vo.SchoolTodoVo;
import com.by_syk.schttableserver.vo.SchoolVo;

public interface ISchoolService {
    List<SchoolVo> getAllSupportedSchools(boolean withUserNum);

    List<SchoolTodoVo> getAllTodoSchools();
}
