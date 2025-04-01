package com.mengnnakk.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.Message;
import com.mengnnakk.entry.MessageUser;
import com.mengnnakk.mapper.MessageMapper;
import com.mengnnakk.mapper.MessageUserMapper;
import com.mengnnakk.service.MessageService;
import com.mengnnakk.viewmodel.admin.message.MessagePageRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;
    private final MessageUserMapper messageUserMapper;

    @Autowired
    public MessageServiceImpl(MessageMapper messageMapper, MessageUserMapper messageUserMapper) {
        this.messageMapper = messageMapper;
        this.messageUserMapper = messageUserMapper;
    }


    @Override
    public List<Message> selectMessageByIds(List<Integer> ids) {
        return messageMapper.selectByIds(ids);
    }

    @Override
    public PageInfo<Message> page(MessagePageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(),requestVM.getPageSize(),"id desc").doSelectPageInfo(()->
        messageMapper.page(requestVM)
                );
    }

    @Override
    public List<MessageUser> selcetByMessageIds(List<Integer> ids) {
        return messageUserMapper.selectByMessageIds(ids);
    }

    @Override
    @Transactional
    public void sendMessage(Message message, List<MessageUser> messageUsers) {
        messageMapper.insertSelective(message);
        messageUsers.forEach(d -> d.setMessageId(message.getId()));
        messageUserMapper.inserts(messageUsers);
    }

    /**
     * 此方法暂时实现不了
     * @param id
     */

    @Override
    @Transactional
    public void read(Integer id) {
        MessageUser messageUser = (MessageUser) messageUserMapper.selectByMessageIds(Collections.singletonList(id));
        if (messageUser.getReaded())
            return;
        messageUser.setReaded(true);
        messageUser.setReadTime(new Date());
        messageUserMapper.updateById(messageUser);


    }

    @Override
    public Integer unReadCount(Integer userId) {
        return messageUserMapper.unReadCount(userId);
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Message messageDetail(Integer id) {
        MessageUser messageUser = messageUserMapper.selectByPrimaryKey(id);
        return messageMapper.selectByPrimaryKey(messageUser.getMessageId());
    }
}