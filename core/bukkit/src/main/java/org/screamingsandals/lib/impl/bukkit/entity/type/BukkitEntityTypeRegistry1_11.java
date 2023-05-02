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

package org.screamingsandals.lib.impl.bukkit.entity.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.impl.entity.type.EntityTypeTagBackPorts;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;

import java.util.*;

@Service
public class BukkitEntityTypeRegistry1_11 extends BukkitEntityTypeRegistry {
    public BukkitEntityTypeRegistry1_11() {
        specialType(org.bukkit.entity.EntityType.class, BukkitEntityType1_11::new);
    }

    @Override
    public void aliasMapping() {
        super.aliasMapping();
        // Tags in older versions should be resolved after all aliases are mapped because the backporting code uses flattening names which may not be used yet
        Arrays.stream(org.bukkit.entity.EntityType.values()).forEach(type -> {
            var backPorts = EntityTypeTagBackPorts.getPortedTags(new BukkitEntityType1_11(type), s -> false, false);
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
        switch (path) {
            //<editor-fold desc="1.13 flattening names -> Enum constant" defaultstate="collapsed">
            // @formatter:off

            case "item": enumName = "DROPPED_ITEM"; break;
            case "experience_orb": enumName = "EXPERIENCE_ORB"; break;
            case "area_effect_cloud": enumName = "AREA_EFFECT_CLOUD"; break;
            case "elder_guardian": enumName = "ELDER_GUARDIAN"; break;
            case "wither_skeleton": enumName = "WITHER_SKELETON"; break;
            case "stray": enumName = "STRAY"; break;
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
            case "husk": enumName = "HUSK"; break;
            case "spectral_arrow": enumName = "SPECTRAL_ARROW"; break;
            case "shulker_bullet": enumName = "SHULKER_BULLET"; break;
            case "dragon_fireball": enumName = "DRAGON_FIREBALL"; break;
            case "zombie_villager": enumName = "ZOMBIE_VILLAGER"; break;
            case "skeleton_horse": enumName = "SKELETON_HORSE"; break;
            case "zombie_horse": enumName = "ZOMBIE_HORSE"; break;
            case "armor_stand": enumName = "ARMOR_STAND"; break;
            case "donkey": enumName = "DONKEY"; break;
            case "mule": enumName = "MULE"; break;
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
                return new BukkitEntityType1_11(org.bukkit.entity.EntityType.valueOf(enumName));
            } catch (IllegalArgumentException ignored) {
            }
        }

        // Not found: treat the path of minecraft: location as the enum constant name and try it again

        if (!"minecraft".equals(location.namespace())) {
            return null;
        }

        try {
            return new BukkitEntityType1_11(org.bukkit.entity.EntityType.valueOf(path.toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException ignored) {
        }

        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull EntityType> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Arrays.stream(org.bukkit.entity.EntityType.values()).filter(e -> e != org.bukkit.entity.EntityType.UNKNOWN),
                BukkitEntityType1_11::new,
                InternalEntityLegacyConstants::translateLegacyName1_11,
                (entityType, literal) -> InternalEntityLegacyConstants.translateLegacyName1_11(entityType).path().contains(literal),
                (entityType, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }
}
