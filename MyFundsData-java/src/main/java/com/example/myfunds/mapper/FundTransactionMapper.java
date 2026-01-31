package com.example.myfunds.mapper;

import com.example.myfunds.entity.FundTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FundTransactionMapper {
    int insert(FundTransaction transaction);
    int update(FundTransaction transaction);
    int deleteById(Long id);
    FundTransaction selectById(Long id);
    List<FundTransaction> selectByUserId(@Param("userId") Long userId);
    List<FundTransaction> selectByFundId(@Param("fundId") Long fundId);
    List<FundTransaction> selectByUserIdAndFundId(@Param("userId") Long userId, @Param("fundId") Long fundId);
    List<FundTransaction> selectByStatus(@Param("status") String status);
}