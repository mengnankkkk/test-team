package com.mengnnakk.controller.admin;

import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.service.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;


@RestController("AdminDashboardController")
@RequestMapping(value = "/api/admin/assignment")
public class PublishController extends BaseApiController {

    @Autowired
    private PublishService publishService;

    @PostMapping("/pubish")
    public String publishAssignment (@RequestParam String assignmentId,
                                     @RequestParam @DateTimeFormat(iso =DateTimeFormat.ISO.DATE_TIME )LocalDateTime publishTime,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime daedline){
        publishService.scheduleAssignment(assignmentId,publishTime,daedline);
        return "作业已经发布";
    }

    @GetMapping("/ranking")
    public Set<String> getAccuracyRanking(){
        return publishService.getAccuracyRanking();
    }
}
