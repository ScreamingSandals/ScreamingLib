package org.screamingsandals.lib;

import org.screamingsandals.lib.block.BlockTypeMapper;
import org.screamingsandals.lib.container.type.InventoryTypeMapping;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.damage.DamageCauseMapping;
import org.screamingsandals.lib.entity.pose.EntityPoseMapping;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.item.ItemTypeMapper;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.attribute.AttributeTypeMapping;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.firework.FireworkEffectMapping;
import org.screamingsandals.lib.item.meta.EnchantmentMapping;
import org.screamingsandals.lib.item.meta.PotionEffectMapping;
import org.screamingsandals.lib.item.meta.PotionMapping;
import org.screamingsandals.lib.particle.ParticleTypeMapping;
import org.screamingsandals.lib.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.gamemode.GameModeMapping;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.internal.InternalCoreService;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.world.chunk.ChunkMapper;
import org.screamingsandals.lib.world.difficulty.DifficultyMapping;
import org.screamingsandals.lib.world.dimension.DimensionMapping;
import org.screamingsandals.lib.block.state.BlockStateMapper;
import org.screamingsandals.lib.world.gamerule.GameRuleMapping;
import org.screamingsandals.lib.world.weather.WeatherMapping;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<className>.+)$",
        replaceRule = "{basePackage}.{platform}.{Platform}{className}"
)
@ServiceDependencies(dependsOn = {
        Server.class,
        EventManager.class,
        EntityTypeMapping.class,
        EntityMapper.class,
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
        ItemFactory.class,
        PlayerMapper.class,
        LocationMapper.class,
        BlockMapper.class,
        BlockStateMapper.class,
        DamageCauseMapping.class,
        GameModeMapping.class,
        InventoryTypeMapping.class,
        EntityPoseMapping.class,
        DifficultyMapping.class,
        DimensionMapping.class,
        ChunkMapper.class,
        GameRuleMapping.class,
        WeatherMapping.class,
        ParticleTypeMapping.class,
        GameRuleMapping.class
})
@InternalCoreService
public abstract class Core {
}
