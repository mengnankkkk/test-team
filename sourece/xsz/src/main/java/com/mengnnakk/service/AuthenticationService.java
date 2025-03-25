package com.mengnnakk.service;


import com.mengnnakk.entry.User;

public interface AuthenticationService {
    boolean authUser(String username,String password);

    boolean authUser(User user,String username,String password);

    String pwdEncode(String password);

    String pwdDecode(String endodPwd);
}