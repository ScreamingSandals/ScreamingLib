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
import org.screamingsandals.lib.container.type.InventoryTypeMapping;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.entity.damage.DamageCauseMapping;
import org.screamingsandals.lib.entity.pose.EntityPoseMapping;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.item.ItemTypeMapper;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.attribute.AttributeTypeMapping;
import org.screamingsandals.lib.item.builder.ItemStackFactory;
import org.screamingsandals.lib.firework.FireworkEffectMapping;
import org.screamingsandals.lib.item.meta.EnchantmentMapping;
import org.screamingsandals.lib.item.meta.PotionEffectMapping;
import org.screamingsandals.lib.item.meta.PotionMapping;
import org.screamingsandals.lib.particle.ParticleTypeMapping;
import org.screamingsandals.lib.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.player.Players;
import org.screamingsandals.lib.player.gamemode.GameModeMapping;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.internal.InternalCoreService;
import org.screamingsandals.lib.block.Blocks;
import org.screamingsandals.lib.world.Locations;
import org.screamingsandals.lib.world.Worlds;
import org.screamingsandals.lib.world.chunk.Chunks;
import org.screamingsandals.lib.world.difficulty.DifficultyMapping;
import org.screamingsandals.lib.world.dimension.DimensionMapping;
import org.screamingsandals.lib.block.state.BlockSnapshots;
import org.screamingsandals.lib.world.gamerule.GameRuleMapping;
import org.screamingsandals.lib.world.weather.WeatherMapping;

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
        EntityTypeMapping.class,
        Entities.class,
        AttributeTypeMapping.class,
        AttributeMapping.class,
        FireworkEffectMapping.class,
        EnchantmentMapping.class,
        PotionEffectMapping.class,
        PotionMapping.class,
        EquipmentSlotMapping.class,
        ItemTypeMapper.class,
        BlockTypeMapper.class,
        ItemBlockIdsRemapper.class,
        ItemStackFactory.class,
        Players.class,
        Locations.class,
        Blocks.class,
        BlockSnapshots.class,
        DamageCauseMapping.class,
        GameModeMapping.class,
        InventoryTypeMapping.class,
        EntityPoseMapping.class,
        DifficultyMapping.class,
        DimensionMapping.class,
        Chunks.class,
        GameRuleMapping.class,
        WeatherMapping.class,
        ParticleTypeMapping.class,
        GameRuleMapping.class,
        Worlds.class,
        ContainerFactory.class
})
@InternalCoreService
public abstract class Core {
}
