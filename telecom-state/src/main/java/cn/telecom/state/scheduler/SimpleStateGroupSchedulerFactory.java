package cn.telecom.state.scheduler;

import cn.telecom.state.scheduler.strategy.WaitingStrategy;
import com.google.common.base.Preconditions;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Supplier;

public class SimpleStateGroupSchedulerFactory implements StateGroupSchedulerFactory {

    protected final StateGroupSchedulerFactoryConfig stateGroupFactoryConfig;

    protected ThreadFactory threadFactory = Executors.defaultThreadFactory();

    protected Supplier<WaitingStrategy> waitingStrategySupplier;

    public SimpleStateGroupSchedulerFactory(StateGroupSchedulerFactoryConfig stateGroupFactoryConfig) {
        this.stateGroupFactoryConfig = stateGroupFactoryConfig;
        long updateFrequency = stateGroupFactoryConfig.getUpdateFrequency();
        if (updateFrequency > 0) {
            this.setWaitingStrategySupplier(() -> WaitingStrategy.sleeping(updateFrequency));
        } else {
            this.setWaitingStrategySupplier(WaitingStrategy::yield);
        }
    }

    @Override
    public StateGroupScheduler create(long id) {
        int maxSize = this.stateGroupFactoryConfig.getMaxSize();
        if (1 == maxSize) {
            return new SingleStateGroupScheduler(id, this.waitingStrategySupplier.get(), this.threadFactory);
        }
        return new SimpleStateGroupScheduler(id,
                this.waitingStrategySupplier.get(),
                this.threadFactory,
                maxSize);
    }

    @Override
    public void setThreadFactory(ThreadFactory threadFactory) {
        Preconditions.checkNotNull(threadFactory);
        this.threadFactory = threadFactory;
    }

    @Override
    public void setWaitingStrategySupplier(Supplier<WaitingStrategy> strategySupplier) {
        this.waitingStrategySupplier = strategySupplier;
    }
}
