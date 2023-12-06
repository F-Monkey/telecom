package cn.telecom.spring.bean;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterFileHandler {
    default SseEmitter upload(HttpServletRequest httpServletRequest) {
        throw new UnsupportedOperationException();
    }

    default SseEmitter download(HttpServletRequest httpServletRequest) {
        throw new UnsupportedOperationException();
    }
}
