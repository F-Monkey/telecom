package cn.telecom.commons.data;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum Disabled implements Serializable {
    TRUE("true"), FALSE("false");
    private final String code;

    Disabled(String code) {
        this.code = code;
    }
}
