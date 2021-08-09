package org.screamingsandals.lib;

import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.damage.DamageCauseMapping;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.attribute.AttributeTypeMapping;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.firework.FireworkEffectMapping;
import org.screamingsandals.lib.material.meta.EnchantmentMapping;
import org.screamingsandals.lib.material.meta.PotionEffectMapping;
import org.screamingsandals.lib.material.meta.PotionMapping;
import org.screamingsandals.lib.material.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.gamemode.GameModeMapping;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.internal.InternalCoreService;
import org.screamingsandals.lib.world.BlockDataMapper;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.world.state.BlockStateMapper;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<className>.+)$",
        replaceRule = "{basePackage}.{platform}.{Platform}{className}"
)
@ServiceDependencies(dependsOn = {
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
        MaterialMapping.class,
        ItemFactory.class,
        PlayerMapper.class,
        LocationMapper.class,
        BlockMapper.class,
        BlockDataMapper.class,
        BlockStateMapper.class,
        DamageCauseMapping.class,
        GameModeMapping.class
})
@InternalCoreService
public abstract class Core {
}
