/*
 * Copyright 2023 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.ai;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.ai.GoalSelector;
import org.screamingsandals.lib.ai.goal.Goal;
import org.screamingsandals.lib.ai.goal.GoalType;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.List;

public class BukkitGoalSelector extends BasicWrapper<Object> implements GoalSelector {
    protected BukkitGoalSelector(@NotNull Object wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public void add(int priority, @NotNull Goal goal) {

    }

    @Override
    public void has(@NotNull Goal goal) {

    }

    @Override
    public void has(@NotNull GoalType type) {

    }

    @Override
    public @Nullable Goal get(@NotNull GoalType type) {
        return null;
    }

    @Override
    public @Unmodifiable @NotNull List<@NotNull Goal> all() {
        return null;
    }

    @Override
    public void removeAll() {

    }

    @Override
    public void remove(@NotNull Goal goal) {

    }

    @Override
    public void remove(@NotNull GoalType type) {

    }
}
