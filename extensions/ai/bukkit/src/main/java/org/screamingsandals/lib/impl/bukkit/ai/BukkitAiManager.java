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

package org.screamingsandals.lib.impl.bukkit.ai;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.ai.AiManager;
import org.screamingsandals.lib.ai.GoalSelector;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.impl.bukkit.ai.goal.BukkitGoalTypeRegistry;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.nms.accessors.MobAccessor;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;

@Service
@ServiceDependencies(dependsOn = {
        BukkitGoalTypeRegistry.class
})
public class BukkitAiManager extends AiManager {
    @Override
    protected @Nullable GoalSelector goalSelector0(@NotNull Entity entity) {
        var handle = ClassStorage.getHandle(entity.as(org.bukkit.entity.Entity.class));
        if (handle != null && MobAccessor.TYPE.get().isInstance(handle)) {
            return new BukkitGoalSelector(handle);
        }
        return null;
    }
}
