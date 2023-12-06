package cn.telecom.spring.web;

import cn.telecom.commons.data.UserSession;

public class ThreadLocalUserSessionContext implements UserSessionContext {

    private final ThreadLocal<UserSession> threadLocal;

    public ThreadLocalUserSessionContext() {
        threadLocal = new ThreadLocal<>();
    }

    @Override
    public UserSession get() {
        return this.threadLocal.get();
    }

    @Override
    public void put(UserSession userSession) {
        this.threadLocal.set(userSession);
    }

    @Override
    public void remove() {
        this.threadLocal.remove();
    }
}
