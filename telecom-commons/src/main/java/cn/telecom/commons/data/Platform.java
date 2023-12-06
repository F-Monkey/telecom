package cn.telecom.commons.data;

import java.io.Serializable;
import java.util.Objects;

public record Platform(String id) implements Serializable {

    public static final Platform BACK_GROUND;
    public static final Platform CLIENT;

    public Platform(String id) {
        this.id = Objects.requireNonNull(id);
    }

    static {
        BACK_GROUND = new Platform("0");

        CLIENT = new Platform("1");
    }

}
