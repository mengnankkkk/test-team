package com.mengnnakk.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mengnnakk.entry.ExamPaper;
import com.mengnnakk.entry.ExamPaperAnswer;
import com.mengnnakk.entry.other.KeyValue;
import com.mengnnakk.viewmodel.admin.paper.ExamPaperAnswerPageRequestVM;
import com.mengnnakk.viewmodel.student.exampaper.ExamPaperAnswerPageVM;
import com.sun.org.apache.bcel.internal.generic.DADD;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.awt.image.Kernel;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Mapper
public interface ExamPaperAnswerMapper extends BaseMapper<ExamPaperAnswer> {
    List<ExamPaperAnswer> studentPage(ExamPaperAnswerPageVM requestVM);

    Integer selectAllCount();

    List<KeyValue> selectCountByDate(@Param("startTime")Date startTime,@Param("endTime") Date endTime);

    ExamPaperAnswer getByPidUid(@Param("pid") Integer paperId, @Param("uid")Integer uid);

    List<ExamPaperAnswer> adminPage(ExamPaperAnswerPageRequestVM requestVM);

}