/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.impl.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.Controllable;

import java.util.LinkedList;
import java.util.List;

public class ControllableImpl implements Controllable {
    private final @NotNull List<@NotNull ControllableImpl> controllableList = new LinkedList<>();

    private @Nullable Runnable pluginLoadMethod;
    private @Nullable Runnable enableMethod;
    private @Nullable Runnable postEnableMethod;
    private @Nullable Runnable preDisableMethod;
    private @Nullable Runnable disableMethod;

    @Override
    public @NotNull Controllable pluginLoad(@NotNull Runnable pluginLoadMethod) {
        this.pluginLoadMethod = pluginLoadMethod;
        return this;
    }

    @Override
    public @NotNull Controllable enable(@NotNull Runnable enableMethod) {
        this.enableMethod = enableMethod;
        return this;
    }

    @Override
    public @NotNull Controllable postEnable(@NotNull Runnable postEnableMethod) {
        this.postEnableMethod = postEnableMethod;
        return this;
    }

    @Override
    public @NotNull Controllable preDisable(@NotNull Runnable preDisableMethod) {
        this.preDisableMethod = preDisableMethod;
        return this;
    }

    @Override
    public @NotNull Controllable disable(@NotNull Runnable disableMethod) {
        this.disableMethod = disableMethod;
        return this;
    }

    @Override
    public @NotNull Controllable child() {
        var controllable = new ControllableImpl();
        controllableList.add(controllable);
        return controllable;
    }

    public void pluginLoad() {
        controllableList.forEach(ControllableImpl::pluginLoad);
        if (pluginLoadMethod != null) {
            pluginLoadMethod.run();
        }
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
