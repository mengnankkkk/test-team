package com.mengnnakk.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mengnnakk.entry.Subject;
import com.mengnnakk.viewmodel.admin.education.SubjectPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SubjectMapper extends BaseMapper<Subject> {
    List<Subject> getSubjectByLevel(Integer level);
    List<Subject> allSubject();
    List<Subject> page(SubjectPageRequestVM requestVM);
}