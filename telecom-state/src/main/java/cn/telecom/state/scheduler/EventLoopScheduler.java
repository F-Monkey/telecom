package cn.telecom.state.scheduler;


import cn.telecom.state.scheduler.strategy.WaitingStrategy;

import java.util.concurrent.ThreadFactory;

public abstract class EventLoopScheduler extends AbstractScheduler {

    protected final WaitingStrategy waitingStrategy;

    public EventLoopScheduler(long id,
                              WaitingStrategy waitingStrategy,
                              ThreadFactory threadFactory) {
        super(id, threadFactory);
        this.waitingStrategy = waitingStrategy;
    }

    @Override
    protected Thread newThread() {
        return super.threadFactory.newThread(() -> {
            for (; ; ) {
                try {
                    this.waitingStrategy.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
                this.execute();
            }
        });
    }

    protected abstract void execute();
}
