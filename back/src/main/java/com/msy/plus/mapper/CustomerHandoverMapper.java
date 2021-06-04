package com.msy.plus.mapper;

import com.msy.plus.core.mapper.MyMapper;
import com.msy.plus.dto.CustomerHandoverList;
import com.msy.plus.entity.CustomerHandover;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CustomerHandoverMapper extends MyMapper<CustomerHandover> {
    List<CustomerHandoverList> listAndSearch(@Param("keyword") String keyword,@Param("startTime")  Date startTime,@Param("endTime")  Date endTime);
}