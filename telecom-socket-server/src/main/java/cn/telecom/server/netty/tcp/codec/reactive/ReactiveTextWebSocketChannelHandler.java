package cn.telecom.server.netty.tcp.codec.reactive;

import cn.telecom.server.*;
import cn.telecom.server.netty.tcp.session.NettySessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@ChannelHandler.Sharable
public class ReactiveTextWebSocketChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final Logger log = LoggerFactory.getLogger(ReactiveTextWebSocketChannelHandler.class);

    protected final NettySessionManager sessionManager;

    protected FilterChain<String> filterChain;
    protected final ReactiveDispatcher<String> dispatcher;

    protected final Scheduler scheduler;

    public ReactiveTextWebSocketChannelHandler(NettySessionManager sessionManager,
                                               List<ReactiveFilter<String>> filters,
                                               ReactiveDispatcher<String> dispatcher,
                                               int schedulerSize) {
        this.sessionManager = sessionManager;
        if (filters != null && !filters.isEmpty()) {
            this.filterChain = new DefaultFilterChain(filters);
        }
        if (schedulerSize <= 0) {
            throw new IllegalArgumentException("invalid argument [schedulerSize]: " + schedulerSize);
        }
        if (schedulerSize == 1) {
            this.scheduler = Schedulers.newSingle("ReactiveTextWebSocketChannelHandlerScheduler");
        } else {
            this.scheduler = Schedulers.newParallel("ReactiveTextWebSocketChannelHandlerScheduler", schedulerSize);
        }
        this.dispatcher = dispatcher;
    }

    public ReactiveTextWebSocketChannelHandler(NettySessionManager sessionManager,
                                               List<ReactiveFilter<String>> filters,
                                               ReactiveDispatcher<String> dispatcher) {
        this(sessionManager, filters, dispatcher, 1);
    }

    static class DefaultFilterChain implements FilterChain<String> {
        private final int index;
        private final List<ReactiveFilter<String>> filters;

        DefaultFilterChain(int index, DefaultFilterChain parent) {
            this.index = index;
            this.filters = parent.getFilters();
        }

        DefaultFilterChain(List<ReactiveFilter<String>> filters) {
            this.filters = filters;
            this.index = 0;
        }

        List<ReactiveFilter<String>> getFilters() {
            return this.filters;
        }

        @Override
        public Mono<Void> doFilter(Session session, String s) {
            return Mono.defer(() -> {
                if (this.index < filters.size()) {
                    ReactiveFilter<String> reactiveFilter = filters.get(index);
                    DefaultFilterChain filterChain = new DefaultFilterChain(this.index + 1, this);
                    return reactiveFilter.filter(filterChain, session, s);
                } else {
                    return Mono.empty(); // complete
                }
            });
        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String text = msg.text();
        Session session;
        try {
            session = sessionManager.findOrCreate(ctx);
        } catch (Exception e) {
            log.error("session find or create error:\n", e);
            return;
        }
        Mono<Void> voidMono;
        if (this.filterChain != null) {
            voidMono = filterChain.doFilter(session, text)
                    .then(this.dispatcher.dispatch(session, text))
                    .doOnError(e -> log.error("reactive channel handler error: \n", e));
        } else {
            voidMono = this.dispatcher.dispatch(session, text);
        }
        voidMono.subscribeOn(this.scheduler).subscribe();
    }
}
