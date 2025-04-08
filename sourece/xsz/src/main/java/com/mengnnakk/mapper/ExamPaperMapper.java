package com.mengnnakk.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mengnnakk.entry.ExamPaper;
import com.mengnnakk.entry.other.KeyValue;
import com.mengnnakk.viewmodel.admin.exam.ExamPaperPageRequestVM;
import com.mengnnakk.viewmodel.student.dashboard.PaperFilter;
import com.mengnnakk.viewmodel.student.dashboard.PaperInfo;
import com.mengnnakk.viewmodel.student.exam.ExamPaperPageVM;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ExamPaperMapper extends BaseMapper<ExamPaper> {

    List<ExamPaper> page(ExamPaperPageRequestVM requestVM);
    List<ExamPaper> taskExamPage(ExamPaperPageRequestVM requestVM);
    List<ExamPaper> studentPage(ExamPaperPageVM request);
    List<PaperInfo> indexPaper(PaperFilter paperFilter);
    Integer selectAllCount();
    List<KeyValue> selectCountByDate(@Param("startTime")Date startTime,@Param("endTime") Date endTime);
    int updateTaskPaper(@Param("taskId") Integer taskId,@Param("paperIds") List<Integer> paperIds);
    int clearTaskPaper(@Param("paperIds")List<Integer> papers);
}