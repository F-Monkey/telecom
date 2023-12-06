package cn.telecom.spring.web;


import cn.telecom.commons.data.UserSession;

public interface UserSessionContext {
    UserSession get();
    void put(UserSession userSession);

    void remove();
}
