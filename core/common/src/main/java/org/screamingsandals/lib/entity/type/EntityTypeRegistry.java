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

package org.screamingsandals.lib.entity.type;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.registry.SimpleRegistry;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class EntityTypeRegistry extends SimpleRegistry<EntityType> {
    private static @Nullable EntityTypeRegistry registry;

    @ApiStatus.Internal
    public EntityTypeRegistry() {
        super(EntityType.class);
        Preconditions.checkArgument(registry == null, "EntityTypeMapping is already initialized!");
        registry = this;
    }

    public static @NotNull EntityTypeRegistry getInstance() {
        return Preconditions.checkNotNull(registry, "EntityTypeRegistry is not initialized yet!");
    }

    @OnPostConstruct
    public void aliasMapping() {
        // Newer flattening <-> Older flattening
        mapAlias("ZOMBIFIED_PIGLIN", "ZOMBIE_PIGMAN");

        // Flattening <-> Bukkit
        mapAlias("ITEM", "DROPPED_ITEM");
        mapAlias("LEASH_KNOT", "LEASH_HITCH");
        mapAlias("EYE_OF_ENDER", "ENDER_SIGNAL");
        mapAlias("POTION", "SPLASH_POTION");
        mapAlias("EXPERIENCE_BOTTLE", "THROWN_EXP_BOTTLE");
        mapAlias("TNT", "PRIMED_TNT");
        mapAlias("FIREWORK_ROCKET", "FIREWORK");
        mapAlias("COMMAND_BLOCK_MINECART", "MINECART_COMMAND");
        mapAlias("CHEST_MINECART", "MINECART_CHEST");
        mapAlias("FURNACE_MINECART", "MINECART_FURNACE");
        mapAlias("TNT_MINECART", "MINECART_TNT");
        mapAlias("HOPPER_MINECART", "MINECART_HOPPER");
        mapAlias("SPAWNER_MINECART", "MINECART_MOB_SPAWNER");
        mapAlias("MOOSHROOM", "MUSHROOM_COW");
        mapAlias("FISHING_BOBBER", "FISHING_HOOK");
        mapAlias("LIGHTNING_BOLT", "LIGHTNING");

        // Flattening <-> Legacy (may also be Bukkit name)
        mapAlias("EXPERIENCE_ORB", "XP_ORB");
        mapAlias("EXPERIENCE_BOTTLE", "XP_BOTTLE");
        mapAlias("EYE_OF_ENDER", "EYE_OF_ENDER_SIGNAL");
        mapAlias("END_CRYSTAL", "ENDER_CRYSTAL");
        mapAlias("FIREWORK_ROCKET", "FIREWORKS_ROCKET");
        mapAlias("COMMAND_BLOCK_MINECART", "COMMANDBLOCK_MINECART");
        mapAlias("SNOW_GOLEM", "SNOWMAN");
        mapAlias("IRON_GOLEM", "VILLAGER_GOLEM");
        mapAlias("EVOKER_FANGS", "EVOCATION_FANGS");
        mapAlias("EVOKER", "EVOCATION_ILLAGER");
        mapAlias("VINDICATOR", "VINDICATION_ILLAGER");
        mapAlias("ILLUSIONER", "ILLUSION_ILLAGER");

        // post-1.11 resource-location name <-> pre-1.11 non-resource-location name
        mapAlias("AREA_EFFECT_CLOUD", "AreaEffectCloud");
        mapAlias("ARMOR_STAND", "ArmorStand");
        mapAlias("CAVE_SPIDER", "CaveSpider");
        mapAlias("CHEST_MINECART", "MinecartChest");
        mapAlias("COMMANDBLOCK_MINECART", "MinecartCommandBlock");
        mapAlias("DRAGON_FIREBALL", "DragonFireball");
        mapAlias("EGG", "ThrownEgg");
        mapAlias("ENDER_CRYSTAL", "EnderCrystal");
        mapAlias("ENDER_DRAGON", "EnderDragon");
        mapAlias("ENDER_PEARL", "ThrownEnderpearl");
        mapAlias("EYE_OF_ENDER_SIGNAL", "EyeOfEnderSignal");
        mapAlias("FALLING_BLOCK", "FallingSand");
        mapAlias("FIREWORKS_ROCKET", "FireworksRocketEntity");
        mapAlias("FURNACE_MINECART", "MinecartFurnace");
        mapAlias("HOPPER_MINECART", "MinecartHopper");
        mapAlias("HORSE", "EntityHorse");
        mapAlias("ITEM_FRAME", "ItemFrame");
        mapAlias("LEASH_KNOT", "LeashKnot");
        mapAlias("LIGHTNING_BOLT", "LightningBolt");
        mapAlias("MAGMA_CUBE", "LavaSlime");
        mapAlias("MINECART", "MinecartRideable");
        mapAlias("MOOSHROOM", "MushroomCow");
        mapAlias("OCELOT", "Ozelot");
        mapAlias("POLAR_BEAR", "PolarBear");
        mapAlias("SHULKER_BULLET", "ShulkerBullet");
        mapAlias("SMALL_FIREBALL", "SmallFireball");
        mapAlias("SPECTRAL_ARROW", "SpectralArrow");
        mapAlias("POTION", "ThrownPotion");
        mapAlias("SPAWNER_MINECART", "MinecartSpawner");
        mapAlias("TNT", "PrimedTnt");
        mapAlias("TNT_MINECART", "MinecartTNT");
        mapAlias("VILLAGER_GOLEM", "VillagerGolem");
        mapAlias("WITHER", "WitherBoss");
        mapAlias("WITHER_SKULL", "WitherSkull");
        mapAlias("XP_BOTTLE", "ThrownExpBottle");
        mapAlias("XP_ORB", "XPOrb");
        mapAlias("ZOMBIE_PIGMAN", "PigZombie");
    }
}
