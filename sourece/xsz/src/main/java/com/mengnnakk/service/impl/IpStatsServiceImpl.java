package com.mengnnakk.service.impl;

import com.mengnnakk.service.IpStatsService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import redis.clients.jedis.Jedis;

import java.time.LocalDate;
import java.util.Map;

@Service
public class IpStatsServiceImpl implements IpStatsService {

    private final Jedis jedis = new Jedis("localhost", 6379);
    private final WebClient webClient = WebClient.create();//（公网 API

    /**
     * 记录ip
     * @param ip
     */
    @Override
    public void recordIp(String ip) {
        String key = "active:ip"+ LocalDate.now();
        jedis.pfadd(key,ip);

    }

    /**
     * 记录UV数
     * @return
     */
    @Override
    public long getTodayUv() {
        String key = "active:ip"+LocalDate.now();
        return jedis.pfcount(key);
    }

    /**
     * 查询ip属地,使用公网API
     *
     * @param ip
     * @return
     */
    @Override
    public Mono<String> getIpRegion(String ip) {
        return webClient.get()
                .uri("http://ip-api.com/json/" + ip)
                .retrieve()
                .bodyToMono(Map.class)
                .map(map -> {
                    String country = (String) map.get("country");
                    String region = (String) map.get("regionName");
                    String city = (String) map.get("city");
                    String isp = (String) map.get("isp");
                    return String.format("%s %s %s (%s)", country, region, city, isp);
                });
    }
}
