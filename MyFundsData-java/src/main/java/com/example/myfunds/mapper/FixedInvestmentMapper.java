package com.example.myfunds.mapper;

import com.example.myfunds.entity.FixedInvestment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface FixedInvestmentMapper {
    int insert(FixedInvestment plan);
    int update(FixedInvestment plan);
    int deleteById(Long id);
    FixedInvestment selectById(Long id);
    List<FixedInvestment> selectByUserId(@Param("userId") Long userId);
    List<FixedInvestment> selectByUserIdAndFundId(@Param("userId") Long userId, @Param("fundId") Long fundId);
    List<FixedInvestment> selectByStatusAndNextExecutionDateBefore(
            @Param("status") String status, 
            @Param("nextExecutionDate") LocalDateTime nextExecutionDate);
}