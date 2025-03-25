package com.mengnnakk.context;

import com.mengnnakk.entry.User;
import com.mengnnakk.entry.UserToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Component
public class WxContext {
    private static final String USER_ATTRIBUTES = "USER_ATTRIBUTES";
    private static final String USER_TOKEN_ATTRIBUTES = "USER_TOKEN_ATTRIBUTES"; // 去掉多余的 "String"

    /**
     * 通过 RequestContextHolder.currentRequestAttributes() 获取当前请求的 RequestAttributes（即当前 HTTP 请求的上下文）。
     *
     * 将 User 和 UserToken 存储到 request 作用域 中，这样它们可以在同一个请求的生命周期内被取出使用。
     *
     * RequestAttributes.SCOPE_REQUEST 代表 请求作用域，意思是这些数据仅在单个 HTTP 请求中有效，一旦请求结束，数据就会销毁。
     * @param user
     * @param userToken
     */

    public void setContext(User user, UserToken userToken) {
        RequestContextHolder.currentRequestAttributes().setAttribute(USER_ATTRIBUTES, user, RequestAttributes.SCOPE_REQUEST);
        RequestContextHolder.currentRequestAttributes().setAttribute(USER_TOKEN_ATTRIBUTES, userToken, RequestAttributes.SCOPE_REQUEST); // SCOPE_REQUESTS 改为 SCOPE_REQUEST
    }

    /**
     * 从 RequestAttributes 中获取 USER_ATTRIBUTES（即 User 实例）。
     * @return
     */

    public User getCurrentUser() {
        return (User) RequestContextHolder.currentRequestAttributes().getAttribute(USER_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST); // 修正中文分号
    }

    /**
     * RequestAttributes 取出 UserToken（用户令牌）
     * @return
     */

    public UserToken getCurrentUserToken() {
        return (UserToken) RequestContextHolder.currentRequestAttributes().getAttribute(USER_TOKEN_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);
    }
}
