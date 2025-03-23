package com.mengnnakk.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.UserEventLog;
import com.mengnnakk.viewmodel.admin.user.UserEventPageRequestVM;
import io.swagger.models.auth.In;
import jdk.internal.dynalink.linker.LinkerServices;

import java.util.List;

public interface UserEventLogService extends IService<UserEventLog> {
    List<UserEventLog> getUserEventLogByUserId(Integer id);
    PageInfo<UserEventLog> page(UserEventPageRequestVM requestVM);
    List<Integer> selectMothCount();
}