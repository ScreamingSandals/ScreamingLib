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

package org.screamingsandals.lib.impl.bukkit.ai.goal;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.ai.goal.Goal;
import org.screamingsandals.lib.impl.nms.accessors.GoalAccessor;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitGoal extends BasicWrapper<Object> implements Goal {
    protected BukkitGoal(@NotNull Object wrappedObject) {
        super(wrappedObject);
        Preconditions.checkArgument(GoalAccessor.getType().isInstance(wrappedObject), "wrappedObject must be an instance of Goal");
    }

    @Override
    public boolean canUse() {
        return (boolean) Reflect.fastInvoke(GoalAccessor.getMethodCanUse1());
    }

    @Override
    public boolean canContinueToUse() {
        return (boolean) Reflect.fastInvoke(GoalAccessor.getMethodCanContinueToUse1());
    }

    @Override
    public void start() {
        Reflect.fastInvoke(GoalAccessor.getMethodStart1());
    }

    @Override
    public void stop() {
        Reflect.fastInvoke(GoalAccessor.getMethodStop1());
    }

    @Override
    public void tick() {
        Reflect.fastInvoke(GoalAccessor.getMethodTick1());
    }
}
