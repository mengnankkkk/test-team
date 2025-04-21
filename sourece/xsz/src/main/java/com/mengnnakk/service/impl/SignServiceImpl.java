package com.mengnnakk.service.impl;

import com.mengnnakk.service.SignService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Service
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
        //签到积分加1 zincrby user:sign:rank:data 1 userid
        jedis.zincrby(getRankKey(today),1,userId);

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

    /**
     * 查看签到排行搒
     * @param topN
     */

    @Override
    public void printRank(int topN) {
        String rankKey = getRankKey(LocalDate.now());
        System.out.println("📊 本月签到排行榜：");
        jedis.zrevrangeWithScores(rankKey,0,topN-1).forEach(entry->{
            System.out.println(entry.getElement() + "->" + (int) entry.getScore() + "天");
        });
    }


    private String buildSignKey(String userId,LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        return "user:sign:"+userId+":"+date.format(formatter);
    }
    private String getRankKey(LocalDate date){
        return "user:sign:rank"+date.format(DateTimeFormatter.ofPattern("yyyyMM"));
    }
}
