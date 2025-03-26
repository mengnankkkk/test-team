package com.mengnnakk.configuration.spring.security;

import com.mengnnakk.base.SystemCode;
import com.mengnnakk.entry.UserEventLog;
import com.mengnnakk.event.UserEvent;
import com.mengnnakk.service.UserService;
import com.mengnnakk.entry.User; // 确保使用的是业务层的 User
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @version 3.5.0
 * @description: 处理用户登录成功事件
 * Copyright (C), 2020-2025, 武汉思维跳跃科技有限公司
 * @date 2021/12/25 9:45
 */
@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;

    @Autowired
    public RestAuthenticationSuccessHandler(ApplicationEventPublisher eventPublisher, UserService userService) {
        this.eventPublisher = eventPublisher;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) { // 确保 principal 是 UserDetails 类型
            UserDetails springUser = (UserDetails) principal;
            User user = userService.getUserByUserName(springUser.getUsername());

            if (user != null) {
                // 记录用户登录事件
                UserEventLog userEventLog = new UserEventLog(user.getId(), user.getUserName(), user.getRealName(), new Date());
                userEventLog.setContent(user.getUserName() + " 登录了学之思开源考试系统");
                eventPublisher.publishEvent(new UserEvent(userEventLog));

                // 构造返回的用户对象
                SecurityProperties.User responseUser = new SecurityProperties.User();
                responseUser.setName(user.getUserName());
                responseUser.setImagePath(user.getImagePath());

                // 返回成功响应
                RestUtil.response(response, SystemCode.OK.getCode(), SystemCode.OK.getMessage(), responseUser);
                return;
            }
        }

        // 如果用户信息获取失败，返回未授权响应
        RestUtil.response(response, SystemCode.UNAUTHORIZED.getCode(), SystemCode.UNAUTHORIZED.getMessage());
    }
}