package cn.telecom.state.core;

public interface StateContext {

    StateContext EMPTY = new StateContext() {
    };

    default <T> T get(String key, Class<T> c) {
        throw new UnsupportedOperationException();
    }

    default void put(String key, Object v){
        throw new UnsupportedOperationException();
    }
}
