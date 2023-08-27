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

package org.screamingsandals.lib.impl.bukkit.attribute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.impl.nms.accessors.AttributeAccessor;
import org.screamingsandals.lib.impl.nms.accessors.HorseAccessor;
import org.screamingsandals.lib.impl.nms.accessors.SharedMonsterAttributesAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ZombieAccessor;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BukkitAttributeTypeRegistry1_8 extends BukkitAttributeTypeRegistry {
    public final @NotNull Map<@NotNull ResourceLocation, Object> locationToAttributes = new HashMap<>();
    public final @NotNull Map<@NotNull Object, ResourceLocation> attributesToLocation = new HashMap<>();

    public BukkitAttributeTypeRegistry1_8() {
        put("generic.max_health", SharedMonsterAttributesAccessor.getFieldMaxHealth());
        put("generic.follow_range", SharedMonsterAttributesAccessor.getFieldFOLLOW_RANGE());
        put("generic.knockback_resistance", SharedMonsterAttributesAccessor.getFieldC());
        put("generic.movement_speed", SharedMonsterAttributesAccessor.getFieldMOVEMENT_SPEED());
        put("generic.attack_damage", SharedMonsterAttributesAccessor.getFieldATTACK_DAMAGE());
        put("horse.jump_strength", HorseAccessor.getFieldAttributeJumpStrength());
        put("zombie.spawn_reinforcements", ZombieAccessor.getFieldA());
    }

    private void put(@NotNull String path, @Nullable Object attribute) {
        if (attribute != null && AttributeAccessor.getType() != null && AttributeAccessor.getType().isInstance(attribute)) {
            var loc = ResourceLocation.of("minecraft", path);
            locationToAttributes.put(loc, attribute);
            attributesToLocation.put(attribute, loc);
        }
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull AttributeType> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> locationToAttributes.entrySet().stream(),
                entry -> new BukkitAttributeType1_8(entry.getValue()),
                Map.Entry::getKey,
                (entry, s) -> entry.getKey().path().contains(s),
                (entry, s) -> "minecraft".equals(s),
                List.of()
        );
    }

    @Override
    protected @Nullable AttributeType resolveMappingPlatform(@NotNull ResourceLocation location) {
        var attribute = locationToAttributes.get(location);
        if (attribute != null) {
            return new BukkitAttributeType1_8(attribute);
        }

        return null;
    }
}
