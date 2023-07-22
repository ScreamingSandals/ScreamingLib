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

package org.screamingsandals.lib.impl.paper.ai.goal;

import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.VanillaGoal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.ai.goal.GoalType;
import org.screamingsandals.lib.impl.bukkit.ai.goal.AbstractBukkitGoalTypeRegistry;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaperGoalTypeRegistry extends AbstractBukkitGoalTypeRegistry {
    private final @NotNull Map<@NotNull ResourceLocation, PaperGoalType> LOCATION_KEY_MAP = new HashMap<>();

    public PaperGoalTypeRegistry() {
        for (var field : VanillaGoal.class.getDeclaredFields()) {
            if (field.getAnnotation(Deprecated.class) == null && !field.isSynthetic() && Modifier.isPublic(field.getModifiers()) && GoalKey.class.isAssignableFrom(field.getType())) {
                var key = (GoalKey<?>) Reflect.getField(field);
                if (key != null) {
                    var location = ResourceLocation.of(key.getNamespacedKey().getNamespace(), key.getNamespacedKey().getKey());
                    LOCATION_KEY_MAP.computeIfAbsent(location, k -> new PaperGoalType(key));
                }
            }
        }
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull GoalType> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                LOCATION_KEY_MAP.entrySet()::stream,
                Map.Entry::getValue,
                Map.Entry::getKey,
                (v, s) -> v.getKey().path().contains(s),
                (v, s) -> v.getKey().namespace().equals(s),
                List.of()
        );
    }

    @Override
    protected @Nullable GoalType resolveMappingPlatform(@NotNull ResourceLocation location) {
        return LOCATION_KEY_MAP.get(location);
    }
}
