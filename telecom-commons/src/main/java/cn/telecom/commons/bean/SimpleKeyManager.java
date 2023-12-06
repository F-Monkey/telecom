package cn.telecom.commons.bean;

import java.util.Random;

public class SimpleKeyManager implements KeyManager<String> {
    @Override
    public String encrypt(String o) {
        int i = o.hashCode();
        long l = new Random().nextLong() * 2 << 16;
        long v = (long) i + l;
        return Long.toHexString(v);
    }

    @Override
    public String decrypt(String s) {
        return s;
    }
}
