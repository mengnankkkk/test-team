package com.mengnnakk.controller;

import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.service.SignService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/sign")
public class SignController extends BaseApiController {
    private final SignService signService;


    public SignController(SignService signService) {
        this.signService = signService;
    }
    @PostMapping("{userid}")
    public String sign(@PathVariable String userid){
        signService.sign(userid);
        return "✅ 签到成功";
    }
    @GetMapping("/{userId}/status")
    public boolean isSign(@PathVariable String userId){
        return signService.isSigned(userId, LocalDate.now());
    }
    @GetMapping("{userId}/count")
    public long total(@PathVariable String userId){
        return signService.getSignCount(userId,LocalDate.now());
    }

    @GetMapping("/rank")
    public void rank() {
        signService.printRank(10); // top 10
    }
}
