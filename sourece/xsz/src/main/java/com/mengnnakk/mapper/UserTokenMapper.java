package com.mengnnakk.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mengnnakk.entry.UserToken;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserTokenMapper extends BaseMapper<UserToken>{


    UserToken getToken(String token);
}