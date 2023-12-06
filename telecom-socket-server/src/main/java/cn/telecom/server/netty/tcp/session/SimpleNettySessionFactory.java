package cn.telecom.server.netty.tcp.session;

import cn.telecom.server.Session;
import io.netty.channel.ChannelHandlerContext;

public class SimpleNettySessionFactory implements NettySessionFactory {
    @Override
    public Session create(ChannelHandlerContext channelHandlerContext) {
        return new SimpleNettySession(channelHandlerContext);
    }
}
