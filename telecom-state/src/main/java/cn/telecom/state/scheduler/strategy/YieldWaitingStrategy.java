package cn.telecom.state.scheduler.strategy;


public class YieldWaitingStrategy implements WaitingStrategy {
    @Override
    public void await() {
        Thread.yield();
    }
}
