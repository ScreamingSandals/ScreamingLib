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

package org.screamingsandals.lib.ai;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.ai.goal.Goal;
import org.screamingsandals.lib.ai.goal.GoalType;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.utils.RawValueHolder;

import java.util.List;

public interface GoalSelector extends Wrapper, RawValueHolder {
    void add(int priority, @NotNull Goal goal);

    boolean has(@NotNull Goal goal);

    boolean has(@NotNull GoalType type);

    @Unmodifiable @NotNull List<@NotNull Goal> get(@NotNull GoalType type);

    @Unmodifiable @NotNull List<@NotNull Goal> all();

    void removeAll();

    void remove(@NotNull Goal goal);

    void remove(@NotNull GoalType type);

    // special add goal methods

    @Nullable Goal addFloatGoal(int priority);

    @Nullable Goal addMeleeAttackGoal(int priority, double speed, boolean pauseWhenMobIdle);

    @Nullable Goal addRandomStrollGoal(int priority, double speed);

    @Nullable Goal addRandomLookAroundGoal(int priority);

    @Nullable Goal addNearestAttackableTargetGoal(int priority, @NotNull EntityType type, boolean checkVisibility);

    @Nullable Goal addHurtByTargetGoal(int priority, @NotNull List<@NotNull EntityType> ignoredEntities);
}
