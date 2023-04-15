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
import org.screamingsandals.lib.attribute.AttributeModifier;
import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.attribute.ItemAttribute;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.container.type.InventoryType;
import org.screamingsandals.lib.entity.damage.DamageType;
import org.screamingsandals.lib.entity.pose.EntityPose;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.firework.FireworkEffect;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.item.meta.Enchantment;
import org.screamingsandals.lib.item.meta.PotionEffect;
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
                .register(AttributeModifier.class, AttributeModifierSerializer.INSTANCE)
                .register(AttributeType.class, AttributeTypeSerializer.INSTANCE)
                .register(DamageType.class, DamageTypeSerializer.INSTANCE)
                .register(DifficultyType.class, DifficultyTypeSerializer.INSTANCE)
                .register(DimensionType.class, DimensionTypeSerializer.INSTANCE)
                .register(Enchantment.class, EnchantmentSerializer.INSTANCE)
                .register(EntityPose.class, EntityPoseSerializer.INSTANCE)
                .register(EntityType.class, EntityTypeSerializer.INSTANCE)
                .register(GameMode.class, GameModeSerializer.INSTANCE)
                .register(GameRuleType.class, GameRuleTypeSerializer.INSTANCE)
                .register(InventoryType.class, InventoryTypeSerializer.INSTANCE)
                .register(ParticleType.class, ParticleTypeSerializer.INSTANCE)
                .register(PotionEffect.class, PotionEffectSerializer.INSTANCE)
                .register(Potion.class, PotionSerializer.INSTANCE)
                .register(WeatherType.class, WeatherTypeSerializer.INSTANCE)
                .register(FireworkEffect.class, FireworkEffectSerializer.INSTANCE)
                .register(ItemType.class, ItemTypeSerializer.INSTANCE)
                .register(Block.class, BlockTypeSerializer.INSTANCE)
                .register(EquipmentSlot.class, EquipmentSlotSerializer.INSTANCE)
                .register(ItemStack.class, ItemSerializer.INSTANCE)
                .register(Location.class, LocationSerializer.INSTANCE)
                .register(ItemAttribute.class, ItemAttributeSerializer.INSTANCE);
    }
    public @NotNull TypeSerializerCollection makeSerializers(TypeSerializerCollection.@NotNull Builder builder) {
        return appendSerializers(builder).build();
    }
}
