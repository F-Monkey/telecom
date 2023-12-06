package cn.telecom.spring.web.session;

import cn.telecom.commons.data.UserSession;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DefaultReactiveUserSessionCache implements ReactiveUserSessionCache {

    private final Cache cache;

    private final LoadingCache<String, UserSession> userCache;

    public DefaultReactiveUserSessionCache(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(UserSession.KEY);
        if (this.cache == null) {
            throw new NullPointerException();
        }
        this.userCache = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.MINUTES).build(new CacheLoader<>() {
            public @NonNull UserSession load(@NonNull String key) throws Exception {
                return Objects.requireNonNull(DefaultReactiveUserSessionCache.this.cache.get(key, UserSession.class));
            }
        });
    }

    @Override
    public Mono<UserSession> find(String key) {
        return Mono.justOrEmpty(this.cache.get(key, UserSession.class));
    }

    @Override
    public Mono<Void> put(String key, UserSession userSession) {
        return Mono.fromRunnable(() -> this.cache.put(key, userSession));
    }

    @Override
    public Mono<Void> remove(String key) {
        return Mono.fromRunnable(() -> this.userCache.asMap().remove(key));
    }
}
