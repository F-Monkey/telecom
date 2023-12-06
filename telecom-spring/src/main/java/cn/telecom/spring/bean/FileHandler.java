package cn.telecom.spring.bean;

import cn.telecom.commons.data.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface FileHandler extends Bean {
    default Result<?> upload(MultipartHttpServletRequest request) {
        throw new UnsupportedOperationException();
    }

    default void download(HttpServletRequest request,
                          HttpServletResponse response) {
        throw new UnsupportedOperationException();
    }
}
