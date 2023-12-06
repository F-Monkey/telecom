package cn.telecom.spring.web;


import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Extension {

    Options uid() default @Options(value = HttpHeaderConstants.AUTHORIZATION_KEY);

    Options platformId() default @Options(value = HttpHeaderConstants.PLATFORM_ID_KEY);

    Options orgId() default @Options(value = HttpHeaderConstants.ORG_ID_KEY, required = false);

    Options traceId() default @Options(value = HttpHeaderConstants.TRACE_ID_KEY, required = false);

    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Options {

        String value() default "";

        boolean required() default true;
    }
}
