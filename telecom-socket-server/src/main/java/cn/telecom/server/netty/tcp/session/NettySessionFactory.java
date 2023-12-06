package cn.telecom.server.netty.tcp.session;

import cn.telecom.server.SessionFactory;
import io.netty.channel.ChannelHandlerContext;

public interface NettySessionFactory extends SessionFactory<ChannelHandlerContext> {
}
