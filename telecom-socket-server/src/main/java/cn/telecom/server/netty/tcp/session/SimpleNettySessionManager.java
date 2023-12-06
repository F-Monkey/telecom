package cn.telecom.server.netty.tcp.session;

import cn.telecom.commons.bean.Refreshable;
import cn.telecom.server.Session;
import cn.telecom.server.SessionFactory;
import cn.telecom.server.netty.SessionUtil;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleNettySessionManager implements NettySessionManager, Refreshable {

    protected final SessionFactory<ChannelHandlerContext> sessionFactory;
    private volatile ConcurrentHashMap<String, Session> sessionMap;

    public SimpleNettySessionManager(SessionFactory<ChannelHandlerContext> sessionFactory) {
        this.sessionMap = new ConcurrentHashMap<>();
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Session findOrCreate(ChannelHandlerContext ctx) {
        final ConcurrentHashMap<String, Session> sessionMap = this.sessionMap;
        Session session = sessionMap.compute(SessionUtil.getId(ctx), (k, v) -> {
            if (null == v) {
                return this.sessionFactory.create(ctx);
            }
            if (!v.isActive()) { // NEVER HAPPENED
                return this.sessionFactory.create(ctx);
            }
            return v;
        });
        this.sessionMap = sessionMap;
        if (session instanceof HeartBeatNettySession) {
            ((HeartBeatNettySession) session).refreshLastOperateTime();
        }
        return session;
    }

    @Override
    public void refresh() {
        Iterator<Map.Entry<String, Session>> iterator = this.sessionMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Session> next = iterator.next();
            Session value = next.getValue();
            if (!value.isActive()) {
                try {
                    value.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                iterator.remove();
            }
        }
    }
}
