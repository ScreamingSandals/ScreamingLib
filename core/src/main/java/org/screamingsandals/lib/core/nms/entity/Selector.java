package org.screamingsandals.lib.core.nms.entity;

import lombok.Getter;

import static org.screamingsandals.lib.core.reflect.SReflect.*;
import static org.screamingsandals.lib.core.nms.utils.ClassStorage.NMS.EntityInsentient;
import static org.screamingsandals.lib.core.nms.utils.ClassStorage.NMS.PathfinderGoal;
import static org.screamingsandals.lib.core.nms.utils.ClassStorage.obtainNewPathfinderSelector;

public abstract class Selector {
    protected final Object handler;
    protected final String keys;
    @Getter
    protected Object selector;

    protected Selector(Object handler, String keys) {
        if (!EntityInsentient.isInstance(handler)) {
            throw new IllegalArgumentException("Invalid mob type");
        }
        this.handler = handler;
        this.keys = keys;
        this.selector = getField(this.handler, this.keys);
    }

    public void registerPathfinder(int position, Object pathfinder) {
        getMethod(this.selector, "a,func_75776_a", Integer.TYPE, PathfinderGoal).invoke(position, pathfinder);
    }

    public void clearSelector() {
        try {
            this.selector = setField(this.handler, this.keys, obtainNewPathfinderSelector(handler));
        } catch (Throwable ignored) {
        }
    }
}
