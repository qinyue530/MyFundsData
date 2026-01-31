package com.example.myfunds.mapper;

import com.example.myfunds.entity.UserFund;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserFundMapper {
    int insert(UserFund userFund);
    int update(UserFund userFund);
    int deleteById(Long id);
    Optional<UserFund> selectById(Long id);
    Optional<UserFund> selectByUserIdAndFundId(@Param("userId") Long userId, @Param("fundId") Long fundId);
    List<UserFund> selectByUserId(@Param("userId") Long userId);
    boolean existsByUserIdAndFundId(@Param("userId") Long userId, @Param("fundId") Long fundId);
}