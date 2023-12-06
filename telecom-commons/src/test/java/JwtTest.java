import cn.telecom.commons.bean.AuthorizationKeyManager;
import cn.telecom.commons.bean.JwtKeyAuthorizationManager;
import cn.telecom.commons.data.Authorization;
import cn.telecom.commons.data.JwtProperties;
import cn.telecom.commons.data.UserSession;
import cn.telecom.commons.util.JwtUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class JwtTest {

    @Test
    public void test01() {
        String s = "eyJhbGciOiJub25lIn0.eyJleHAiOjE2OTk2MDMyMTUsImlhdCI6MTY5OTYwMjYxMCwiYXV0aG9yaXphdGlvbiI6eyJ1aWQiOiI2NGZmZDhkM2EzMDM1MTY5MDIyMjYwZmUiLCJuaWNrbmFtZSI6IuadjuW5syIsInBob25lTm8iOiIxODA1NTA1OTE4NiJ9fQ.";
        Claims decrypt = JwtUtils.decrypt(s);
        Object o = decrypt.get(AuthorizationKeyManager.AUTHORIZATION_KEY);
        Gson gson = new Gson();
        Authorization authorization = gson.fromJson(gson.toJson(o), Authorization.class);
        System.out.println(authorization.getUid());
    }

    @Test
    public void test02() {
        JwtProperties jwtProperties = new JwtProperties();
        JwtKeyAuthorizationManager jwtKeyAuthorizationManager = new JwtKeyAuthorizationManager(jwtProperties);
        Authorization authorization = new Authorization();
        authorization.setUid("64ffd8d3a3035169022260fe");
        authorization.setUsername("李平");
        String encrypt = jwtKeyAuthorizationManager.encrypt(authorization);
        System.err.println(encrypt);
        boolean validKey = jwtKeyAuthorizationManager.isValidKey(encrypt);
        System.err.println(validKey);

    }

    @Test
    public void test03() {
        LoadingCache<String, UserSession> build = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.MINUTES).build(new CacheLoader<>() {
            public @NonNull UserSession load(@NonNull String key) throws Exception {
                return new UserSession();
            }
        });
        build.asMap().remove("111");
    }
}
