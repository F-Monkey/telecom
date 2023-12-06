package cn.telecom.state.scheduler;

import cn.telecom.state.scheduler.strategy.WaitingStrategy;
import com.google.common.base.Preconditions;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Supplier;

public class SimpleEventPublishSchedulerFactory implements EventPublishSchedulerFactory {

    protected ThreadFactory threadFactory = Executors.defaultThreadFactory();

    protected Supplier<WaitingStrategy> waitingStrategySupplier = WaitingStrategy::blocking;

    @Override
    public EventPublishScheduler create(long id) {
        return new SimpleEventPublishScheduler(id, this.waitingStrategySupplier.get(), this.threadFactory);
    }

    @Override
    public void setThreadFactory(ThreadFactory threadFactory) {
        Preconditions.checkNotNull(threadFactory);
        this.threadFactory = threadFactory;
    }

    @Override
    public void setWaitingStrategy(Supplier<WaitingStrategy> waitingStrategySupplier) {
        Preconditions.checkNotNull(waitingStrategySupplier);
        this.waitingStrategySupplier = waitingStrategySupplier;
    }
}
