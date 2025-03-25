package com.mengnnakk.service.impl;


import com.mengnnakk.configuration.property.SystemConfig;
import com.mengnnakk.entry.User;
import com.mengnnakk.service.AuthenticationService;
import com.mengnnakk.service.UserService;
import com.mengnnakk.utility.RsaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final SystemConfig systemConfig;

    @Autowired
    public AuthenticationServiceImpl(UserService userService,SystemConfig systemConfig){
        this.userService = userService;
        this.systemConfig = systemConfig;
    }


    @Override
    public boolean authUser(String username, String password) {
        User user = userService.getUserByUserName(username);
        return authUser(user,username,password);
    }

    @Override
    public boolean authUser(User user, String username, String password) {
        if (user == null) {
            return false;
        }
        String encodePwd = user.getPassword();
        if (null==encodePwd||encodePwd.length()==0){
            return false;
        }
        String pwd = pwdDecode(encodePwd);
        return pwd.equals(password);
    }

    @Override
    public String pwdEncode(String password) {
        return RsaUtil.rsaEncode(systemConfig.getPwdKey().getPublicKey(),password);
    }

    @Override
    public String pwdDecode(String endodPwd) {
        return RsaUtil.rsaDecode(systemConfig.getPwdKey().getPrivateKey(),endodPwd);
    }
}