package com.msy.plus.mapper;

import com.msy.plus.core.mapper.MyMapper;
import com.msy.plus.dto.AnalysisQuery;
import com.msy.plus.entity.Analysis;
import com.msy.plus.entity.CustomerManager;
import com.msy.plus.dto.CustomerManagerList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerManagerMapper extends MyMapper<CustomerManager> {
    List<CustomerManagerList> listAllWithDictionary(@Param("keyword") String keyword,@Param("status")  Integer status);
    CustomerManager getDetailById(@Param("id") long id);
    List<Analysis> queryAnalysis(@Param("map") AnalysisQuery analysisQuery);
}