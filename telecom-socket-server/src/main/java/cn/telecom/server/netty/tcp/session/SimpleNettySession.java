package cn.telecom.server.netty.tcp.session;

import cn.telecom.server.Session;
import cn.telecom.server.netty.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SimpleNettySession implements Session {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    protected final ChannelHandlerContext ctx;
    private static final ConcurrentMap<String, AttributeKey<Object>> ATTRIBUTE_KEY_MAP;

    private final String id;

    static {
        ATTRIBUTE_KEY_MAP = new ConcurrentHashMap<>();
    }

    public SimpleNettySession(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.id = SessionUtil.getId(this.ctx);
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public Object setAttribute(String key, Object val) {
        AttributeKey<Object> attrKey = ATTRIBUTE_KEY_MAP.compute(key, (k, v) -> {
            if (v == null) {
                v = AttributeKey.newInstance(k);
            }
            return v;
        });
        return this.ctx.channel().attr(attrKey).getAndSet(val);
    }

    @Override
    public Object getAttribute(String key) {
        AttributeKey<Object> attributeKey = ATTRIBUTE_KEY_MAP.get(key);
        if (attributeKey == null) {
            throw new IllegalArgumentException("key:" + key + " is not exists");
        }
        return this.ctx.channel().attr(attributeKey).get();
    }

    @Override
    public void write(Object data) {
        if (null == data) {
            return;
        }
        if (data instanceof String) {
            this.ctx.writeAndFlush(new TextWebSocketFrame((String) data));
            return;
        }

        if (data instanceof BinaryWebSocketFrame) {
            this.ctx.writeAndFlush(data);
            return;
        }
        log.error("invalid data type: {}", data.getClass());
    }

    @Override
    public boolean isActive() {
        return this.ctx.channel().isActive();
    }

    @Override
    public void close() throws IOException {
        this.ctx.channel().close();
    }

    @Override
    public String getRemoteAddress() {
        return this.ctx.channel().remoteAddress().toString();
    }
}
