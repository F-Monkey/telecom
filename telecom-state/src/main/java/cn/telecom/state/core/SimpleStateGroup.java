package cn.telecom.state.core;


import cn.telecom.commons.util.Timer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class SimpleStateGroup extends AbstractStateGroup {

    public SimpleStateGroup(String id, StateContext stateContext, Timer timer, boolean canAutoUpdate) {
        super(id, stateContext, timer, canAutoUpdate);
    }

    @Override
    protected Queue<Object> createEventQueue() {
        return new LinkedList<>();
    }

    @Override
    protected Map<String, State> createStateMap() {
        return new HashMap<>();
    }
}
