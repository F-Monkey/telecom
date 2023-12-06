package cn.telecom.server.netty;

import io.netty.channel.ChannelHandlerContext;

public abstract class SessionUtil {
    public static String getId(ChannelHandlerContext ctx) {
        return ctx.channel().id().asLongText();
    }
}
