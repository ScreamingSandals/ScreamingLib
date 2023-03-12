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

package org.screamingsandals.lib.particle;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.registry.SimpleRegistry;

@AbstractService
@ApiStatus.Internal
public abstract class ParticleTypeRegistry extends SimpleRegistry<ParticleType> {
    private static @Nullable ParticleTypeRegistry registry;

    public ParticleTypeRegistry() {
        super(ParticleType.class);
        Preconditions.checkArgument(registry == null, "ParticleTypeRegistry is already initialized!");
        registry = this;
    }

    public static @NotNull ParticleTypeRegistry getInstance() {
        return Preconditions.checkNotNull(registry, "ParticleTypeRegistry is not initialized yet!");
    }

    @OnPostConstruct
    public void mapMinecraftToBukkit() {
        mapAlias("AMBIENT_ENTITY_EFFECT", "SPELL_MOB_AMBIENT");
        mapAlias("ANGRY_VILLAGER", "VILLAGER_ANGRY");
        mapAlias("BLOCK", "BLOCK_CRACK");
        mapAlias("BLOCK", "BLOCK_DUST");
        mapAlias("BUBBLE", "WATER_BUBBLE");
        mapAlias("DRIPPING_LAVA", "DRIP_LAVA");
        mapAlias("DRIPPING_WATER", "DRIP_WATER");
        mapAlias("DUST", "REDSTONE");
        mapAlias("EFFECT", "SPELL");
        mapAlias("ELDER_GUARDIAN", "MOB_APPEARANCE");
        mapAlias("ENCHANT", "ENCHANTMENT_TABLE");
        mapAlias("ENCHANTED_HIT", "CRIT_MAGIC");
        mapAlias("ENTITY_EFFECT", "SPELL_MOB");
        mapAlias("EXPLOSION", "EXPLOSION_LARGE");
        mapAlias("EXPLOSION_EMITTER", "EXPLOSION_HUGE");
        mapAlias("FIREWORK", "FIREWORKS_SPARK");
        mapAlias("FISHING", "WATER_WAKE");
        mapAlias("HAPPY_VILLAGER", "VILLAGER_HAPPY");
        mapAlias("INSTANT_EFFECT", "SPELL_INSTANT");
        mapAlias("ITEM", "ITEM_CRACK");
        mapAlias("ITEM_SLIME", "SLIME");
        mapAlias("ITEM_SNOWBALL", "SNOWBALL");
        mapAlias("ITEM_SNOWBALL", "SNOW_SHOVEL");
        mapAlias("LARGE_SMOKE", "SMOKE_LARGE");
        mapAlias("MYCELIUM", "TOWN_AURA");
        mapAlias("POOF", "EXPLOSION_NORMAL");
        mapAlias("RAIN", "WATER_DROP");
        mapAlias("SMOKE", "SMOKE_NORMAL");
        mapAlias("SPLASH", "WATER_SPLASH");
        mapAlias("TOTEM_OF_UNDYING", "TOTEM");
        mapAlias("UNDERWATER", "SUSPENDED");
        mapAlias("UNDERWATER", "SUSPENDED_DEPTH");
        mapAlias("WITCH", "SPELL_WITCH");

        // REMOVED
        mapAlias("TAKE", "ITEM_TAKE");
    }
}
