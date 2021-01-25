package org.screamingsandals.lib.utils;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class Controllable {
    private final List<Controllable> controllableList = new LinkedList<>();

    private Runnable enableMethod;
    private Runnable disableMethod;

    public Controllable enable(@NotNull Runnable enableMethod) {
        this.enableMethod = enableMethod;
        return this;
    }

    public Controllable disable(@NotNull Runnable disableMethod) {
        this.disableMethod = disableMethod;
        return this;
    }

    public Controllable child() {
        var controllable = new Controllable();
        controllableList.add(controllable);
        return controllable;
    }

    public void enable() {
        controllableList.forEach(Controllable::enable);
        if (enableMethod != null) {
            enableMethod.run();
        }
    }

    public void disable() {
        if (disableMethod != null) {
            disableMethod.run();
        }
        new LinkedList<>(controllableList)
                .descendingIterator()
                .forEachRemaining(Controllable::disable);
    }

    public void reload() {
        disable();
        enable();
    }
}
