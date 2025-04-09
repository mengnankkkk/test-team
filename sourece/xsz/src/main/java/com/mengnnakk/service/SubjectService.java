package com.mengnnakk.service;


import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.Subject;
import com.mengnnakk.viewmodel.admin.education.SubjectPageRequestVM;
import io.swagger.models.auth.In;
import org.bouncycastle.LICENSE;

import java.util.List;

public interface SubjectService extends IService<Subject> {
    Subject selectById(Integer id);

    int updateByIdFilter(Subject record);

    List<Subject> getSubjectByLevel(Integer level);
    List<Subject> allSubject();
    Integer levelBySubjectId(Integer id);
    PageInfo<Subject> page(SubjectPageRequestVM requestVM);
}