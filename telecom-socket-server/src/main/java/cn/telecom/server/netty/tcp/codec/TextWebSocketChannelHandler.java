package cn.telecom.server.netty.tcp.codec;

import cn.telecom.server.Dispatcher;
import cn.telecom.server.Filter;
import cn.telecom.server.Session;
import cn.telecom.server.netty.tcp.session.NettySessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ChannelHandler.Sharable
public class TextWebSocketChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final Logger log = LoggerFactory.getLogger(TextWebSocketChannelHandler.class);

    protected final NettySessionManager sessionManager;

    protected final List<Filter<String>> filters;

    protected final Dispatcher<String> dispatcher;

    public TextWebSocketChannelHandler(NettySessionManager sessionManager,
                                       List<Filter<String>> filters,
                                       Dispatcher<String> dispatcher) {
        this.sessionManager = sessionManager;
        this.filters = filters;
        this.dispatcher = dispatcher;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        Session session;
        try {
            session = sessionManager.findOrCreate(ctx);
        } catch (Exception e) {
            log.error("session find or create error:\n", e);
            return;
        }
        if (this.filters != null && this.filters.size() > 0) {
            for (Filter<String> filter : this.filters) {
                if (!filter.filter(session, text)) {
                    return;
                }
            }
        }
        if (this.dispatcher != null) {
            this.dispatcher.dispatch(session, text);
        }
    }
}
