package com.mengnnakk.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mengnnakk.entry.Question;
import com.mengnnakk.entry.other.KeyValue;
import com.mengnnakk.viewmodel.admin.question.QuestionPageRequestVM;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
    List<Question> page(QuestionPageRequestVM requestVM);
    List<Question> selectByIds(@Param("ids") List<Integer> ids);
    Integer selectAllCount();
    List<KeyValue> selectCountByDate(@Param("startTime")Date startTime,@Param("endTime")Date endTime);


}