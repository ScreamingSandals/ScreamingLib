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
import org.screamingsandals.lib.impl.bukkit.ai.goal.BukkitGoal;
import org.screamingsandals.lib.impl.nms.accessors.GoalSelectorAccessor;
import org.screamingsandals.lib.impl.nms.accessors.MobAccessor;
import org.screamingsandals.lib.impl.nms.accessors.PathfinderGoalSelector_i_PathfinderGoalSelectorItemAccessor;
import org.screamingsandals.lib.impl.nms.accessors.WrappedGoalAccessor;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BukkitGoalSelector extends BasicWrapper<Object> implements GoalSelector {
    protected BukkitGoalSelector(@NotNull Object entity) {
        super(entity);
        Preconditions.checkArgument(MobAccessor.getType().isInstance(entity));
    }

    @Override
    public void add(int priority, @NotNull Goal goal) {
        var selector = getSelector(goal);

        if (selector != null) {
            Reflect.fastInvoke(selector, GoalSelectorAccessor.getMethodAddGoal1(), priority, goal.raw());
        }
    }

    @Override
    public boolean has(@NotNull Goal goal) {
        var selector = getSelector(goal);

        if (selector == null) {
            return false;
        }

        if (GoalSelectorAccessor.getFieldAvailableGoals() != null) {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.getFieldAvailableGoals());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : (Collection<?>) goals) {
                    if (Objects.equals(Reflect.fastInvoke(g, WrappedGoalAccessor.getMethodGetGoal1()), goal.raw())) {
                        return true;
                    }
                }
            }
        } else {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.getFieldField_75782_a());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : (Collection<?>) goals) {
                    if (Objects.equals(Reflect.getField(g, PathfinderGoalSelector_i_PathfinderGoalSelectorItemAccessor.getFieldField_75733_a()), goal.raw())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean has(@NotNull GoalType type) {
        return hasInSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldGoalSelector()), type)
                || hasInSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldTargetSelector()), type);
    }

    private boolean hasInSelector(@Nullable Object selector, @NotNull GoalType type) {
        if (selector == null) {
            return false;
        }

        if (GoalSelectorAccessor.getFieldAvailableGoals() != null) {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.getFieldAvailableGoals());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : (Collection<?>) goals) {
                    var obj = Reflect.fastInvoke(g, WrappedGoalAccessor.getMethodGetGoal1());
                    if (obj != null && type.as(Class.class).equals(obj.getClass())) {
                        return true;
                    }
                }
            }
        } else {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.getFieldField_75782_a());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : (Collection<?>) goals) {
                    var obj = Reflect.getField(g, PathfinderGoalSelector_i_PathfinderGoalSelectorItemAccessor.getFieldField_75733_a());
                    if (obj != null && type.as(Class.class).equals(obj.getClass())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public @Unmodifiable @NotNull List<@NotNull Goal> get(@NotNull GoalType type) {
        var list = new ArrayList<Goal>();

        getForSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldGoalSelector()), type, list);
        getForSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldTargetSelector()), type, list);

        return Collections.unmodifiableList(list);
    }

    private void getForSelector(@Nullable Object selector, @Nullable GoalType type, @NotNull List<@NotNull Goal> finalGoals) {
        if (selector == null) {
            return;
        }

        if (GoalSelectorAccessor.getFieldAvailableGoals() != null) {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.getFieldAvailableGoals());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : (Collection<?>) goals) {
                    var obj = Reflect.fastInvoke(g, WrappedGoalAccessor.getMethodGetGoal1());
                    if (obj != null && (type == null || type.as(Class.class).equals(obj.getClass()))) {
                        finalGoals.add(new BukkitGoal(obj));
                    }
                }
            }
        } else {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.getFieldField_75782_a());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : (Collection<?>) goals) {
                    var obj = Reflect.getField(g, PathfinderGoalSelector_i_PathfinderGoalSelectorItemAccessor.getFieldField_75733_a());
                    if (obj != null && (type == null || type.as(Class.class).equals(obj.getClass()))) {
                        finalGoals.add(new BukkitGoal(obj));
                    }
                }
            }
        }
    }

    @Override
    public @Unmodifiable @NotNull List<@NotNull Goal> all() {
        var list = new ArrayList<Goal>();

        getForSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldGoalSelector()), null, list);
        getForSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldTargetSelector()), null, list);

        return Collections.unmodifiableList(list);
    }

    @Override
    public void removeAll() {
        removeForSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldGoalSelector()), null);
        removeForSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldTargetSelector()), null);
    }

    @Override
    public void remove(@NotNull Goal goal) {
        var selector = getSelector(goal);

        if (selector != null) {
            Reflect.fastInvoke(selector, GoalSelectorAccessor.getMethodRemoveGoal1(), goal.raw());
        }
    }

    @Override
    public void remove(@NotNull GoalType type) {
        removeForSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldGoalSelector()), type);
        removeForSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldTargetSelector()), type);
    }

    private void removeForSelector(@Nullable Object selector, @Nullable GoalType type) {
        if (selector == null) {
            return;
        }

        if (GoalSelectorAccessor.getFieldAvailableGoals() != null) {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.getFieldAvailableGoals());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : (Collection<?>) goals) {
                    var obj = Reflect.fastInvoke(g, WrappedGoalAccessor.getMethodGetGoal1());
                    if (obj != null && (type == null || type.as(Class.class).equals(obj.getClass()))) {
                        Reflect.fastInvoke(selector, GoalSelectorAccessor.getMethodRemoveGoal1(), obj);
                    }
                }
            }
        } else {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.getFieldField_75782_a());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : (Collection<?>) goals) {
                    var obj = Reflect.getField(g, PathfinderGoalSelector_i_PathfinderGoalSelectorItemAccessor.getFieldField_75733_a());
                    if (obj != null && (type == null || type.as(Class.class).equals(obj.getClass()))) {
                        Reflect.fastInvoke(selector, GoalSelectorAccessor.getMethodRemoveGoal1(), obj);
                    }
                }
            }
        }
    }

    private @Nullable Object getSelector(@NotNull Goal goal) {
        if (goal.isTarget()) {
            return Reflect.getField(wrappedObject, MobAccessor.getFieldTargetSelector());
        } else {
            return Reflect.getField(wrappedObject, MobAccessor.getFieldGoalSelector());
        }
    }
}
