package cn.telecom.spring.web.session;

import cn.telecom.commons.data.UserSession;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.NonNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DefaultUserSessionCache implements UserSessionCache {

    private final Cache cache;

    private final LoadingCache<String, UserSession> userCache;

    public DefaultUserSessionCache(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(UserSession.KEY);
        if (this.cache == null) {
            throw new NullPointerException();
        }
        this.userCache = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.MINUTES).build(new CacheLoader<>() {
            public @NonNull UserSession load(@NonNull String key) throws Exception {
                return Objects.requireNonNull(DefaultUserSessionCache.this.cache.get(key, UserSession.class));
            }
        });
    }

    @Override
    public UserSession find(String key) {
        return userCache.getUnchecked(key);
    }

    @Override
    public void put(String key, UserSession userSession) {
        this.cache.put(key, userSession);
    }

    @Override
    public void remove(String key) {
        this.userCache.asMap().remove(key);
    }
}
