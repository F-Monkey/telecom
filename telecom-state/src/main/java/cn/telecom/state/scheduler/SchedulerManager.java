package cn.telecom.state.scheduler;

public interface SchedulerManager {
    void addEvent(String groupId, Object event, Object... args);
}