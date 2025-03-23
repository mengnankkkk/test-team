package com.mengnnakk.viewmodel.admin.message;


import com.mengnnakk.base.BasePage;

public class MessagePageRequestVM extends BasePage {
    private String sendUserName;

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }
}
