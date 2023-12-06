package cn.telecom.commons.bean;

public interface KeyManager<T> {
    String encrypt(T t);

    T decrypt(String s);
}
