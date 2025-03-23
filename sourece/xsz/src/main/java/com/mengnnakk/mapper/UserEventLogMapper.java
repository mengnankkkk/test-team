package com.mengnnakk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mengnnakk.entry.UserEventLog;
import com.mengnnakk.entry.other.KeyValue;
import com.mengnnakk.viewmodel.admin.user.UserEventPageRequestVM;
import jdk.internal.dynalink.linker.LinkerServices;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserEventLogMapper extends BaseMapper<UserEventLog>, com.baomidou.mybatisplus.mapper.BaseMapper<UserEventLog> {
    List<UserEventLog> getUserEventLogByUserId(Integer id);

    List<UserEventLog> page(UserEventPageRequestVM requestVM);

    List<KeyValue> selectCountByDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}