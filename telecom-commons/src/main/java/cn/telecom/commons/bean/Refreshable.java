package cn.telecom.commons.bean;

public interface Refreshable {
    void refresh();

    default long delay() {
        return 0;
    }

    default long timeIntervalMs() {
        return 1000;
    }
}
