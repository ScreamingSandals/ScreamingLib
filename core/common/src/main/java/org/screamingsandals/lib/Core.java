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

import org.screamingsandals.lib.block.BlockTypeMapper;
import org.screamingsandals.lib.container.ContainerFactory;
import org.screamingsandals.lib.container.type.InventoryTypeRegistry;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.entity.damage.DamageTypeRegistry;
import org.screamingsandals.lib.entity.pose.EntityPoseRegistry;
import org.screamingsandals.lib.entity.type.EntityTypeRegistry;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.item.ItemTypeMapper;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.attribute.AttributeTypeRegistry;
import org.screamingsandals.lib.item.builder.ItemStackFactory;
import org.screamingsandals.lib.firework.FireworkEffectMapping;
import org.screamingsandals.lib.item.meta.EnchantmentMapping;
import org.screamingsandals.lib.item.meta.PotionEffectMapping;
import org.screamingsandals.lib.item.meta.PotionRegistry;
import org.screamingsandals.lib.particle.ParticleTypeRegistry;
import org.screamingsandals.lib.slot.EquipmentSlotRegistry;
import org.screamingsandals.lib.player.Players;
import org.screamingsandals.lib.player.gamemode.GameModeRegistry;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.internal.InternalCoreService;
import org.screamingsandals.lib.block.Blocks;
import org.screamingsandals.lib.world.Locations;
import org.screamingsandals.lib.world.Worlds;
import org.screamingsandals.lib.world.chunk.Chunks;
import org.screamingsandals.lib.world.difficulty.DifficultyRegistry;
import org.screamingsandals.lib.world.dimension.DimensionRegistry;
import org.screamingsandals.lib.block.state.BlockSnapshots;
import org.screamingsandals.lib.world.gamerule.GameRuleRegistry;
import org.screamingsandals.lib.world.weather.WeatherRegistry;

/**
 * Main entry point of the core module.
 */
@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<className>.+)$",
        replaceRule = "{basePackage}.{platform}.{Platform}{className}"
)
@ServiceDependencies(dependsOn = {
        Server.class,
        CustomPayload.class,
        EventManager.class,
        Tasker.class,
        EntityTypeRegistry.class,
        Entities.class,
        AttributeTypeRegistry.class,
        AttributeMapping.class,
        FireworkEffectMapping.class,
        EnchantmentMapping.class,
        PotionEffectMapping.class,
        PotionRegistry.class,
        EquipmentSlotRegistry.class,
        ItemTypeMapper.class,
        BlockTypeMapper.class,
        ItemBlockIdsRemapper.class,
        ItemStackFactory.class,
        Players.class,
        Locations.class,
        Blocks.class,
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
        GameRuleRegistry.class,
        Worlds.class,
        ContainerFactory.class
})
@InternalCoreService
public abstract class Core {
}
