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

package org.screamingsandals.lib.bukkit.entity.type;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.bukkit.tags.KeyedUtils;
import org.screamingsandals.lib.entity.BasicEntity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.ResourceLocation;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.Location;

import java.util.Arrays;

public class BukkitEntityType extends BasicWrapper<org.bukkit.entity.EntityType> implements EntityType {
    @Getter
    private final byte additionalLegacyData;

    public BukkitEntityType(@NotNull org.bukkit.entity.EntityType wrappedObject) {
        super(wrappedObject);
        this.additionalLegacyData = 0;
    }

    @ApiStatus.Experimental // should not be used in modern environments
    public BukkitEntityType(@NotNull org.bukkit.entity.EntityType wrappedObject, byte additionalLegacyData) {
        super(wrappedObject);
        this.additionalLegacyData = additionalLegacyData;
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean isAlive() {
        return wrappedObject.isAlive();
    }

    @Override
    public boolean hasTag(@NotNull Object tag) {
        ResourceLocation key;
        if (tag instanceof ResourceLocation) {
            key = (ResourceLocation) tag;
        } else {
            key = ResourceLocation.of(tag.toString());
        }
        // native tags (while they have been implemented in 1.14, Bukkit API didn't have them until late build of 1.17.1
        if (Server.isVersion(1, 13)) {
            if (Reflect.getField(Tag.class, "REGISTRY_ENTITY_TYPES") != null) {
                KeyedUtils.isTagged(Tag.REGISTRY_ENTITY_TYPES, new NamespacedKey(key.namespace(), key.path()), org.bukkit.entity.EntityType.class, wrappedObject);
            } else if (Reflect.getField(Tag.class, "REGISTRY_ENTITIES") != null) { // Paper implemented them earlier in 1.16.5
                KeyedUtils.isTagged(Tag.REGISTRY_ENTITIES, new NamespacedKey(key.namespace(), key.path()), org.bukkit.entity.EntityType.class, wrappedObject);
            } // TODO: else bypass using NMS on CB-like servers
        }
        // backported tags
        if (!"minecraft".equals(key.namespace())) {
            return false;
        }
        var value = key.path();
        return BukkitEntityTypeRegistry.hasTagInBackPorts(wrappedObject, value);
    }

    @Override
    public boolean is(@Nullable Object entityType) {
        if (entityType instanceof org.bukkit.entity.EntityType || entityType instanceof EntityType) {
            return equals(entityType);
        }
        if (entityType instanceof String) {
            var str = (String) entityType;
            if (str.startsWith("#")) {
                // seems like a tag
                return hasTag(str.substring(1));
            }
        }
        return equals(EntityType.ofNullable(entityType));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... entityTypes) {
        return Arrays.stream(entityTypes).anyMatch(this::is);
    }

    @Override
    public @Nullable BasicEntity spawn(@NotNull Location location) {
        return Entities.spawn(this, location);
    }

    @Override
    public @NotNull ResourceLocation location() {
        if (Server.isVersion(1, 14)) {
            var namespaced = wrappedObject.getKey();
            return ResourceLocation.of(namespaced.namespace(), namespaced.getKey());
        } else {
            return translateLegacyName(wrappedObject, additionalLegacyData);
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj) && (!(obj instanceof BukkitEntityType) || ((BukkitEntityType) obj).additionalLegacyData == this.additionalLegacyData);
    }

    @ApiStatus.Experimental // should not be used in modern environments
    public static @NotNull ResourceLocation translateLegacyName(@NotNull org.bukkit.entity.EntityType type, byte additionalLegacyData) {
        @Nullable String path = null;
        switch (type.name()) {
            //<editor-fold desc="Enum constants -> 1.13 flattening names" defaultstate="collapsed">
            // @formatter:off

            case "DROPPED_ITEM": path = "item"; break;
            case "EXPERIENCE_ORB": path = "experience_orb"; break;
            case "AREA_EFFECT_CLOUD": path = "area_effect_cloud"; break;
            case "ELDER_GUARDIAN": path = "elder_guardian"; break;
            case "WITHER_SKELETON": path = "wither_skeleton"; break;
            case "STRAY": path = "stray"; break;
            case "EGG": path = "egg"; break;
            case "LEASH_HITCH": path = "leash_knot"; break;
            case "PAINTING": path = "painting"; break;
            case "ARROW": path = "arrow"; break;
            case "SNOWBALL": path = "snowball"; break;
            case "FIREBALL": path = "fireball"; break;
            case "SMALL_FIREBALL": path = "small_fireball"; break;
            case "ENDER_PEARL": path = "ender_pearl"; break;
            case "ENDER_SIGNAL": path = "eye_of_ender"; break;
            case "SPLASH_POTION": path = "potion"; break;
            case "THROWN_EXP_BOTTLE": path = "experience_bottle"; break;
            case "ITEM_FRAME": path = "item_frame"; break;
            case "WITHER_SKULL": path = "wither_skull"; break;
            case "PRIMED_TNT": path = "tnt"; break;
            case "FALLING_BLOCK": path = "falling_block"; break;
            case "FIREWORK": path = "firework_rocket"; break;
            case "HUSK": path = "husk"; break;
            case "SPECTRAL_ARROW": path = "spectral_arrow"; break;
            case "SHULKER_BULLET": path = "shulker_bullet"; break;
            case "DRAGON_FIREBALL": path = "dragon_fireball"; break;
            case "ZOMBIE_VILLAGER": path = "zombie_villager"; break;
            case "SKELETON_HORSE": path = "skeleton_horse"; break;
            case "ZOMBIE_HORSE": path = "zombie_horse"; break;
            case "ARMOR_STAND": path = "armor_stand"; break;
            case "DONKEY": path = "donkey"; break;
            case "MULE": path = "mule"; break;
            case "EVOKER_FANGS": path = "evoker_fangs"; break;
            case "EVOKER": path = "evoker"; break;
            case "VEX": path = "vex"; break;
            case "VINDICATOR": path = "vindicator"; break;
            case "ILLUSIONER": path = "illusioner"; break;
            case "MINECART_COMMAND": path = "command_block_minecart"; break;
            case "BOAT": path = "boat"; break;
            case "MINECART": path = "minecart"; break;
            case "MINECART_CHEST": path = "chest_minecart"; break;
            case "MINECART_FURNACE": path = "furnace_minecart"; break;
            case "MINECART_TNT": path = "tnt_minecart"; break;
            case "MINECART_HOPPER": path = "hopper_minecart"; break;
            case "MINECART_MOB_SPAWNER": path = "spawner_minecart"; break;
            case "CREEPER": path = "creeper"; break;
            case "SKELETON":
                path = "skeleton";
                if (!Server.isVersion(1, 11)) {
                    switch (additionalLegacyData) {
                        case InternalEntityLegacyConstants.SKELETON_VARIANT_WITHER: path = "wither_skeleton"; break;
                        case InternalEntityLegacyConstants.SKELETON_VARIANT_STRAY: path = "stray"; break;
                    }
                }
                break;
            case "SPIDER": path = "spider"; break;
            case "GIANT": path = "giant"; break;
            case "ZOMBIE":
                path = "zombie";
                if (!Server.isVersion(1, 11)) {
                    switch (additionalLegacyData) {
                        case InternalEntityLegacyConstants.ZOMBIE_VARIANT_VILLAGER: path = "zombie_villager"; break;
                        case InternalEntityLegacyConstants.ZOMBIE_VARIANT_HUSK: path = "husk"; break;
                    }
                }
                break;
            case "SLIME": path = "slime"; break;
            case "GHAST": path = "ghast"; break;
            case "PIG_ZOMBIE": path = "zombie_pigman"; break;
            case "ENDERMAN": path = "enderman"; break;
            case "CAVE_SPIDER": path = "cave_spider"; break;
            case "SILVERFISH": path = "silverfish"; break;
            case "BLAZE": path = "blaze"; break;
            case "MAGMA_CUBE": path = "magma_cube"; break;
            case "ENDER_DRAGON": path = "ender_dragon"; break;
            case "WITHER": path = "wither"; break;
            case "BAT": path = "bat"; break;
            case "WITCH": path = "witch"; break;
            case "ENDERMITE": path = "endermite"; break;
            case "GUARDIAN":
                if (!Server.isVersion(1, 11) && additionalLegacyData == InternalEntityLegacyConstants.ELDER_GUARDIAN) {
                    path = "elder_guardian";
                } else {
                    path = "guardian";
                }
                break;
            case "SHULKER": path = "shulker"; break;
            case "PIG": path = "pig"; break;
            case "SHEEP": path = "sheep"; break;
            case "COW": path = "cow"; break;
            case "CHICKEN": path = "chicken"; break;
            case "SQUID": path = "squid"; break;
            case "WOLF": path = "wolf"; break;
            case "MUSHROOM_COW": path = "mooshroom"; break;
            case "SNOWMAN": path = "snow_golem"; break;
            case "OCELOT": path = "ocelot"; break;
            case "IRON_GOLEM": path = "iron_golem"; break;
            case "HORSE":
                path = "horse";
                if (!Server.isVersion(1, 11)) {
                    switch (additionalLegacyData) {
                        case InternalEntityLegacyConstants.HORSE_VARIANT_DONKEY: path = "donkey"; break;
                        case InternalEntityLegacyConstants.HORSE_VARIANT_MULE: path = "mule"; break;
                        case InternalEntityLegacyConstants.HORSE_VARIANT_SKELETON: path = "skeleton_horse"; break;
                        case InternalEntityLegacyConstants.HORSE_VARIANT_ZOMBIE: path = "zombie_horse"; break;
                    }
                }
                break;
            case "RABBIT": path = "rabbit"; break;
            case "POLAR_BEAR": path = "polar_bear"; break;
            case "LLAMA": path = "llama"; break;
            case "LLAMA_SPIT": path = "llama_spit"; break;
            case "PARROT": path = "parrot"; break;
            case "VILLAGER": path = "villager"; break;
            case "ENDER_CRYSTAL": path = "end_crystal"; break;
            case "TURTLE": path = "turtle"; break;
            case "PHANTOM": path = "phantom"; break;
            case "TRIDENT": path = "trident"; break;
            case "COD": path = "cod"; break;
            case "SALMON": path = "salmon"; break;
            case "PUFFERFISH": path = "pufferfish"; break;
            case "TROPICAL_FISH": path = "tropical_fish"; break;
            case "DROWNED": path = "drowned"; break;
            case "DOLPHIN": path = "dolphin"; break;
            case "FISHING_HOOK": path = "fishing_bobber"; break;
            case "LIGHTNING": path = "lightning_bolt"; break;
            case "PLAYER": path = "player"; break;

            // @formatter:on
            //</editor-fold>
        }
        return ResourceLocation.of(path != null ? path : type.name());
    }
}
