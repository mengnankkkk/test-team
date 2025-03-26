package com.mengnnakk.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mengnnakk.configuration.property.SystemConfig;
import com.mengnnakk.entry.User;
import com.mengnnakk.entry.UserToken;
import com.mengnnakk.mapper.UserMapper;
import com.mengnnakk.mapper.UserTokenMapper;
import com.mengnnakk.service.UserService;
import com.mengnnakk.service.UserTokenService;
import com.mengnnakk.utility.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class UserTokenServiceImpl extends ServiceImpl<UserTokenMapper, UserToken> implements UserTokenService {


    private final UserTokenMapper userTokenMapper;
    private final UserService userService;
    private final SystemConfig systemConfig;

    @Autowired
    public UserTokenServiceImpl(UserTokenMapper userTokenMapper, UserService userService, SystemConfig systemConfig) {
        this.userTokenMapper = userTokenMapper;
        this.userService = userService;
        this.systemConfig = systemConfig;
    }

    @Override
    @Transactional
    public UserToken bind(User user){
        user.setModifyTime(new Date());
        userService.updateById(user);
        return insertUserToken(user);
    }

    @Override
    public UserToken checkBind(String openid){
        User user = userService.selectByWxOpenId(openid);
        if (null==user){
            return insertUserToken(user);
        }
        return null;
    }

    @Override
    public UserToken getToken(String token) {
        return userTokenMapper.getToken(token);
    }

    @Override
    public UserToken insertUserToken(User user) {
        Date startTime = new Date();
        Date endTime = DateTimeUtil.addDuration(startTime, systemConfig.getWx().getTokenToLive());
        UserToken userToken = new UserToken();
        userToken.setToken(UUID.randomUUID().toString());
        userToken.setUserId(user.getId());
        userToken.setWxOpenId(user.getWxOpenId());
        userToken.setCreateTime(startTime);
        userToken.setEndTime(endTime);
        userToken.setUserName(user.getUserName());

        return userToken;
    }

    @Override
    public void unBind(UserToken userToken) {
        User user = userService.selectById(userToken.getUserId());
        user.setModifyTime(new Date());
        user.setWxOpenId(null);
        userService.updateById(user);
        userTokenMapper.delete(userToken.getId());
    }


}
