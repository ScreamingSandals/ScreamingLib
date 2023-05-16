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

package org.screamingsandals.lib;

import org.screamingsandals.lib.impl.block.BlockRegistry;
import org.screamingsandals.lib.container.ContainerFactory;
import org.screamingsandals.lib.impl.container.type.InventoryTypeRegistry;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.impl.entity.damage.DamageTypeRegistry;
import org.screamingsandals.lib.impl.entity.pose.EntityPoseRegistry;
import org.screamingsandals.lib.impl.entity.type.EntityTypeRegistry;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.impl.ItemBlockIdsRemapper;
import org.screamingsandals.lib.impl.entity.villager.ProfessionRegistry;
import org.screamingsandals.lib.impl.firework.FireworkEffectTypeRegistry;
import org.screamingsandals.lib.impl.gameevent.GameEventRegistry;
import org.screamingsandals.lib.impl.item.ItemTypeRegistry;
import org.screamingsandals.lib.impl.attribute.Attributes;
import org.screamingsandals.lib.impl.attribute.AttributeTypeRegistry;
import org.screamingsandals.lib.item.builder.ItemStackFactory;
import org.screamingsandals.lib.impl.firework.FireworkEffectRegistry;
import org.screamingsandals.lib.impl.item.meta.EnchantmentRegistry;
import org.screamingsandals.lib.impl.item.meta.PotionEffectRegistry;
import org.screamingsandals.lib.impl.item.meta.PotionRegistry;
import org.screamingsandals.lib.impl.particle.ParticleTypeRegistry;
import org.screamingsandals.lib.impl.slot.EquipmentSlotRegistry;
import org.screamingsandals.lib.player.Players;
import org.screamingsandals.lib.impl.player.gamemode.GameModeRegistry;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.internal.InternalCoreService;
import org.screamingsandals.lib.block.BlockPlacements;
import org.screamingsandals.lib.impl.world.Locations;
import org.screamingsandals.lib.world.Worlds;
import org.screamingsandals.lib.impl.world.chunk.Chunks;
import org.screamingsandals.lib.impl.world.difficulty.DifficultyRegistry;
import org.screamingsandals.lib.impl.world.dimension.DimensionRegistry;
import org.screamingsandals.lib.impl.block.snapshot.BlockSnapshots;
import org.screamingsandals.lib.impl.world.gamerule.GameRuleRegistry;
import org.screamingsandals.lib.impl.world.weather.WeatherRegistry;

/**
 * Main entry point of the core module.
 */
@AbstractService("org.screamingsandals.lib.impl.{platform}.{Platform}Core")
@ServiceDependencies(dependsOn = {
        Server.class,
        CustomPayload.class,
        EventManager.class,
        Tasker.class,
        EntityTypeRegistry.class,
        Entities.class,
        AttributeTypeRegistry.class,
        Attributes.class,
        FireworkEffectTypeRegistry.class,
        FireworkEffectRegistry.class,
        EnchantmentRegistry.class,
        PotionEffectRegistry.class,
        PotionRegistry.class,
        EquipmentSlotRegistry.class,
        ItemTypeRegistry.class,
        BlockRegistry.class,
        ItemBlockIdsRemapper.class,
        ItemStackFactory.class,
        Players.class,
        Locations.class,
        BlockPlacements.class,
        BlockSnapshots.class,
        DamageTypeRegistry.class,
        GameModeRegistry.class,
        InventoryTypeRegistry.class,
        EntityPoseRegistry.class,
        DifficultyRegistry.class,
        DimensionRegistry.class,
        Chunks.class,
        GameRuleRegistry.class,
        WeatherRegistry.class,
        ParticleTypeRegistry.class,
        ProfessionRegistry.class,
        GameRuleRegistry.class,
        Worlds.class,
        ContainerFactory.class,
        GameEventRegistry.class
})
@InternalCoreService
public abstract class Core {
}
