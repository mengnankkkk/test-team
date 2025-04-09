package com.mengnnakk.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.Subject;
import com.mengnnakk.mapper.SubjectMapper;
import com.mengnnakk.service.SubjectService;
import com.mengnnakk.viewmodel.admin.education.SubjectPageRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    private final SubjectMapper subjectMapper;

    @Autowired
    public SubjectServiceImpl(SubjectMapper subjectMapper) {
        this.subjectMapper = subjectMapper;
    }

    @Override
    public Subject selectById(Integer id){
        return super.selectById(id);
    }

    @Override
    public int updateByIdFilter(Subject record){
        return super.baseMapper.updateById(record);
    }


    @Override
    public List<Subject> getSubjectByLevel(Integer level) {
        return subjectMapper.getSubjectByLevel(level);
    }

    @Override
    public List<Subject> allSubject() {
        return subjectMapper.allSubject();
    }

    @Override
    public Integer levelBySubjectId(Integer id) {
        return this.selectById(id).getLevel();
    }

    @Override
    public PageInfo<Subject> page(SubjectPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(),requestVM.getPageSize(),"id desc").doSelectPageInfo(()->
                subjectMapper.page(requestVM)//doSelectPageInfo() 执行数据库查询，并封装成 PageInfo 对象。
                );//subjectMapper.page执行查询
    }
}