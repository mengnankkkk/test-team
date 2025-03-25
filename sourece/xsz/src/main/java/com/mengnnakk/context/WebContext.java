package com.mengnnakk.context;


import com.mengnnakk.entry.User;
import com.mengnnakk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.Serializable;

@Component
public class WebContext {
    private static final String USER_ATTRIBUTES = "USER_ATTRIBUTES";
    private final UserService userService;

    @Autowired
    public WebContext(UserService userService){
        this.userService = userService;
    }

    public void setCurrentUser(User user) {
        RequestContextHolder.currentRequestAttributes().setAttribute(USER_ATTRIBUTES, user, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 获取当前用户
     * @return
     */
    public User getCurrentUser(){
        User user = (User) RequestContextHolder.currentRequestAttributes().getAttribute(USER_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);
        if (null ==user){
            return user;
        }else {
            org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (null==springUser){
                return null;
            }
            user = userService.getUserByUserName(springUser.getUsername());
            if (user != null) { // 只有 user 不为 null 时才设置上下文
                setCurrentUser(user);
            } else {
                throw new RuntimeException("用户不存在");
            }
            return user;
        }

    }
}