package com.example.myfunds.mapper;

import com.example.myfunds.entity.Fund;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FundMapper {
    int insert(Fund fund);
    int update(Fund fund);
    int deleteById(Long id);
    Optional<Fund> selectById(Long id);
    Optional<Fund> selectByFundCode(@Param("fundCode") String fundCode);
    List<Fund> selectAll();
    boolean existsByFundCode(@Param("fundCode") String fundCode);
}