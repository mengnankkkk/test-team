package com.mengnnakk.configuration.spring.wx;


import com.mengnnakk.base.SystemCode;
import com.mengnnakk.configuration.spring.security.RestUtil;
import com.mengnnakk.context.WxContext;
import com.mengnnakk.entry.User;
import com.mengnnakk.entry.UserToken;
import com.mengnnakk.service.UserService;
import com.mengnnakk.service.UserTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class TokenHandlerInterceptor implements HandlerInterceptor {

    private final UserTokenService userTokenService;
    private final UserService userService;
    private final WxContext wxContext;

    @Autowired
    public TokenHandlerInterceptor(UserTokenService userTokenService, UserService userService, WxContext wxContext) {
        this.userTokenService = userTokenService;
        this.userService = userService;
        this.wxContext = wxContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            RestUtil.response(response, SystemCode.UNAUTHORIZED);
            return false;
        }

        if (StringUtils.isBlank(token)) {
            RestUtil.response(response, SystemCode.UNAUTHORIZED);
            return false;
        }

        if (token.length() != 36) {
            RestUtil.response(response, SystemCode.UNAUTHORIZED);
            return false;
        }

        UserToken userToken = userTokenService.getToken(token);
        if (null == userToken) {
            RestUtil.response(response, SystemCode.UNAUTHORIZED);
            return false;
        }

        Date now = new Date();
        User user = userService.getUserByUserName(userToken.getUserName());
        if (now.before(userToken.getEndTime())) {
            wxContext.setContext(user,userToken);
            return true;
        } else {   //refresh token
            UserToken refreshToken = userTokenService.insertUserToken(user);
            RestUtil.response(response, SystemCode.AccessTokenError.getCode(), SystemCode.AccessTokenError.getMessage(), refreshToken.getToken());
            return false;
        }
    }
}
