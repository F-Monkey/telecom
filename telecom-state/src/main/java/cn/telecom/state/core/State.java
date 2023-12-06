package cn.telecom.state.core;

import cn.telecom.commons.util.Timer;

public interface State {

    String code();

    StateGroup getStateGroup();

    default StateContext getStateContext() {
        return this.getStateGroup().getStateContext();
    }

    void init(Timer timer) throws Exception;

    void onInitError(Timer timer, Exception e);

    void fireEvent(Timer timer, Object event) throws Exception;

    void onFireEventError(Timer timer, Object event, Exception e);

    void update(Timer timer, StateInfo stateInfo) throws Exception;

    void onUpdateError(Timer timer, StateInfo stateInfo, Exception e);

    String finish(Timer timer) throws Exception;

    String onFinishError(Timer timer, Exception e);
}
