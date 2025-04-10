package com.mengnnakk.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mengnnakk.entry.Message;
import com.mengnnakk.viewmodel.admin.message.MessagePageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    List<Message> page(MessagePageRequestVM requestVM);

    List<Message> selectByIds(List<Integer> ids);

    int readAdd(Integer id);

    void insertSelective(Message message);

    Message selectByPrimaryKey(Integer messageId);
}