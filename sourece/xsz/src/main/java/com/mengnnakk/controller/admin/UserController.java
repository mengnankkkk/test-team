package com.mengnnakk.controller.admin;


import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("AdminUserController")
@RequestMapping(value = "/api/admin/user")
public class UserController extends BaseApiController {

    private final UserService userService;
}
