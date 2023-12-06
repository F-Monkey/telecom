package cn.telecom.server;

public interface SessionManager<T> {
    Session findOrCreate(T t);
}
