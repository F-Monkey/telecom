package cn.telecom.server;

import reactor.core.publisher.Mono;

public interface FilterChain<Pkg> {
    Mono<Void> doFilter(Session session, Pkg pkg);
}
