package cn.telecom.orm.reactive;

import cn.telecom.commons.data.BaseEntity;
import cn.telecom.commons.data.KVPair;
import cn.telecom.commons.data.UserSession;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.function.Supplier;

public interface ReactiveRepositoryConfiguration {
    default ReactiveBeforeCreateBehavior<BaseEntity> beforeCreateBehavior(Supplier<UserSession> userSessionSupplier) {
        return new ReactiveBeforeCreateBehavior<>() {
            @Override
            public Mono<Void> beforeCreate(BaseEntity baseEntity) {
                return Mono.fromRunnable(() -> {
                    UserSession userSession = userSessionSupplier.get();
                    if (userSession != null) {
                        baseEntity.setCreator(KVPair.of(userSession.getUid(), userSession.getUsername()));
                        baseEntity.setUpdater(KVPair.of(userSession.getUid(), userSession.getUsername()));
                    }
                    long time = new Date().getTime();
                    baseEntity.setCreateDateTime(time);
                    baseEntity.setUpdateDateTime(time);
                });
            }
        };
    }

    default ReactiveBeforeUpdateBehavior<BaseEntity> beforeUpdateBehavior(Supplier<UserSession> userSessionSupplier) {

        return new ReactiveBeforeUpdateBehavior<>() {
            @Override
            public Mono<Void> beforeUpdate(BaseEntity baseEntity) {
                return Mono.fromRunnable(() -> {
                    UserSession userSession = userSessionSupplier.get();
                    if (userSession != null) {
                        baseEntity.setUpdater(KVPair.of(userSession.getUid(), userSession.getUsername()));
                    }
                    long time = new Date().getTime();
                    baseEntity.setUpdateDateTime(time);
                });
            }
        };
    }
}
