package cn.telecom.commons.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class Authorization implements Serializable {

    public static final String KEY = "authorization";

    private String token;
    private String uid;
    private String orgId;
    private String username;
    private String nickname;
    private String phoneNo;
    private String email;
    private String headIcon;
}
