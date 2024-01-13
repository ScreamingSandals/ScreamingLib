/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.entity.type;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.ResourceLocation;

@UtilityClass
public class InternalEntityLegacyConstants {
    public static final byte HORSE_VARIANT_DONKEY = 1;
    public static final byte HORSE_VARIANT_MULE = 2;
    public static final byte HORSE_VARIANT_SKELETON = 3;
    public static final byte HORSE_VARIANT_ZOMBIE = 4;

    public static final byte SKELETON_VARIANT_WITHER = 1;
    public static final byte SKELETON_VARIANT_STRAY = 2;

    public static final byte ZOMBIE_VARIANT_VILLAGER = 1;
    public static final byte ZOMBIE_VARIANT_HUSK = 2;

    public static final byte ELDER_GUARDIAN = 1;

    public static @NotNull ResourceLocation translateLegacyName1_11(@NotNull org.bukkit.entity.EntityType type) {
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
            case "SKELETON": path = "skeleton"; break;
            case "SPIDER": path = "spider"; break;
            case "GIANT": path = "giant"; break;
            case "ZOMBIE": path = "zombie"; break;
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
            case "GUARDIAN": path = "guardian"; break;
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
            case "HORSE": path = "horse"; break;
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

    public static @NotNull ResourceLocation translateLegacyName1_8(@NotNull org.bukkit.entity.EntityType type, byte additionalLegacyData) {
        @Nullable String path = null;
        switch (type.name()) {
            //<editor-fold desc="Enum constants+pre-1.11 backports -> 1.13 flattening names" defaultstate="collapsed">
            // @formatter:off

            case "DROPPED_ITEM": path = "item"; break;
            case "EXPERIENCE_ORB": path = "experience_orb"; break;
            case "AREA_EFFECT_CLOUD": path = "area_effect_cloud"; break;
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
            case "SPECTRAL_ARROW": path = "spectral_arrow"; break;
            case "SHULKER_BULLET": path = "shulker_bullet"; break;
            case "DRAGON_FIREBALL": path = "dragon_fireball"; break;
            case "ARMOR_STAND": path = "armor_stand"; break;
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
                switch (additionalLegacyData) {
                    case InternalEntityLegacyConstants.SKELETON_VARIANT_WITHER: path = "wither_skeleton"; break;
                    case InternalEntityLegacyConstants.SKELETON_VARIANT_STRAY: path = "stray"; break;
                }
                break;
            case "SPIDER": path = "spider"; break;
            case "GIANT": path = "giant"; break;
            case "ZOMBIE":
                path = "zombie";
                switch (additionalLegacyData) {
                    case InternalEntityLegacyConstants.ZOMBIE_VARIANT_VILLAGER: path = "zombie_villager"; break;
                    case InternalEntityLegacyConstants.ZOMBIE_VARIANT_HUSK: path = "husk"; break;
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
                if (additionalLegacyData == InternalEntityLegacyConstants.ELDER_GUARDIAN) {
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
                switch (additionalLegacyData) {
                    case InternalEntityLegacyConstants.HORSE_VARIANT_DONKEY: path = "donkey"; break;
                    case InternalEntityLegacyConstants.HORSE_VARIANT_MULE: path = "mule"; break;
                    case InternalEntityLegacyConstants.HORSE_VARIANT_SKELETON: path = "skeleton_horse"; break;
                    case InternalEntityLegacyConstants.HORSE_VARIANT_ZOMBIE: path = "zombie_horse"; break;
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
