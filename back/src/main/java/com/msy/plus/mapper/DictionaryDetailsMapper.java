package com.msy.plus.mapper;

import com.msy.plus.core.mapper.MyMapper;
import com.msy.plus.entity.DictionaryContents;
import com.msy.plus.entity.DictionaryDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictionaryDetailsMapper extends MyMapper<DictionaryDetails> {
    List<DictionaryContents> listWithKeyword(@Param("id") int id,@Param("keyword")  String keyword);

}