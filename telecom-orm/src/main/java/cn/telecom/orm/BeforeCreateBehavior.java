package cn.telecom.orm;

public interface BeforeCreateBehavior<T> {
    default void beforeCreate(T t) {
    }
}
