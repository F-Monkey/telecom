package cn.telecom.server.netty.tcp;

import cn.telecom.server.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyWebSocketServer implements Server {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final ServerBootstrap bootstrap;

    protected final EventLoopGroup bossGroup;

    protected final EventLoopGroup workerGroup;

    protected final ChannelInitializer<NioSocketChannel> customerHandler;

    protected final int port;

    public NettyWebSocketServer(ChannelInitializer<NioSocketChannel> customerHandler,
                                int port,
                                int bossSize,
                                int workerSize) {
        this.bootstrap = new ServerBootstrap();
        this.bossGroup = new NioEventLoopGroup(bossSize);
        this.workerGroup = new NioEventLoopGroup(workerSize);
        this.customerHandler = customerHandler;
        this.port = port;
    }

    @Override
    public void start() {
        this.bootstrap.group(this.bossGroup, this.workerGroup)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(this.customerHandler);

        try {
            ChannelFuture channelFuture = this.bootstrap.bind(this.port).sync();
            log.info("server start at port: {}", this.port);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.bossGroup.shutdownGracefully();
            this.workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }
}
