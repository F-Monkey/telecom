package cn.telecom.state.core;

import cn.telecom.commons.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public abstract class AbstractState implements State {

    private final String code;

    protected final Logger log = LoggerFactory.getLogger(this.getClass());


    protected final StateGroup stateGroup;

    public AbstractState(String code,
                         StateGroup stateGroup) {
        Objects.requireNonNull(code, "code can not be null");
        Objects.requireNonNull(stateGroup, "stateGroup can not be null");
        this.code = code;
        this.stateGroup = stateGroup;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public void init(Timer timer) throws Exception {

    }

    @Override
    public StateGroup getStateGroup() {
        return this.stateGroup;
    }

    @Override
    public void onInitError(Timer timer, Exception e) {
        log.error("stateGroup: {} state: {} init error on time: {} \n", this.getStateGroup().id(), this.code(), timer.getCurrentTimeMs(), e);
    }

    @Override
    public void fireEvent(Timer timer, Object event) throws Exception {

    }

    @Override
    public void onFireEventError(Timer timer, Object event, Exception e) {
        log.error("stateGroup: {} state: {} fireEvent error on time: {} \n", this.getStateGroup().id(), this.code(), timer.getCurrentTimeMs(), e);
    }

    @Override
    public void update(Timer timer, StateInfo stateInfo) throws Exception {

    }

    @Override
    public void onUpdateError(Timer timer, StateInfo stateInfo, Exception e) {
        log.error("stateGroup: {} state: {} update error on time: {} \n", this.getStateGroup().id(), this.code(), timer.getCurrentTimeMs(), e);
    }

    @Override
    public String onFinishError(Timer timer, Exception e) {
        log.error("stateGroup: {} state: {} finish error on time: {} \n", this.getStateGroup().id(), this.code(), timer.getCurrentTimeMs(), e);
        return null;
    }
}
