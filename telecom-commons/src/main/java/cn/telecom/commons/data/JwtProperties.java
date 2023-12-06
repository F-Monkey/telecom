package cn.telecom.commons.data;

import lombok.Data;

import java.util.Map;

@Data
public class JwtProperties {

    private String id;

    private String subject;

    private long expireTimeMs = 7 * 24 * 60 * 60 * 1000;

    private String secret;

    private Map<String, Object> claims;

    private String content;
}
