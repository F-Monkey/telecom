package cn.telecom.commons.bean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class RefreshScheduler {

    private final ScheduledExecutorService scheduledExecutorService;

    public RefreshScheduler(int nThread) {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(nThread);
    }

    public void addTask(Refreshable refreshable) {
        this.scheduledExecutorService.scheduleAtFixedRate(refreshable::refresh,
                refreshable.delay(),
                refreshable.timeIntervalMs(),
                TimeUnit.MILLISECONDS);
    }
}
