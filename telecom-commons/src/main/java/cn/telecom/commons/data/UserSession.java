package cn.telecom.commons.data;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

@Data
public class UserSession implements Serializable {

    public static final String KEY = "user_session";

    private String uid;
    private String token;
    private String username;
    private Collection<String> orgIds;
}
