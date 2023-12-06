package cn.telecom.state.scheduler;


import cn.telecom.commons.bean.Countable;
import cn.telecom.state.core.StateGroup;

public interface StateGroupScheduler extends Scheduler, Countable {
    boolean tryAddStateGroup(StateGroup stateGroup);
}
