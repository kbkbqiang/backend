package com.backend.dao.mapper;

import com.backend.dao.model.InvestigationUrgeInfo;

public interface InvestigationUrgeInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InvestigationUrgeInfo record);

    int insertSelective(InvestigationUrgeInfo record);

    InvestigationUrgeInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InvestigationUrgeInfo record);

    int updateByPrimaryKey(InvestigationUrgeInfo record);
}