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
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.ai.goal.Goal;
import org.screamingsandals.lib.ai.goal.GoalType;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.impl.nms.accessors.GoalAccessor;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;

public class BukkitGoalType extends BasicWrapper<Class<?>> implements GoalType {
    protected BukkitGoalType(@NotNull Class<?> wrappedObject) {
        super(wrappedObject);
        Preconditions.checkArgument(GoalAccessor.getType().isAssignableFrom(wrappedObject), "A goal type must be assignable to Goal class");
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.getSimpleName();
    }

    @Override
    public boolean applicableTo(@NotNull EntityType entityType) {
        return false;
    }

    @Override
    public @Nullable Goal createGoal(@NotNull Entity entity, @NotNull Object @NotNull ... parameters) {
        return null;
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof Class || object instanceof BukkitGoalType) {
            return equals(object);
        }
        return equals(GoalType.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public @NotNull ResourceLocation location() {
        return null;
    }
}
