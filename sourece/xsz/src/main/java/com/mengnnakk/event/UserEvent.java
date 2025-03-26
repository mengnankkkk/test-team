package com.mengnnakk.event;


import com.mengnnakk.entry.UserEventLog;
import org.springframework.context.ApplicationEvent;

public class UserEvent extends ApplicationEvent {
    private final UserEventLog userEventLog;


    public UserEvent(final UserEventLog userEventLog) {
        super(userEventLog);
        this.userEventLog = userEventLog;
    }

    public UserEventLog getUserEventLog(){
        return userEventLog;
    }
}