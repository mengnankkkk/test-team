package com.mengnnakk.controller.student;


import com.github.pagehelper.PageInfo;
import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.base.RestResponse;
import com.mengnnakk.entry.Message;
import com.mengnnakk.entry.MessageUser;
import com.mengnnakk.entry.User;
import com.mengnnakk.entry.UserEventLog;
import com.mengnnakk.entry.enums.RoleEnum;
import com.mengnnakk.entry.enums.UserStatusEnum;
import com.mengnnakk.event.UserEvent;
import com.mengnnakk.service.AuthenticationService;
import com.mengnnakk.service.MessageService;
import com.mengnnakk.service.UserEventLogService;
import com.mengnnakk.service.UserService;
import com.mengnnakk.utility.DateTimeUtil;
import com.mengnnakk.utility.PageInfoHelper;
import com.mengnnakk.viewmodel.admin.user.UserEventLogVM;
import com.mengnnakk.viewmodel.admin.user.UserResponseVM;
import com.mengnnakk.viewmodel.student.user.MessageRequestVM;
import com.mengnnakk.viewmodel.student.user.MessageResponseVM;
import com.mengnnakk.viewmodel.student.user.UserRegisterVM;
import org.bouncycastle.LICENSE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import sun.security.krb5.internal.PAData;

import javax.jws.soap.SOAPBinding;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController("StudentUserController")
@RequestMapping(value = "/api/student/user")
public class UserController extends BaseApiController {
    private final UserService userService;
    private final UserEventLogService userEventLogService;
    private final MessageService messageService;
    private final AuthenticationService authenticationService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserController(UserService userService, UserEventLogService userEventLogService, MessageService messageService, AuthenticationService authenticationService, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.userEventLogService = userEventLogService;
        this.messageService = messageService;
        this.authenticationService = authenticationService;
        this.eventPublisher = eventPublisher;
    }
    @PostMapping("/current")
    public RestResponse<UserResponseVM> current(){
        User user = getCurrentUser();
        UserResponseVM userResponseVM = UserResponseVM.from(user);
        return RestResponse.ok(userResponseVM);
    }
    @PostMapping("/register")
    public RestResponse register(@RequestBody @Valid UserRegisterVM model){
        User exisUser = userService.getUserByUserName(model.getUserName());
        if (null!=exisUser){
            return new RestResponse<>(2,"用户已存在");
        }
        User user = modelMapper.map(model,User.class);
        String encodePwd = authenticationService.pwdEncode(model.getPassword());
        user.setUserUuid(UUID.randomUUID().toString());
        user.setPassword(encodePwd);
        user.setRole(RoleEnum.STUDENT.getCode());
        user.setStatus(UserStatusEnum.Enable.getCode());
        user.setLastActiveTime(new Date());
        user.setCreateTime(new Date());
        user.setDeleted(false);
        userService.insert(user);//无法解析 'UserService' 中的方法 'insertByFilter'
        UserEventLog userEventLog = new UserEventLog(user.getId(), user.getUserName(), user.getRealName(), new Date());
        userEventLog.setContent("欢迎 " + user.getUserName() + " 注册考试系统");
        eventPublisher.publishEvent(new UserEvent(userEventLog));
        return RestResponse.ok();
    }
    @PostMapping("/message/read/{id}")
    public RestResponse read(@PathVariable Integer id){
        messageService.read(id);
        return RestResponse.ok();
    }
    @PostMapping("/message/unreadCount")
    public RestResponse unReadCount(){
        Integer count = messageService.unReadCount(getCurrentUser().getId());
        return RestResponse.ok(count);
    }
    @PostMapping("/log")
    public RestResponse<List<UserEventLogVM>> log(){
        User user = getCurrentUser();
        List<UserEventLog> userEventLogs = userEventLogService.getUserEventLogByUserId(user.getId());
        List<UserEventLogVM> userEventLogVMS = userEventLogs.stream().map(d->{
            UserEventLogVM vm = modelMapper.map(d,UserEventLogVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(d.getCreateTime()));
            return vm;
        }).collect(Collectors.toList());
        return RestResponse.ok(userEventLogVMS);
    }
    @RequestMapping(value = "/message/page", method = RequestMethod.POST)
    public RestResponse<PageInfo<MessageResponseVM>> messagePageList(@RequestBody MessageRequestVM messageRequestVM) {
        messageRequestVM.setReceiveUserId(getCurrentUser().getId());
        PageInfo<MessageUser> messageUserPageInfo = messageService.studentPage(messageRequestVM);
        List<Integer> ids = messageUserPageInfo.getList().stream().map(d -> d.getMessageId()).collect(Collectors.toList());
        List<Message> messages = ids.size() != 0 ? messageService.selectMessageByIds(ids) : null;
        PageInfo<MessageResponseVM> page = PageInfoHelper.copyMap(messageUserPageInfo, e -> {
            MessageResponseVM vm = modelMapper.map(e, MessageResponseVM.class);
            messages.stream().filter(d -> e.getMessageId().equals(d.getId())).findFirst().ifPresent(message -> {
                vm.setTitle(message.getTitle());
                vm.setContent(message.getContent());
                vm.setSendUserName(message.getSendUserName());
            });
            vm.setCreateTime(DateTimeUtil.dateFormat(e.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }


}

