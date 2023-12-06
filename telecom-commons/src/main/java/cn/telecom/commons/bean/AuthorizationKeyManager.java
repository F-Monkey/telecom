package cn.telecom.commons.bean;

import cn.telecom.commons.data.Authorization;

public interface AuthorizationKeyManager extends KeyManager<Authorization> {
    String AUTHORIZATION_KEY = "authorization";

    boolean isValidKey(String key);
}
