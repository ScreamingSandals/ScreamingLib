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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.entity.type.EntityTypeTagBackPorts;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.key.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.utils.registry.SimpleRegistryItemStream;

import java.util.*;
import java.util.stream.Stream;

public class BukkitEntityTypeRegistry1_8 extends BukkitEntityTypeRegistry {
    public BukkitEntityTypeRegistry1_8() {
        specialType(org.bukkit.entity.EntityType.class, BukkitEntityType1_8::new);
    }

    @Override
    public void aliasMapping() {
        super.aliasMapping();
        // Tags in older versions should be resolved after all aliases are mapped because the backporting code uses flattening names which may not be used yet
        Arrays.stream(org.bukkit.entity.EntityType.values()).forEach(type -> {
            var backPorts = EntityTypeTagBackPorts.getPortedTags(new BukkitEntityType1_8(type), s -> false, false);
            if (backPorts != null && !backPorts.isEmpty()) {
                tagBackPorts.put(type, backPorts);
            }
        });
    }

    @Override
    protected @Nullable EntityType resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!"minecraft".equals(location.namespace())) {
            return null; // how am I supposed to do this?
        }

        var path = location.path();

        @Nullable String enumName = null;
        byte additionalLegacyData = 0;
        switch (path) {
            //<editor-fold desc="1.13 flattening names -> Enum constant with pre-1.11 backports" defaultstate="collapsed">
            // @formatter:off

            case "item": enumName = "DROPPED_ITEM"; break;
            case "experience_orb": enumName = "EXPERIENCE_ORB"; break;
            case "area_effect_cloud": enumName = "AREA_EFFECT_CLOUD"; break;
            case "elder_guardian":
                enumName = "GUARDIAN";
                additionalLegacyData = InternalEntityLegacyConstants.ELDER_GUARDIAN;
                break;
            case "wither_skeleton":
                enumName = "SKELETON";
                additionalLegacyData = InternalEntityLegacyConstants.SKELETON_VARIANT_WITHER;
                break;
            case "stray":
                if (Server.isVersion(1, 10)) {
                    enumName = "SKELETON";
                    additionalLegacyData = InternalEntityLegacyConstants.SKELETON_VARIANT_STRAY;
                }
                break;
            case "egg": enumName = "EGG"; break;
            case "leash_knot": enumName = "LEASH_HITCH"; break;
            case "painting": enumName = "PAINTING"; break;
            case "arrow": enumName = "ARROW"; break;
            case "snowball": enumName = "SNOWBALL"; break;
            case "fireball": enumName = "FIREBALL"; break;
            case "small_fireball": enumName = "SMALL_FIREBALL"; break;
            case "ender_pearl": enumName = "ENDER_PEARL"; break;
            case "eye_of_ender": enumName = "ENDER_SIGNAL"; break;
            case "potion": enumName = "SPLASH_POTION"; break;
            case "experience_bottle": enumName = "THROWN_EXP_BOTTLE"; break;
            case "item_frame": enumName = "ITEM_FRAME"; break;
            case "wither_skull": enumName = "WITHER_SKULL"; break;
            case "tnt": enumName = "PRIMED_TNT"; break;
            case "falling_block": enumName = "FALLING_BLOCK"; break;
            case "firework_rocket": enumName = "FIREWORK"; break;
            case "husk":
                if (Server.isVersion(1, 10)) {
                    enumName = "ZOMBIE";
                    additionalLegacyData = InternalEntityLegacyConstants.ZOMBIE_VARIANT_HUSK;
                }
                break;
            case "spectral_arrow": enumName = "SPECTRAL_ARROW"; break;
            case "shulker_bullet": enumName = "SHULKER_BULLET"; break;
            case "dragon_fireball": enumName = "DRAGON_FIREBALL"; break;
            case "zombie_villager":
                enumName = "ZOMBIE";
                additionalLegacyData = InternalEntityLegacyConstants.ZOMBIE_VARIANT_VILLAGER;
                break;
            case "skeleton_horse":
                enumName = "HORSE";
                additionalLegacyData = InternalEntityLegacyConstants.HORSE_VARIANT_SKELETON;
                break;
            case "zombie_horse":
                enumName = "HORSE";
                additionalLegacyData = InternalEntityLegacyConstants.HORSE_VARIANT_ZOMBIE;
                break;
            case "armor_stand": enumName = "ARMOR_STAND"; break;
            case "donkey":
                enumName = "HORSE";
                additionalLegacyData = InternalEntityLegacyConstants.HORSE_VARIANT_DONKEY;
                break;
            case "mule":
                enumName = "HORSE";
                additionalLegacyData = InternalEntityLegacyConstants.HORSE_VARIANT_MULE;
                break;
            case "evoker_fangs": enumName = "EVOKER_FANGS"; break;
            case "evoker": enumName = "EVOKER"; break;
            case "vex": enumName = "VEX"; break;
            case "vindicator": enumName = "VINDICATOR"; break;
            case "illusioner": enumName = "ILLUSIONER"; break;
            case "command_block_minecart": enumName = "MINECART_COMMAND"; break;
            case "boat": enumName = "BOAT"; break;
            case "minecart": enumName = "MINECART"; break;
            case "chest_minecart": enumName = "MINECART_CHEST"; break;
            case "furnace_minecart": enumName = "MINECART_FURNACE"; break;
            case "tnt_minecart": enumName = "MINECART_TNT"; break;
            case "hopper_minecart": enumName = "MINECART_HOPPER"; break;
            case "spawner_minecart": enumName = "MINECART_MOB_SPAWNER"; break;
            case "creeper": enumName = "CREEPER"; break;
            case "skeleton": enumName = "SKELETON"; break;
            case "spider": enumName = "SPIDER"; break;
            case "giant": enumName = "GIANT"; break;
            case "zombie": enumName = "ZOMBIE"; break;
            case "slime": enumName = "SLIME"; break;
            case "ghast": enumName = "GHAST"; break;
            case "zombie_pigman": enumName = "PIG_ZOMBIE"; break;
            case "enderman": enumName = "ENDERMAN"; break;
            case "cave_spider": enumName = "CAVE_SPIDER"; break;
            case "silverfish": enumName = "SILVERFISH"; break;
            case "blaze": enumName = "BLAZE"; break;
            case "magma_cube": enumName = "MAGMA_CUBE"; break;
            case "ender_dragon": enumName = "ENDER_DRAGON"; break;
            case "wither": enumName = "WITHER"; break;
            case "bat": enumName = "BAT"; break;
            case "witch": enumName = "WITCH"; break;
            case "endermite": enumName = "ENDERMITE"; break;
            case "guardian": enumName = "GUARDIAN"; break;
            case "shulker": enumName = "SHULKER"; break;
            case "pig": enumName = "PIG"; break;
            case "sheep": enumName = "SHEEP"; break;
            case "cow": enumName = "COW"; break;
            case "chicken": enumName = "CHICKEN"; break;
            case "squid": enumName = "SQUID"; break;
            case "wolf": enumName = "WOLF"; break;
            case "mooshroom": enumName = "MUSHROOM_COW"; break;
            case "snow_golem": enumName = "SNOWMAN"; break;
            case "ocelot": enumName = "OCELOT"; break;
            case "iron_golem": enumName = "IRON_GOLEM"; break;
            case "horse": enumName = "HORSE"; break;
            case "rabbit": enumName = "RABBIT"; break;
            case "polar_bear": enumName = "POLAR_BEAR"; break;
            case "llama": enumName = "LLAMA"; break;
            case "llama_spit": enumName = "LLAMA_SPIT"; break;
            case "parrot": enumName = "PARROT"; break;
            case "villager": enumName = "VILLAGER"; break;
            case "end_crystal": enumName = "ENDER_CRYSTAL"; break;
            case "turtle": enumName = "TURTLE"; break;
            case "phantom": enumName = "PHANTOM"; break;
            case "trident": enumName = "TRIDENT"; break;
            case "cod": enumName = "COD"; break;
            case "salmon": enumName = "SALMON"; break;
            case "pufferfish": enumName = "PUFFERFISH"; break;
            case "tropical_fish": enumName = "TROPICAL_FISH"; break;
            case "drowned": enumName = "DROWNED"; break;
            case "dolphin": enumName = "DOLPHIN"; break;
            case "fishing_bobber": enumName = "FISHING_HOOK"; break;
            case "lightning_bolt": enumName = "LIGHTNING"; break;
            case "player": enumName = "PLAYER"; break;

            // @formatter:on
            //</editor-fold>
        }

        if (enumName != null) {
            try {
                return new BukkitEntityType1_8(org.bukkit.entity.EntityType.valueOf(enumName), additionalLegacyData);
            } catch (IllegalArgumentException ignored) {
            }
        }

        // Not found: treat the path of minecraft: location as the enum constant name and try it again

        if (!"minecraft".equals(location.namespace())) {
            return null;
        }

        try {
            return new BukkitEntityType1_8(org.bukkit.entity.EntityType.valueOf(location.path().toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException ignored) {
        }

        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull EntityType> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Arrays.stream(org.bukkit.entity.EntityType.values()).filter(e -> e != org.bukkit.entity.EntityType.UNKNOWN).flatMap(type1 -> {
                    switch (type1) {
                        case ZOMBIE:
                            if (Server.isVersion(1, 10)) {
                                return Stream.of(
                                        Pair.of(type1, 0),
                                        Pair.of(type1, InternalEntityLegacyConstants.ZOMBIE_VARIANT_VILLAGER),
                                        Pair.of(type1, InternalEntityLegacyConstants.ZOMBIE_VARIANT_HUSK)
                                );
                            } else {
                                return Stream.of(
                                        Pair.of(type1, 0),
                                        Pair.of(type1, InternalEntityLegacyConstants.ZOMBIE_VARIANT_VILLAGER)
                                );
                            }
                        case SKELETON:
                            if (Server.isVersion(1, 10)) {
                                return Stream.of(
                                        Pair.of(type1, 0),
                                        Pair.of(type1, InternalEntityLegacyConstants.SKELETON_VARIANT_WITHER),
                                        Pair.of(type1, InternalEntityLegacyConstants.SKELETON_VARIANT_STRAY)
                                );
                            } else {
                                return Stream.of(
                                        Pair.of(type1, 0),
                                        Pair.of(type1, InternalEntityLegacyConstants.SKELETON_VARIANT_WITHER)
                                );
                            }
                        case HORSE:
                            return Stream.of(
                                    Pair.of(type1, 0),
                                    Pair.of(type1, InternalEntityLegacyConstants.HORSE_VARIANT_DONKEY),
                                    Pair.of(type1, InternalEntityLegacyConstants.HORSE_VARIANT_MULE),
                                    Pair.of(type1, InternalEntityLegacyConstants.HORSE_VARIANT_SKELETON),
                                    Pair.of(type1, InternalEntityLegacyConstants.HORSE_VARIANT_ZOMBIE)
                            );
                        case GUARDIAN:
                            return Stream.of(Pair.of(type1, 0), Pair.of(type1, InternalEntityLegacyConstants.ELDER_GUARDIAN));
                    }
                    return Stream.of(Pair.of(type1, 0));
                }),
                pair -> new BukkitEntityType1_8(pair.first(), pair.second().byteValue()),
                pair -> InternalEntityLegacyConstants.translateLegacyName1_8(pair.first(), pair.second().byteValue()),
                (pair, literal) -> InternalEntityLegacyConstants.translateLegacyName1_8(pair.first(), pair.second().byteValue()).path().contains(literal),
                (pair, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }
}
