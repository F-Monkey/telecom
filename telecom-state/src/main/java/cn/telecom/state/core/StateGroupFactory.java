package cn.telecom.state.core;

public interface StateGroupFactory {
    StateGroup create(String id, Object ...args);
}
