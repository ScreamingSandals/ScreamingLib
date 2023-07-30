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

package org.screamingsandals.lib.ai.goal;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.ai.impl.goal.GoalTypeRegistry;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface GoalType extends RegistryItem, RawValueHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    boolean applicableTo(@NotNull Entity entity);

    @Nullable Goal createGoal(@NotNull Entity entity, @NotNull Object @NotNull... parameters);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.GOAL_TYPE) @Nullable Object object);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.GOAL_TYPE) @Nullable Object @NotNull... objects);

    static @NotNull GoalType of(@MinecraftType(MinecraftType.Type.GOAL_TYPE) @NotNull Object dyeColor) {
        var result = ofNullable(dyeColor);
        Preconditions.checkNotNullIllegal(result, "Could not find dye color: " + dyeColor);
        return result;
    }

    @Contract("null -> null")
    static @Nullable GoalType ofNullable(@MinecraftType(MinecraftType.Type.GOAL_TYPE) @Nullable Object dyeColor) {
        if (dyeColor instanceof GoalType) {
            return (GoalType) dyeColor;
        }
        return GoalTypeRegistry.getInstance().resolveMapping(dyeColor);
    }

    static @NotNull RegistryItemStream<@NotNull GoalType> all() {
        return GoalTypeRegistry.getInstance().getRegistryItemStream();
    }
}
