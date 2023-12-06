package cn.telecom.state.scheduler.strategy;

class BlockingWaitingStrategy implements WaitingStrategy {

    private final Object LOCK = new Object();

    BlockingWaitingStrategy() {
    }

    @Override
    public void await() throws InterruptedException {
        synchronized (this.LOCK) {
            this.LOCK.wait();
        }
    }

    @Override
    public void signalAllWhenBlocking() {
        synchronized (this.LOCK) {
            this.LOCK.notifyAll();
        }
    }
}
