package cn.telecom.server.netty.tcp.session;

import cn.telecom.server.SessionManager;
import io.netty.channel.ChannelHandlerContext;

public interface NettySessionManager extends SessionManager<ChannelHandlerContext> {
}
