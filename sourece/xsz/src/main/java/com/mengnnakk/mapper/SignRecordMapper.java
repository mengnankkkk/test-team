package com.mengnnakk.mapper;

import com.mengnnakk.entry.sign.SignRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;
@Mapper
public interface SignRecordMapper extends BaseMapper<SignRecord> {
    @Select("SELECT student_id FROM sign_record WHERE task_id = #{taskId}")
    Set<String> findSignedStudentIds(@Param("taskId")String taskId);
}
