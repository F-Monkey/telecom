package cn.telecom.state.core;


import cn.telecom.commons.bean.Refreshable;
import org.springframework.objenesis.instantiator.util.UnsafeUtils;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleStateGroupPool implements StateGroupPool, Refreshable {

    protected final StateGroupFactory stateGroupFactory;
    protected volatile ConcurrentHashMap<String, StateGroup> stateGroupMap;

    static final Unsafe UNSAFE;
    static final long STATE_GROUP_MAP_OFFSET;

    static {
        UNSAFE = UnsafeUtils.getUnsafe();
        Field f;
        try {
            f = SimpleStateGroupPool.class.getDeclaredField("stateGroupMap");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        STATE_GROUP_MAP_OFFSET = UNSAFE.objectFieldOffset(f);
    }

    public SimpleStateGroupPool(StateGroupFactory stateGroupFactory) {
        this.stateGroupMap = new ConcurrentHashMap<>();
        this.stateGroupFactory = stateGroupFactory;
    }

    @Override
    public FetchStateGroup findOrCreate(String id, Object... args) {
        boolean[] isNew = {false};
        final ConcurrentHashMap<String, StateGroup> stateGroupMap = this.stateGroupMap;
        StateGroup stateGroup = stateGroupMap.computeIfAbsent(id, (key) -> {
            isNew[0] = true;
            return this.stateGroupFactory.create(key, args);
        });
        this.stateGroupMap = stateGroupMap;
        return new FetchStateGroup(isNew[0], stateGroup);
    }

    @Override
    public void refresh() {
        final ConcurrentHashMap<String, StateGroup> stateGroupMap = this.stateGroupMap;
        ConcurrentHashMap<String, StateGroup> newStateGroupMap = new ConcurrentHashMap<>();
        for (Map.Entry<String, StateGroup> e : stateGroupMap.entrySet()) {
            StateGroup value = e.getValue();
            if (value.canClose()) {
                value.close();
                continue;
            }
            value.flush();
            newStateGroupMap.put(e.getKey(), e.getValue());
        }
        UNSAFE.compareAndSwapObject(this, STATE_GROUP_MAP_OFFSET, stateGroupMap, newStateGroupMap);
    }
}
