package cn.telecom.server.netty.tcp;

import cn.telecom.server.netty.tcp.codec.HttpRequestChannelHandler;
import cn.telecom.server.netty.tcp.session.NettySessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class WebSocketChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    protected final String path;
    protected final ChannelHandler customerHandler;
    protected final NettySessionManager sessionManager;

    public WebSocketChannelInitializer(String path,
                                       ChannelHandler customerHandler, NettySessionManager sessionManager) {
        this.path = path;
        this.customerHandler = customerHandler;
        this.sessionManager = sessionManager;
    }

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new HttpRequestChannelHandler(sessionManager));
        pipeline.addLast(new WebSocketServerProtocolHandler(path, null,
                true, 65336 * 10));
        pipeline.addLast(customerHandler);
    }
}
