package cn.telecom.server;

import reactor.core.publisher.Mono;

public interface ReactiveFilter<Pkg> {

    Mono<Void> filter(FilterChain<Pkg> chain, Session session, Pkg pkg);
}
