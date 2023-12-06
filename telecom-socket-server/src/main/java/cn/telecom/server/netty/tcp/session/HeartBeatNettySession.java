package cn.telecom.server.netty.tcp.session;

import cn.telecom.commons.util.Timer;
import io.netty.channel.ChannelHandlerContext;

public class HeartBeatNettySession extends SimpleNettySession {

    protected final Timer timer;

    private volatile long lastOperateTime;

    private long timeThreshold = 5000;

    public HeartBeatNettySession(ChannelHandlerContext ctx,
                                 Timer timer) {
        super(ctx);
        this.timer = timer;
        this.refreshLastOperateTime();
    }

    public void refreshLastOperateTime() {
        this.lastOperateTime = timer.getCurrentTimeMs();
    }

    public void setTimeThreshold(long timeThreshold) {
        this.timeThreshold = timeThreshold;
    }

    @Override
    public boolean isActive() {
        return super.isActive() && (this.timer.getCurrentTimeMs() - lastOperateTime < timeThreshold);
    }
}
