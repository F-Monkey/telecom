package cn.telecom.state.scheduler;


import cn.telecom.state.scheduler.strategy.WaitingStrategy;

import java.util.function.Supplier;

public interface StateGroupSchedulerFactory extends SchedulerFactory {
    StateGroupScheduler create(long id);

    void setWaitingStrategySupplier(Supplier<WaitingStrategy> strategySupplier);

}
