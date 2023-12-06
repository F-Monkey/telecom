package cn.telecom.spring.web.reactive.filter;

import cn.telecom.commons.data.UserSession;
import cn.telecom.commons.data.vo.Result;
import cn.telecom.commons.data.vo.Results;
import cn.telecom.spring.web.HttpHeaderConstants;
import com.google.common.base.Strings;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class ReactiveUserSessionFilter implements WebFilter {
    private final Function<String, Mono<UserSession>> userSessionFunction;
    final Runnable afterFilterRunner;

    public ReactiveUserSessionFilter(final Function<String, Mono<UserSession>> userSessionFunction,
                                     final Runnable afterFilterRunner) {
        this.userSessionFunction = userSessionFunction;
        this.afterFilterRunner = afterFilterRunner;
    }

    protected Mono<Void> writeResponse(ServerHttpResponse serverHttpResponse, Result<?> result) {
        return serverHttpResponse.writeAndFlushWith(Flux.just(Flux.just(serverHttpResponse.bufferFactory().wrap(Results.toJson(result).getBytes()))));
    }

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange,
                             @NonNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = request.getHeaders().getFirst(HttpHeaderConstants.AUTHORIZATION_KEY);
        if (Strings.isNullOrEmpty(token)) {
            return chain.filter(exchange);
        }
        return this.userSessionFunction.apply(token)
                .doOnNext(userSession -> exchange.getAttributes().put(UserSession.KEY, userSession))
                .then(chain.filter(exchange).then(Mono.defer(() -> Mono.fromRunnable(this.afterFilterRunner))));
    }
}
