package org.screamingsandals.lib.utils;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class ControllableImpl implements Controllable {
    private final List<ControllableImpl> controllableList = new LinkedList<>();

    private Runnable enableMethod;
    private Runnable postEnableMethod;
    private Runnable preDisableMethod;
    private Runnable disableMethod;

    @Override
    public Controllable enable(@NotNull Runnable enableMethod) {
        this.enableMethod = enableMethod;
        return this;
    }

    @Override
    public Controllable postEnable(@NotNull Runnable postEnableMethod) {
        this.postEnableMethod = postEnableMethod;
        return this;
    }

    @Override
    public Controllable preDisable(@NotNull Runnable preDisableMethod) {
        this.preDisableMethod = preDisableMethod;
        return this;
    }

    @Override
    public Controllable disable(@NotNull Runnable disableMethod) {
        this.disableMethod = disableMethod;
        return this;
    }

    @Override
    public Controllable child() {
        var controllable = new ControllableImpl();
        controllableList.add(controllable);
        return controllable;
    }

    public void enable() {
        controllableList.forEach(ControllableImpl::enable);
        if (enableMethod != null) {
            enableMethod.run();
        }
    }

    public void postEnable() {
        controllableList.forEach(ControllableImpl::postEnable);
        if (postEnableMethod != null) {
            postEnableMethod.run();
        }
    }

    public void preDisable() {
        if (preDisableMethod != null) {
            preDisableMethod.run();
        }
        new LinkedList<>(controllableList)
                .descendingIterator()
                .forEachRemaining(ControllableImpl::preDisable);
    }

    public void disable() {
        if (disableMethod != null) {
            disableMethod.run();
        }
        new LinkedList<>(controllableList)
                .descendingIterator()
                .forEachRemaining(ControllableImpl::disable);
    }

    public void reload() {
        preDisable();
        disable();
        enable();
        postEnable();
    }
}
