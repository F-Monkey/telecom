package cn.telecom.state.scheduler;

public interface EventPublishScheduler extends Scheduler {
    void publish(Runnable event);
}
