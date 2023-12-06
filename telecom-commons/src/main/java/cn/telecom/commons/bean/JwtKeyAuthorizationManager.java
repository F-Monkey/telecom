package cn.telecom.commons.bean;

import cn.telecom.commons.data.Authorization;
import cn.telecom.commons.data.JwtCreateOptions;
import cn.telecom.commons.data.JwtProperties;
import cn.telecom.commons.data.KVPair;
import cn.telecom.commons.util.JwtUtils;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Map;

public class JwtKeyAuthorizationManager implements AuthorizationKeyManager {

    private final JwtProperties jwtProperties;

    private final Gson gson = new Gson();

    public JwtKeyAuthorizationManager(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public String encrypt(Authorization authorization) {
        JwtCreateOptions.Builder builder = JwtCreateOptions.builder()
                .subject(jwtProperties.getSubject())
                .secret(jwtProperties.getSecret())
                .content(jwtProperties.getContent())
                .id(jwtProperties.getId());
        Map<String, Object> claims = jwtProperties.getClaims();
        if (!CollectionUtils.isEmpty(claims)) {
            builder.claims(claims);
        }
        builder.addClaim(KVPair.of(AUTHORIZATION_KEY, authorization));
        long expireTimeMs = jwtProperties.getExpireTimeMs();
        if (expireTimeMs > 0) {
            Date date = new Date();
            builder.issuedAt(date);
            builder.expiration(new Date(date.getTime() + expireTimeMs));
        }
        return JwtUtils.encrypt(builder.build());
    }

    @Override
    public Authorization decrypt(String s) {
        Claims claims = JwtUtils.decrypt(s, jwtProperties.getSecret());
        Object o = claims.get(AUTHORIZATION_KEY);
        if (o == null) {
            return null;
        }
        return gson.fromJson(gson.toJson(o), Authorization.class);
    }

    @Override
    public boolean isValidKey(String key) {
        Claims claims = JwtUtils.decrypt(key, jwtProperties.getSecret());
        try {
            long exp = JwtUtils.getExp(claims);
            return new Date().getTime() < exp;
        } catch (NullPointerException e) {
            return true;
        }
    }
}
