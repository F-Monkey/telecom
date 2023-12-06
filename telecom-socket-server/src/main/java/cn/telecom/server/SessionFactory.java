package cn.telecom.server;

public interface SessionFactory<T> {
    Session create(T t) throws SessionException;
}
