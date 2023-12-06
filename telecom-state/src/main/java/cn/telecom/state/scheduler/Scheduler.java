package cn.telecom.state.scheduler;

public interface Scheduler {

    long id();

    void start();

    boolean isStarted();

    void stop();
}
