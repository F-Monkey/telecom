package cn.telecom.state.scheduler.strategy;

public interface WaitingStrategy {
    default void await() throws InterruptedException {
    }

    default void signalAllWhenBlocking() {

    }

    static WaitingStrategy blocking() {
        return new BlockingWaitingStrategy();
    }

    static WaitingStrategy sleeping(long waitTime) {
        return new SleepingWaitingStrategy(waitTime);
    }

    static WaitingStrategy noOp() {
        return new NoOpWaitingStrategy();
    }

    static WaitingStrategy yield() {
        return new YieldWaitingStrategy();
    }
}
