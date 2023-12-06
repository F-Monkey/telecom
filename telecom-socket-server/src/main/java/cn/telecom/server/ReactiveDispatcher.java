package cn.telecom.server;

import reactor.core.publisher.Mono;

public interface ReactiveDispatcher<Pkg> {
    Mono<Void> dispatch(Session session, Pkg pkg);
}
