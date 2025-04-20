package com.mengnnakk.service.impl;

import com.mengnnakk.service.SignService;
import org.joda.time.DateTime;
import redis.clients.jedis.Jedis;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SignServiceImpl implements SignService {
    private final Jedis jedis;

    public SignServiceImpl(){
        this.jedis = new Jedis("localhost", 6379);
    }

    /**
     * 签到
     * @param userId
     */
    @Override
    public void sign(String userId) {
        LocalDate today = LocalDate.now();
        String key = buildSignKey(userId,today);
        int dayIndex = today.getDayOfMonth()-1;
        jedis.setbit(key,dayIndex,true);//SETBIT userid:sign:date index 1
        System.out.println("✅ 签到成功：" + userId + " 第 " + (dayIndex + 1) + " 天");


    }

    /**
     * 是否签到
     * @param userId
     * @param date
     * @return
     */

    @Override
    public boolean isSigned(String userId, LocalDate date) {
        String key = buildSignKey(userId,date);
        int dayIndex =date.getDayOfMonth()-1;
        return jedis.getbit(key,dayIndex);//getbit userid:sign:date index
    }

    /**
     * 查看签到天数
     * @param userId
     * @param date
     * @return
     */

    @Override
    public long getSignCount(String userId, LocalDate date) {
        String key = buildSignKey(userId,date);
        return jedis.bitcount(key);
    }
    private String buildSignKey(String userId,LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        return "user:sign:"+userId+":"+date.format(formatter);
    }
}
