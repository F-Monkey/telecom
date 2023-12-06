package cn.telecom.server;

import java.io.Closeable;
import java.io.IOException;

public interface Session extends Closeable {

    String id();

    Object setAttribute(String key, Object val);

    Object getAttribute(String key);

    void write(Object data);

    boolean isActive();

    @Override
    void close() throws IOException;

    String getRemoteAddress();
}
