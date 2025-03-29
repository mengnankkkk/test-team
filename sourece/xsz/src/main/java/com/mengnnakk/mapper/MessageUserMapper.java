package com.mengnnakk.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mengnnakk.entry.MessageUser;
import com.mengnnakk.viewmodel.student.user.MessageRequestVM;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.bouncycastle.LICENSE;

import java.util.List;
import java.util.concurrent.locks.LockSupport;

@Mapper
public interface MessageUserMapper extends BaseMapper<MessageUser> {

    List<MessageUser> selectByMessageIds(List<Integer> ids);

    int inserts(List<MessageUser> list);

    List<MessageUser> studentPage(MessageRequestVM requestVM);

    Integer unReadCount(Integer userId);
}