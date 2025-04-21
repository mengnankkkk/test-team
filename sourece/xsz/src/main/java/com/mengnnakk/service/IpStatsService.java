package com.mengnnakk.service;

import reactor.core.publisher.Mono;

public interface IpStatsService {
    public void recordIp(String ip);

    public long getTodayUv();

    public Mono<String> getIpRegion(String ip);
}
