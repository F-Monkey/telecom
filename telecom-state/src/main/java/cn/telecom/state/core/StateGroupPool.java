package cn.telecom.state.core;

import com.google.common.base.Preconditions;

public interface StateGroupPool {
    record FetchStateGroup(boolean isNew, StateGroup stateGroup) {
            public FetchStateGroup(boolean isNew, StateGroup stateGroup) {
                this.isNew = isNew;
                Preconditions.checkArgument(stateGroup != null, "[stateGroup] can not be null");
                this.stateGroup = stateGroup;
            }
        }

    FetchStateGroup findOrCreate(String id, Object... args);
}
