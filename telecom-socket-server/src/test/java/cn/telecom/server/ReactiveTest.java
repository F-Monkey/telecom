package cn.telecom.server;

import cn.telecom.server.netty.tcp.NettyWebSocketServer;
import cn.telecom.server.netty.tcp.WebSocketChannelInitializer;
import cn.telecom.server.netty.tcp.codec.reactive.ReactiveTextWebSocketChannelHandler;
import cn.telecom.server.netty.tcp.session.NettySessionFactory;
import cn.telecom.server.netty.tcp.session.NettySessionManager;
import cn.telecom.server.netty.tcp.session.SimpleNettySessionFactory;
import cn.telecom.server.netty.tcp.session.SimpleNettySessionManager;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.Collections;

public class ReactiveTest {
    @Test
    public void test01() {
        String path = "/test";
        NettySessionFactory nettySessionFactory = new SimpleNettySessionFactory();
        NettySessionManager nettySessionManager = new SimpleNettySessionManager(nettySessionFactory);
        ReactiveDispatcher<String> dispatcher = (session, s) -> {
            session.write("result: " + s);
            return Mono.empty();
        };
        ReactiveFilter<String> filter = (chain, session, s) -> {
            return Mono.error(new IllegalArgumentException("invalid message: " + s));
        };
        ReactiveTextWebSocketChannelHandler reactiveTextWebSocketChannelHandler = new ReactiveTextWebSocketChannelHandler(nettySessionManager, Collections.singletonList(filter), dispatcher);
        WebSocketChannelInitializer webSocketChannelInitializer = new WebSocketChannelInitializer(path, reactiveTextWebSocketChannelHandler, nettySessionManager);
        NettyWebSocketServer nettyWebSocketServer = new NettyWebSocketServer(webSocketChannelInitializer, 8080, 2, 2);
        nettyWebSocketServer.start();
    }
}
