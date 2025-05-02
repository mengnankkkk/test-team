package com.mengnnakk.controller.admin;


import com.mengnnakk.base.BaseApiController;

import com.mengnnakk.service.ClassSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

@RestController("AdminDashboardController")
@RequestMapping("api/sign")
public class ClassSignController extends BaseApiController {

    @Autowired
    private ClassSignService classSignService;

    @PostMapping("/create")
    public boolean create(@RequestParam LocalDateTime start,@RequestParam LocalDateTime end){
        return classSignService.createTask(start,end);
    }
    @PostMapping("/close?{taskId}")
    public void  close(@PathVariable String taskId){
        classSignService.closeTask(taskId);
    }

    @GetMapping("/records/{taskId}")
    public Set<String> records(@PathVariable String taskId) {
        return classSignService.getRecords(taskId);
    }

}
