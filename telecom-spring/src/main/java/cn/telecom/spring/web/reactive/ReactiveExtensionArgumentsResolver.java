package cn.telecom.spring.web.reactive;

import cn.telecom.commons.data.UserSession;
import cn.telecom.spring.web.Extension;
import cn.telecom.spring.web.data.ExtensionQueryRequest;
import com.google.common.base.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReactiveExtensionArgumentsResolver implements HandlerMethodArgumentResolver {

    private final Map<MethodParameter, Extension> authAnnotationCache;

    public ReactiveExtensionArgumentsResolver() {
        authAnnotationCache = new ConcurrentHashMap<>();
    }

    @Nullable
    protected String resolved(ServerWebExchange exchange,
                              Extension.Options options) {
        String headerKey = options.value();
        boolean required = options.required();
        if (required && Strings.isNullOrEmpty(headerKey)) {
            throw new NullPointerException("[" + headerKey + "] is required");
        }
        String header = exchange.getRequest().getHeaders().getFirst(headerKey);
        if (required && Strings.isNullOrEmpty(header)) {
            throw new NullPointerException("[" + headerKey + "] is required");
        }
        return header;
    }

    @Nullable
    protected UserSession resolvedUId(ServerWebExchange exchange,
                                      Extension.Options options) {
        String headerKey = options.value();
        boolean required = options.required();
        if (required && Strings.isNullOrEmpty(headerKey)) {
            throw new NullPointerException("[" + headerKey + "] is required");
        }
        Object attribute = exchange.getAttribute(UserSession.KEY);
        if (required && attribute == null) {
            throw new NullPointerException("[" + headerKey + "] is required");
        }
        return (UserSession) attribute;
    }
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Extension.class) != null
                && ExtensionQueryRequest.class.equals(parameter.getParameterType());
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {
        Extension authAnno = this.authAnnotationCache.computeIfAbsent(parameter, p -> p.getParameterAnnotation(Extension.class));
        if (null == authAnno) {
            return Mono.empty();
        }
        UserSession userSession;
        try {
            userSession = this.resolvedUId(exchange, authAnno.uid());
        } catch (NullPointerException npe) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, npe.getMessage());
        }
        String platformId = this.resolved(exchange, authAnno.platformId());
        String orgId = this.resolved(exchange, authAnno.orgId());
        String tractId = this.resolved(exchange, authAnno.traceId());
        ExtensionQueryRequest.ExtensionQueryRequestBuilder builder = ExtensionQueryRequest.builder();
        ExtensionQueryRequest queryRequest = builder.uid(userSession == null ? null : userSession.getUid())
                .platformId(platformId)
                .orgId(orgId)
                .traceId(tractId)
                .build();
        queryRequest.setUserSession(userSession);
        return Mono.just(queryRequest);
    }
}
