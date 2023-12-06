package cn.telecom.commons.data.vo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;

import java.io.Serializable;

@Getter
@Builder(access = AccessLevel.PACKAGE)
public class Result<T> implements Serializable {
    private int code;
    private String msg;
    private T data;
    private Throwable error;

    @Tolerate
    private Result() {
    }

}
