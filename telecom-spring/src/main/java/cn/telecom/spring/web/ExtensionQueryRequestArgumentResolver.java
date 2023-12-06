package cn.telecom.spring.web;

import cn.telecom.commons.data.UserSession;
import cn.telecom.spring.web.data.ExtensionQueryRequest;
import com.google.common.base.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionQueryRequestArgumentResolver implements HandlerMethodArgumentResolver {

    private final Map<MethodParameter, Extension> authAnnotationCache;

    public ExtensionQueryRequestArgumentResolver() {
        this.authAnnotationCache = new ConcurrentHashMap<>();
    }

    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        return null != parameter.getParameterAnnotation(Extension.class)
                && ExtensionQueryRequest.class.equals(parameter.getParameterType());
    }

    protected String resolved(NativeWebRequest webRequest,
                              Extension.Options options) {
        String headerKey = options.value();
        boolean required = options.required();
        if (required && Strings.isNullOrEmpty(headerKey)) {
            throw new NullPointerException("[" + headerKey + "] is required");
        }
        String header = webRequest.getHeader(headerKey);
        if (required && Strings.isNullOrEmpty(header)) {
            throw new NullPointerException("[" + headerKey + "] is required");
        }
        return header;
    }

    protected UserSession resolvedUId(NativeWebRequest webRequest,
                                      Extension.Options options) {
        String headerKey = options.value();
        boolean required = options.required();
        if (required && Strings.isNullOrEmpty(headerKey)) {
            throw new NullPointerException("[" + headerKey + "] is required");
        }
        Object attribute = webRequest.getAttribute(UserSession.KEY, RequestAttributes.SCOPE_REQUEST);
        if (required && attribute == null) {
            throw new NullPointerException("[" + headerKey + "] is required");
        }
        return (UserSession) attribute;
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        Extension authAnno = this.authAnnotationCache.computeIfAbsent(parameter, p -> p.getParameterAnnotation(Extension.class));
        if (null == authAnno) {
            return null;
        }
        UserSession userSession;
        try {
            userSession = this.resolvedUId(webRequest, authAnno.uid());
        } catch (NullPointerException npe) {
            mavContainer.setStatus(HttpStatus.UNAUTHORIZED);
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, npe.getMessage());
        }
        String platformId = this.resolved(webRequest, authAnno.platformId());
        String orgId = this.resolved(webRequest, authAnno.orgId());
        String tractId = this.resolved(webRequest, authAnno.traceId());
        ExtensionQueryRequest.ExtensionQueryRequestBuilder builder = ExtensionQueryRequest.builder();
        ExtensionQueryRequest queryRequest = builder.uid(userSession == null ? null : userSession.getUid())
                .platformId(platformId)
                .orgId(orgId)
                .traceId(tractId)
                .build();
        queryRequest.setUserSession(userSession);
        return queryRequest;
    }
}
