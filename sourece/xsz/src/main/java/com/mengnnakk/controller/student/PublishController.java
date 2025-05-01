package com.mengnnakk.controller.student;

import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.service.PublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@Slf4j
@RestController("StudentDashboardController")
@RequestMapping(value = "/api/student/assignment")
public class PublishController extends BaseApiController {
    @Autowired
    private PublishService publishService;

    @PostMapping("/submit")
    public String submitAssignment(@RequestParam String sutdentId, @RequestParam String assignmentId, @RequestParam String content){
        publishService.submitAssignment(sutdentId,assignmentId,content);
        return "作业提交成功";
    }

    @GetMapping("/wrongbook/{studentId}")
    public Set<String> getWrongBook(@PathVariable String studentId){
        return publishService.getWrongBook(studentId);
    }
}
