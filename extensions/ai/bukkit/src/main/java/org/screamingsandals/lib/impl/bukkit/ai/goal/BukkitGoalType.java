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
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.nms.accessors.GoalAccessor;
import org.screamingsandals.lib.impl.utils.Primitives;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Arrays;

public class BukkitGoalType extends BasicWrapper<Class<?>> implements GoalType {
    private final @NotNull ResourceLocation location;

    public BukkitGoalType(@NotNull Class<?> wrappedObject, @NotNull ResourceLocation location) {
        super(wrappedObject);
        Preconditions.checkArgument(GoalAccessor.getType().isAssignableFrom(wrappedObject), "A goal type must be assignable to Goal class");
        this.location = location;
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.getSimpleName();
    }

    @Override
    public boolean applicableTo(@NotNull Entity entity) {
        var handle = ClassStorage.getHandle(entity.as(org.bukkit.entity.Entity.class));

        if (handle == null) {
            return false;
        }

        var constructors = wrappedObject.getDeclaredConstructors();
        for (var constructor : constructors) {
            if (constructor.getParameterCount() >= 1) {
                if (constructor.getParameterTypes()[0].isInstance(handle)) {
                    return true;
                }
            }

        }

        return false;
    }

    @Override
    public @Nullable Goal createGoal(@NotNull Entity entity, @NotNull Object @NotNull ... parameters) {
        var handle = ClassStorage.getHandle(entity.as(org.bukkit.entity.Entity.class));

        if (handle == null) {
            return null;
        }

        var compoundParameters = new Object[1 + parameters.length];
        compoundParameters[0] = entity;
        System.arraycopy(parameters, 0, compoundParameters, 1, parameters.length);

        var constructors = wrappedObject.getDeclaredConstructors();
        constructorLoop:
        for (var constructor : constructors) {
            if (constructor.getParameterCount() == compoundParameters.length) {
                var types = constructor.getParameterTypes();
                for (int i = 0; i < compoundParameters.length; i++) {
                    if (types[i].isPrimitive()) {
                        if (!Primitives.wrap(types[i]).isInstance(compoundParameters[i])) {
                            continue constructorLoop;
                        }
                    } else if (!types[i].isInstance(compoundParameters[i])) {
                        continue constructorLoop;
                    }
                }
                var construct = Reflect.construct(constructor, compoundParameters);
                if (construct != null) {
                    return new BukkitGoal(construct);
                }
            }

        }
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
        return location;
    }
}
