package cn.telecom.commons.data;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum DataStatus implements Serializable {
    NEW(0), DELETED(1);

    private final int code;

    DataStatus(int code) {
        this.code = code;
    }

    public static DataStatus of(int code) {
        for (DataStatus ds : values()) {
            if (ds.getCode() == code) {
                return ds;
            }
        }
        return null;
    }
}
