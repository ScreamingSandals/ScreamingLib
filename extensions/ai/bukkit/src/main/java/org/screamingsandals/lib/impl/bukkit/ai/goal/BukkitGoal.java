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

package org.screamingsandals.lib.impl.bukkit.ai.goal;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.ai.goal.Goal;
import org.screamingsandals.lib.impl.nms.accessors.GoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.Goal$FlagAccessor;
import org.screamingsandals.lib.impl.nms.accessors.TargetGoalAccessor;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Set;

public class BukkitGoal extends BasicWrapper<Object> implements Goal {
    public BukkitGoal(@NotNull Object wrappedObject) {
        super(wrappedObject);
        Preconditions.checkArgument(GoalAccessor.TYPE.get().isInstance(wrappedObject), "wrappedObject must be an instance of Goal");
    }

    @Override
    public boolean isTarget() {
        if (GoalAccessor.METHOD_GET_FLAGS.get() != null) {
            var flags = Reflect.fastInvoke(wrappedObject, GoalAccessor.METHOD_GET_FLAGS.get());

            if (flags instanceof Set) {
                return ((Set<?>) flags).contains(Goal$FlagAccessor.FIELD_TARGET.get());
            }

            return false;
        } else {
            return TargetGoalAccessor.TYPE.get().isInstance(wrappedObject);
        }
    }

    @Override
    public boolean canUse() {
        return (boolean) Reflect.fastInvoke(wrappedObject, GoalAccessor.METHOD_CAN_USE.get());
    }

    @Override
    public boolean canContinueToUse() {
        return (boolean) Reflect.fastInvoke(wrappedObject, GoalAccessor.METHOD_CAN_CONTINUE_TO_USE.get());
    }

    @Override
    public void start() {
        Reflect.fastInvoke(wrappedObject, GoalAccessor.METHOD_START.get());
    }

    @Override
    public void stop() {
        Reflect.fastInvoke(wrappedObject, GoalAccessor.METHOD_STOP.get());
    }

    @Override
    public void tick() {
        Reflect.fastInvoke(wrappedObject, GoalAccessor.METHOD_TICK.get());
    }
}
