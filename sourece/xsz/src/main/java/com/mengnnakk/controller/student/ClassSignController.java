package com.mengnnakk.controller.student;

import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.service.ClassSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/sign")
@RestController("StudentDashboardController")
public class ClassSignController extends BaseApiController {
    @Autowired
    private ClassSignService classSignService;
    @PostMapping("/do")
    public boolean sign(@RequestParam String taskId, @RequestParam String studentId, @RequestParam String code) {
        return classSignService.sign(taskId, studentId, code);
    }
}
