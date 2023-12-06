package cn.telecom.orm.reactive;

import reactor.core.publisher.Mono;

public interface ReactiveBeforeUpdateBehavior<T> {
    default Mono<Void> beforeUpdate(T t) {
        return Mono.empty();
    }
}
