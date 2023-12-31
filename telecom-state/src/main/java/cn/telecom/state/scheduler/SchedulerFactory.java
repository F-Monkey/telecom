package cn.telecom.state.scheduler;

import java.util.concurrent.ThreadFactory;

public interface SchedulerFactory {
    Scheduler create(long id);

    void setThreadFactory(ThreadFactory threadFactory);

}
