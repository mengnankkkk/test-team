package com.mengnnakk.service;


import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.Message;
import com.mengnnakk.entry.MessageUser;
import com.mengnnakk.viewmodel.admin.message.MessagePageRequestVM;
import io.swagger.models.auth.In;
import sun.misc.MessageUtils;

import javax.validation.Valid;
import java.util.List;

public interface MessageService {
    List<Message> selectMessageByIds(List<Integer> ids);

    PageInfo<Message> page(MessagePageRequestVM requestVM);

    List<MessageUser> selcetByMessageIds(List<Integer> ids);

    void sendMessage(Message message,List<MessageUser> messageUsers);

    void read(Integer id);

    Integer unReadCount(Integer userId);

    Message messageDetail(Integer id);
}