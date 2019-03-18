package com.tbp.interceptor;

import com.tbp.model.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession {

    User user;

    public void addLoggerUser(User user) {
        this.user = user;
    }

    public void removeLoggerUser(User user) {
        this.user = null;
    }

    public User getLoggerUser() {
        return this.user;
    }

}
