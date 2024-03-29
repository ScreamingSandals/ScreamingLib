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

package org.screamingsandals.lib.ai;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Core;
import org.screamingsandals.lib.ai.impl.goal.GoalTypeRegistry;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;

@AbstractService("org.screamingsandals.lib.impl.{platform}.ai.{Platform}AiManager")
@ServiceDependencies(dependsOn = {
        Core.class,
        GoalTypeRegistry.class
})
public abstract class AiManager {
    private static @Nullable AiManager manager;

    @ApiStatus.Internal
    public AiManager() {
        Preconditions.checkArgument(manager == null, "AiManager is already initialized.");
        manager = this;
    }

    @Contract("null -> null")
    public static @Nullable GoalSelector goalSelector(@Nullable Entity entity) {
        if (entity == null) {
            return null;
        }
        Preconditions.checkArgument(manager != null, "AiManager is not initialized yet.");
        return manager.goalSelector0(entity);
    }

    protected abstract @Nullable GoalSelector goalSelector0(@NotNull Entity entity);
}
