package cn.telecom.orm;

public interface BeforeUpdateBehavior<T> {
    default void beforeUpdate(T t) {
    }
}
