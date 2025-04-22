package com.mengnnakk.controller;

import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.service.IpStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ip")
public class IpController extends BaseApiController {
    private final IpStatsService ipStatsService;

    @Autowired
    public IpController(IpStatsService ipStatsService) {
        this.ipStatsService = ipStatsService;
    }
    @PostMapping("/track")
    public String track(HttpServletRequest request) {
        String ip = getClientIp(request);
        ipStatsService.recordIp(ip);
        return "✅ IP 记录成功：" + ip;
    }
    // 查询属地
    @GetMapping("/region")
    public Mono<String> region(@RequestParam String ip) {
        return ipStatsService.getIpRegion(ip);
    }
    // 查询 UV
    @GetMapping("/uv")
    public long uv() {
        return ipStatsService.getTodayUv();
    }

    /**
     * 获取真实ip
     * @param request
     * @return
     */
    private String getClientIp(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        return xf == null ? request.getRemoteAddr() : xf.split(",")[0];
    }
}
