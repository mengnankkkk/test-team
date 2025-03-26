package com.mengnnakk.controller.admin;


import com.github.pagehelper.PageInfo;
import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.base.RestResponse;
import com.mengnnakk.entry.User;
import com.mengnnakk.entry.UserEventLog;
import com.mengnnakk.service.AuthenticationService;
import com.mengnnakk.service.UserEventLogService;
import com.mengnnakk.service.UserService;
import com.mengnnakk.utility.DateTimeUtil;
import com.mengnnakk.utility.PageInfoHelper;
import com.mengnnakk.viewmodel.admin.user.UserEventLogVM;
import com.mengnnakk.viewmodel.admin.user.UserEventPageRequestVM;
import com.mengnnakk.viewmodel.admin.user.UserPageRequestVM;
import com.mengnnakk.viewmodel.admin.user.UserResponseVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController("AdminUserController")
@RequestMapping(value = "/api/admin/user")
public class UserController extends BaseApiController {

    private final UserService userService;
    private final UserEventLogService userEventLogService;
    private final AuthenticationService authenticationService;

    @Autowired
    public UserController(UserService userService, UserEventLogService userEventLogService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.userEventLogService = userEventLogService;
        this.authenticationService = authenticationService;
    }
    @RequestMapping(value = "/page/list",method = RequestMethod.POST)
    public RestResponse<PageInfo<UserResponseVM>> pagelist(@RequestBody UserPageRequestVM model){
        PageInfo<User> pageInfo = userService.userPage(model);
        PageInfo<UserResponseVM> page = PageInfoHelper.copyMap(pageInfo,d->UserResponseVM.from(d));
        return RestResponse.ok(page);
    }

    @RequestMapping(value = "/event/page/list", method = RequestMethod.POST)
    public RestResponse<PageInfo<UserEventLogVM>> eventPagelist(@Valid @RequestBody UserEventPageRequestVM model) {
        PageInfo<UserEventLog> pageInfo = Optional.ofNullable(userEventLogService.page(model)).orElse(new PageInfo<>());
        PageInfo<UserEventLogVM> page = pageInfo.getList() != null ? PageInfoHelper.copyMap(pageInfo, d -> {
            if (d == null) return null;
            UserEventLogVM vm = modelMapper.map(d, UserEventLogVM.class);
            vm.setCreateTime(d.getCreateTime() != null ? DateTimeUtil.dateFormat(d.getCreateTime()) : "");
            return vm;
        }) : new PageInfo<>();

        return RestResponse.ok(page);
    }




}
