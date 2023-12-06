package cn.telecom.state.scheduler;


import cn.telecom.state.scheduler.strategy.WaitingStrategy;

import java.util.function.Supplier;

public interface EventPublishSchedulerFactory extends SchedulerFactory {
    EventPublishScheduler create(long id);


    void setWaitingStrategy(Supplier<WaitingStrategy> waitingStrategy);
}
