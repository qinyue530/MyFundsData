package com.example.myfunds.mapper;

import com.example.myfunds.entity.FundStock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FundStockMapper {
    int insert(FundStock fundStock);
    int update(FundStock fundStock);
    int deleteById(Long id);
    int deleteByFundId(@Param("fundId") Long fundId);
    FundStock selectById(Long id);
    List<FundStock> selectByFundId(@Param("fundId") Long fundId);
    List<FundStock> selectAll();
}