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

package org.screamingsandals.lib.configurate;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.attribute.AttributeTypeHolder;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.container.type.InventoryTypeHolder;
import org.screamingsandals.lib.entity.damage.DamageCauseHolder;
import org.screamingsandals.lib.entity.pose.EntityPose;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.item.meta.Potion;
import org.screamingsandals.lib.nbt.Tag;
import org.screamingsandals.lib.nbt.configurate.TagSerializer;
import org.screamingsandals.lib.particle.ParticleType;
import org.screamingsandals.lib.player.gamemode.GameMode;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.spectator.configurate.SpectatorSerializers;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.world.difficulty.DifficultyType;
import org.screamingsandals.lib.world.dimension.DimensionType;
import org.screamingsandals.lib.world.gamerule.GameRuleType;
import org.screamingsandals.lib.world.weather.WeatherType;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

@UtilityClass
public class SLibSerializers {
    public TypeSerializerCollection.@NotNull Builder appendSerializers(TypeSerializerCollection.@NotNull Builder builder) {
        return SpectatorSerializers.appendSerializers(builder)
                .register(Tag.class, TagSerializer.INSTANCE)
                .register(AttributeTypeHolder.class, AttributeTypeHolderSerializer.INSTANCE)
                .register(DamageCauseHolder.class, DamageCauseHolderSerializer.INSTANCE)
                .register(DifficultyType.class, DifficultyHolderSerializer.INSTANCE)
                .register(DimensionType.class, DimensionHolderSerializer.INSTANCE)
                .register(EnchantmentHolder.class, EnchantmentHolderSerializer.INSTANCE)
                .register(EntityPose.class, EntityPoseHolderSerializer.INSTANCE)
                .register(EntityType.class, EntityTypeHolderSerializer.INSTANCE)
                .register(GameMode.class, GameModeHolderSerializer.INSTANCE)
                .register(GameRuleType.class, GameRuleHolderSerializer.INSTANCE)
                .register(InventoryTypeHolder.class, InventoryTypeHolderSerializer.INSTANCE)
                .register(ParticleType.class, ParticleTypeHolderSerializer.INSTANCE)
                .register(PotionEffectHolder.class, PotionEffectHolderSerializer.INSTANCE)
                .register(Potion.class, PotionHolderSerializer.INSTANCE)
                .register(WeatherType.class, WeatherHolderSerializer.INSTANCE)
                .register(FireworkEffectHolder.class, FireworkEffectHolderSerializer.INSTANCE)
                .register(ItemTypeHolder.class, ItemTypeHolderSerializer.INSTANCE)
                .register(BlockTypeHolder.class, BlockTypeHolderSerializer.INSTANCE)
                .register(EquipmentSlot.class, EquipmentSlotHolderSerializer.INSTANCE)
                .register(ItemStack.class, ItemSerializer.INSTANCE)
                .register(Location.class, LocationHolderSerializer.INSTANCE);
    }
    public @NotNull TypeSerializerCollection makeSerializers(TypeSerializerCollection.@NotNull Builder builder) {
        return appendSerializers(builder).build();
    }
}
