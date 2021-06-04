package com.msy.plus.mapper;

import com.msy.plus.core.mapper.MyMapper;
import com.msy.plus.entity.CFUHSearch;
import com.msy.plus.entity.CustomerFollowUpHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerFollowUpHistoryMapper extends MyMapper<CustomerFollowUpHistory> {
    List<CFUHSearch> listAndSearch(@Param("keyword") String keyword,@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("type") Integer type);
}