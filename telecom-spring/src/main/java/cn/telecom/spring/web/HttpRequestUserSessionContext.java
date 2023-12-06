package cn.telecom.spring.web;

import cn.telecom.commons.data.UserSession;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpRequestUserSessionContext implements UserSessionContext {

    protected final Map<String, UserSession> userSessionMap;

    public HttpRequestUserSessionContext() {
        userSessionMap = new ConcurrentHashMap<>();
    }

    @Override
    public UserSession get() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String sessionId = requestAttributes.getSessionId();
        return this.userSessionMap.get(sessionId);
    }

    @Override
    public void put(UserSession userSession) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String sessionId = requestAttributes.getSessionId();
        this.userSessionMap.put(sessionId, userSession);
    }

    @Override
    public void remove() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String sessionId = requestAttributes.getSessionId();
        this.userSessionMap.remove(sessionId);
    }
}
