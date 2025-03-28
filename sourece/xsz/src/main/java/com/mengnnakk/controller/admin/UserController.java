package com.mengnnakk.controller.admin;


import com.baomidou.mybatisplus.extension.api.R;
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
import com.mengnnakk.viewmodel.admin.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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

    /**
     * 查看用户
     * @param model
     * @return
     */
    @RequestMapping(value = "/page/list",method = RequestMethod.POST)
    public RestResponse<PageInfo<UserResponseVM>> pagelist(@RequestBody UserPageRequestVM model){
        PageInfo<User> pageInfo = userService.userPage(model);
        PageInfo<UserResponseVM> page = PageInfoHelper.copyMap(pageInfo,d->UserResponseVM.from(d));
        return RestResponse.ok(page);
    }

    /**
     * 查看事件的用户
     * @param model
     * @return
     */

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

    /**
     * 根据id搜索用户
     * @param id
     * @return
     */
    @RequestMapping(value = "/select/{id}",method = RequestMethod.POST)
    public RestResponse<UserResponseVM> select(@PathVariable Integer id){
        User user = userService.getUserById(id);
        UserResponseVM userResponseVM = UserResponseVM.from(user);
        return RestResponse.ok(userResponseVM);
    }

    /**
     * 查看当前用户
     * @return
     */

    @RequestMapping(value = "/current",method = RequestMethod.POST)
    public RestResponse<UserResponseVM> current(){
        User user  = getCurrentUser();
        UserResponseVM  userResponseVM = UserResponseVM.from(user);
        return RestResponse.ok(userResponseVM);
    }


    /**
     * 编辑注册
     * @param model
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public RestResponse<User> edit(@RequestBody @Valid UserCreateVM model){
        if (model.getId()==null){
            User existUser = userService.getUserByUserName(model.getUserName());
            if (null!=existUser){
                return new RestResponse<>(2,"用户已存在");
            }
            if (StringUtils.isEmpty(model.getPassword())){
                return new RestResponse<>(3,"密码不能为空");
            }
        }
        if (StringUtils.isEmpty(model.getBirthDay())){
            model.setBirthDay(null);
        }
        User user = modelMapper.map(model,User.class);

        if (model.getId()==null){
            String encodePwd = authenticationService.pwdEncode(model.getPassword());
            user.setPassword(encodePwd);
            user.setUserUuid(UUID.randomUUID().toString());
            user.setCreateTime(new Date());
            user.setLastActiveTime(new Date());
            user.setDeleted(false);
        }else {
            if (!StringUtils.isEmpty(model.getPassword())){
                String encodePwd = authenticationService.pwdEncode(model.getPassword());
                user.setPassword(encodePwd);
            }
            user.setModifyTime(new Date());
            userService.updateById(user);
        }
        return RestResponse.ok(user);
    }

    /**
     * 更新用户
     * @param model
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
public RestResponse update(@RequestBody @Valid UserUpdateVM model){
        User user = userService.selectById(getCurrentUser().getId());
        modelMapper.map(model,user);
        user.setModifyTime(new Date());
        userService.updateById(user);
        return RestResponse.ok();
    }







}
