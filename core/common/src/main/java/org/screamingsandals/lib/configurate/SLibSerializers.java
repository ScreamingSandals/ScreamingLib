/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.configurate;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.attribute.AttributeTypeHolder;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.container.type.InventoryTypeHolder;
import org.screamingsandals.lib.entity.damage.DamageCauseHolder;
import org.screamingsandals.lib.entity.pose.EntityPoseHolder;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.item.meta.PotionHolder;
import org.screamingsandals.lib.particle.ParticleTypeHolder;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.world.difficulty.DifficultyHolder;
import org.screamingsandals.lib.world.dimension.DimensionHolder;
import org.screamingsandals.lib.world.gamerule.GameRuleHolder;
import org.screamingsandals.lib.world.weather.WeatherHolder;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

@UtilityClass
public class SLibSerializers {
    @NotNull
    public TypeSerializerCollection makeSerializers(TypeSerializerCollection.@NotNull Builder builder) {
        return builder
                .register(AttributeTypeHolder.class, AttributeTypeHolderSerializer.INSTANCE)
                .register(DamageCauseHolder.class, DamageCauseHolderSerializer.INSTANCE)
                .register(DifficultyHolder.class, DifficultyHolderSerializer.INSTANCE)
                .register(DimensionHolder.class, DimensionHolderSerializer.INSTANCE)
                .register(EnchantmentHolder.class, EnchantmentHolderSerializer.INSTANCE)
                .register(EntityPoseHolder.class, EntityPoseHolderSerializer.INSTANCE)
                .register(EntityTypeHolder.class, EntityTypeHolderSerializer.INSTANCE)
                .register(GameModeHolder.class, GameModeHolderSerializer.INSTANCE)
                .register(GameRuleHolder.class, GameRuleHolderSerializer.INSTANCE)
                .register(InventoryTypeHolder.class, InventoryTypeHolderSerializer.INSTANCE)
                .register(ParticleTypeHolder.class, ParticleTypeHolderSerializer.INSTANCE)
                .register(PotionEffectHolder.class, PotionEffectHolderSerializer.INSTANCE)
                .register(PotionHolder.class, PotionHolderSerializer.INSTANCE)
                .register(WeatherHolder.class, WeatherHolderSerializer.INSTANCE)
                .register(FireworkEffectHolder.class, FireworkEffectHolderSerializer.INSTANCE)
                .register(ItemTypeHolder.class, ItemTypeHolderSerializer.INSTANCE)
                .register(BlockTypeHolder.class, BlockTypeHolderSerializer.INSTANCE)
                .register(EquipmentSlotHolder.class, EquipmentSlotHolderSerializer.INSTANCE)
                .build();
    }
}
