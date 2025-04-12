package com.mengnnakk.mapper;

import com.mengnnakk.entry.TaskExamCustomerAnswer;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface TaskExamCustomerAnswerMapper extends BaseMapper<TaskExamCustomerAnswer>, com.baomidou.mybatisplus.mapper.BaseMapper<TaskExamCustomerAnswer> {
    TaskExamCustomerAnswer getByUid(@Param("tid") Integer tid,@Param("uid") Integer uid);

    List<TaskExamCustomerAnswer> selectByUid(@Param("taskIds")List<Integer> taskIds,@Param("uid") Integer uid);
}