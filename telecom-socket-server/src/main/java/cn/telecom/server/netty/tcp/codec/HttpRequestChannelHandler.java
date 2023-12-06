package cn.telecom.server.netty.tcp.codec;

import cn.telecom.server.SessionException;
import cn.telecom.server.netty.tcp.session.NettySessionManager;
import com.google.common.base.Strings;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestChannelHandler.class);

    public static final AttributeKey<Map<String, String>> PARAMS_KEY = AttributeKey.newInstance("params");

    private final NettySessionManager nettSessionManager;

    public HttpRequestChannelHandler(NettySessionManager nettSessionManager) {
        this.nettSessionManager = nettSessionManager;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
        Map<String, String> paramsMap = this.decodeRequestQueryParams(httpRequest);
        ctx.channel().attr(PARAMS_KEY).set(paramsMap);
        this.nettSessionManager.findOrCreate(ctx);
        ctx.fireChannelRead(httpRequest.retain());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof SessionException) {
            log.error("session create error:\n", cause);
            ctx.channel().close();
        }
        super.exceptionCaught(ctx, cause);
    }

    private Map<String, String> decodeRequestQueryParams(FullHttpRequest httpRequest) throws URISyntaxException {
        HttpHeaders headers = httpRequest.headers();
        log.info("headers: {}", headers);
        URI uri = new URI(httpRequest.uri());
        httpRequest.setUri(uri.getPath());
        String query = uri.getQuery();
        Map<String, String> paramsMap = new HashMap<>();
        if (Strings.isNullOrEmpty(query)) {
            return paramsMap;
        }
        for (String s : query.split("&")) {
            String[] split = s.split("=");
            if (split.length != 2) {
                continue;
            }
            paramsMap.put(split[0].trim(), split[1].trim());
        }
        return paramsMap;
    }
}
