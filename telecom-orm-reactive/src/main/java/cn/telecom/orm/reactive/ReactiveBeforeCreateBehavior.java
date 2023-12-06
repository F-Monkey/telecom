package cn.telecom.orm.reactive;

import reactor.core.publisher.Mono;

public interface ReactiveBeforeCreateBehavior<T> {
    default Mono<Void> beforeCreate(T t) {
        return Mono.empty();
    }
}
