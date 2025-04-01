package com.mengnnakk.controller.admin;


import com.github.pagehelper.PageInfo;
import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.base.RestResponse;
import com.mengnnakk.entry.Message;
import com.mengnnakk.entry.MessageUser;
import com.mengnnakk.entry.User;
import com.mengnnakk.service.MessageService;
import com.mengnnakk.service.UserService;
import com.mengnnakk.utility.PageInfoHelper;
import com.mengnnakk.viewmodel.admin.message.MessagePageRequestVM;
import com.mengnnakk.viewmodel.admin.message.MessageSendVM;
import com.mengnnakk.viewmodel.student.user.MessageRequestVM;
import com.mengnnakk.viewmodel.student.user.MessageResponseVM;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.security.krb5.internal.PAEncTSEnc;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController("AdminMessageController")
@RequestMapping(value = "/api/admin/message")
public class MessageController extends BaseApiController {

    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }


    /**
     * 发送信息操作
     * @param model
     * @return
     */
    @RequestMapping(value = "/send",method = RequestMethod.POST)
    public RestResponse send(@RequestBody @Valid MessageSendVM model){
        User user = getCurrentUser();
        List<User> receiveUser = userService.selectByIds(model.getReceiveUserIds());
        Date now = new Date();
        Message message = new Message();
        message.setTitle(model.getTitle());
        message.setContent(model.getContent());
        message.setCreateTime(now);
        message.setReadCount(0);
        message.setReceiveUserCount(receiveUser.size());
        message.setSendUserId(user.getId());
        message.setSendUserName(user.getUserName());
        message.setSendRealName(user.getRealName());
        List<MessageUser> messageUsers = receiveUser.stream().map(d -> {
            MessageUser messageUser = new MessageUser();
            messageUser.setCreateTime(now);
            messageUser.setReaded(false);
            messageUser.setReceiveRealName(d.getRealName());
            messageUser.setReceiveUserId(d.getId());
            messageUser.setReceiveUserName(d.getUserName());
            return messageUser;
        }).collect(Collectors.toList());
        messageService.sendMessage(message,messageUsers);
        return RestResponse.ok();

    }


    @RequestMapping(value = "/page",method = RequestMethod.POST)
    public RestResponse<PageInfo<MessageResponseVM>> pagelist(@RequestBody MessagePageRequestVM model){
        PageInfo<Message> pageInfo = messageService.page(model);
        List<Integer> ids = pageInfo.getList().stream().map(d->d.getId()).collect(Collectors.toList());
        List<MessageUser> messageUsers = ids.size() ==0?null:messageService.selcetByMessageIds(ids);
        PageInfo<MessageResponseVM > page = PageInfoHelper.copyMap(pageInfo,m->{
            MessageRequestVM vm = modelMapper.map(m,MessageRequestVM.class);
            String receives = messageUsers.stream().filter(d->d.getMessageId().equals(m.getId())).map(d->d.getReceiveUserName())
                    .collect(Collectors.joining(","));
            vm.setReceiveUserId(Integer.valueOf(receives));
            return vm;
        });
        return RestResponse.ok(page);
    }
}
