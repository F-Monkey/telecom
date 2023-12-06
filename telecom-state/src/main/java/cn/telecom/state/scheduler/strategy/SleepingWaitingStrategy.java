package cn.telecom.state.scheduler.strategy;

import com.google.common.base.Preconditions;

import java.util.concurrent.locks.LockSupport;

class SleepingWaitingStrategy implements WaitingStrategy {

    private final long waitTimeMs;

    SleepingWaitingStrategy(long waitTimeMs) {
        Preconditions.checkArgument(waitTimeMs > 0, "[waitTime] must be more than 0");
        this.waitTimeMs = waitTimeMs;
    }

    @Override
    public void await() {
        LockSupport.parkNanos(this.waitTimeMs * 1000);
    }
}
