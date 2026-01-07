/*
 * Copyright 2026 ScreamingSandals
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

package org.screamingsandals.lib.impl.attribute;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistry;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;

@ProvidedService
@ApiStatus.Internal
public abstract class AttributeTypeRegistry extends SimpleRegistry<AttributeType> {
    private static @Nullable AttributeTypeRegistry registry;

    public AttributeTypeRegistry() {
        super(AttributeType.class);
        Preconditions.checkArgument(registry == null, "AttributeTypeRegistry is already initialized!");
        registry = this;
    }

    public static @NotNull AttributeTypeRegistry getInstance() {
        return Preconditions.checkNotNull(registry, "AttributeTypeRegistry is not initialized yet!");
    }

    @OnPostConstruct
    public void mapAliases() {
        mapAlias("generic.jump_strength", "horse.jump_strength");

        // 1.21.2 <-> 1.21.1
        mapAlias("generic.max_health", "max_health");
        mapAlias("generic.follow_range", "follow_range");
        mapAlias("generic.knockback_resistance", "knockback_resistance");
        mapAlias("generic.movement_speed", "movement_speed");
        mapAlias("generic.flying_speed", "flying_speed");
        mapAlias("generic.attack_damage", "attack_damage");
        mapAlias("generic.attack_knockback", "attack_knockback");
        mapAlias("generic.attack_speed", "attack_speed");
        mapAlias("generic.armor", "armor");
        mapAlias("generic.armor_toughness", "armor_toughness");
        mapAlias("generic.fall_damage_multiplier", "fall_damage_multiplier");
        mapAlias("generic.luck", "luck");
        mapAlias("generic.max_absorption", "max_absorption");
        mapAlias("generic.safe_fall_distance", "safe_fall_distance");
        mapAlias("generic.scale", "scale");
        mapAlias("generic.step_height", "step_height");
        mapAlias("generic.gravity", "gravity");
        mapAlias("generic.jump_strength", "jump_strength");
        mapAlias("generic.burning_time", "burning_time");
        mapAlias("generic.explosion_knockback_resistance", "explosion_knockback_resistance");
        mapAlias("generic.movement_efficiency", "movement_efficiency");
        mapAlias("generic.oxygen_bonus", "oxygen_bonus");
        mapAlias("generic.water_movement_efficiency", "water_movement_efficiency");
        mapAlias("player.block_interaction_range", "block_interaction_range");
        mapAlias("player.entity_interaction_range", "entity_interaction_range");
        mapAlias("player.block_break_speed", "block_break_speed");
        mapAlias("player.mining_efficiency", "mining_efficiency");
        mapAlias("player.sneaking_speed", "sneaking_speed");
        mapAlias("player.submerged_mining_speed", "submerged_mining_speed");
        mapAlias("player.sweeping_damage_ratio", "sweeping_damage_ratio");
        mapAlias("zombie.spawn_reinforcements", "spawn_reinforcements");
    }
}
