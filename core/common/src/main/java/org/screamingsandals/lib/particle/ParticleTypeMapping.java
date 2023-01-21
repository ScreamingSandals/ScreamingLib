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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.ParticleTypeHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.List;

@AbstractService
public abstract class ParticleTypeMapping extends AbstractTypeMapper<ParticleTypeHolder> {
    private static @Nullable ParticleTypeMapping particleTypeMapping;

    protected final @NotNull BidirectionalConverter<ParticleTypeHolder> particleTypeConverter = BidirectionalConverter.<ParticleTypeHolder>build()
            .registerP2W(ParticleTypeHolder.class, d -> d)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return ParticleTypeHolderSerializer.INSTANCE.deserialize(ParticleTypeHolder.class, node);
                } catch (SerializationException ex) {
                    ex.printStackTrace();
                    return null;
                }
            });

    @ApiStatus.Internal
    public ParticleTypeMapping() {
        if (particleTypeMapping != null) {
            throw new UnsupportedOperationException("ParticleTypeMapping is already initialized!");
        }
        particleTypeMapping = this;
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

    @CustomAutocompletion(CustomAutocompletion.Type.PARTICLE_TYPE)
    @OfMethodAlternative(value = ParticleTypeHolder.class, methodName = "ofNullable")
    @Contract("null -> null")
    public static @Nullable ParticleTypeHolder resolve(@Nullable Object particle) {
        if (particleTypeMapping == null) {
            throw new UnsupportedOperationException("ParticleTypeMapping is not initialized yet.");
        }

        if (particle == null) {
            return null;
        }

        return particleTypeMapping.particleTypeConverter.convertOptional(particle).or(() -> particleTypeMapping.resolveFromMapping(particle)).orElse(null);
    }

    @OfMethodAlternative(value = ParticleTypeHolder.class, methodName = "all")
    public static @NotNull List<@NotNull ParticleTypeHolder> getValues() {
        if (particleTypeMapping == null) {
            throw new UnsupportedOperationException("ParticleTypeMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(particleTypeMapping.values);
    }
}
