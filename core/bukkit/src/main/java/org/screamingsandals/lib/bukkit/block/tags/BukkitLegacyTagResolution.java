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

package org.screamingsandals.lib.bukkit.block.tags;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

// because we support legacy only on bukkit and this doesn't affect flattening versions, we don't need it in common module
@UtilityClass
public class BukkitLegacyTagResolution {
    public static @NotNull List<String> constructTags(@NotNull Material material) {
        var list = new ArrayList<String>();
        /* tags related to features released after 1.12.2 are not backported
         unless the tag contains older block and can be useful in older versions or for better compatibility (beds, *_ores, etc.) */

        // this list is based on the MinecraftWiki page: https://minecraft.fandom.com/wiki/Tag
        switch (material.name()) {
            // TODO: mineable/axe
            // TODO: mineable/hoe
            // TODO: mineable/pickaxe
            // TODO: mineable/shovel

            // TODO: needs_diamond_tool
            // TODO: needs_iron_tool
            // TODO: needs_stone_tool

            // TODO: acacia_logs
            // TODO: spruce_logs
            // TODO: birch_logs
            // TODO: dark_oak_logs
            // TODO: jungle_logs
            // TODO: oak_logs

            // TODO: add dirt:3 to mushroom_grow_block

            // animals_spawnable_on
            case "GRASS":
                list.add("animals_spawnable_on");
                list.add("dirt"); // also part of dirt
                list.add("rabbits_spawnable_on"); // also part of rabbits_spawnable_on
                list.add("valid_spawn"); // also part of valid_spawn
                list.add("wolves_spawnable_on"); // also part of wolves_spawnable_on
                list.add("enderman_holdable"); // also part of enderman_holdable
                break;
            // anvil
            case "ANVIL":
                list.add("anvil");
                break;
            // banners
            case "STANDING_BANNER":
            case "WALL_BANNER":
                list.add("banners");
                list.add("wall_post_override"); // also part of wall_post_override
                break;
            // base_stone_nether
            case "NETHERRACK":
                list.add("base_stone_nether");
                list.add("infiniburn_overworld"); // also part of infiniburn_overworld
                list.add("infiniburn_end"); // also part of infiniburn_end
                list.add("infiniburn_nether"); // also part of infiniburn_nether
                list.add("enderman_holdable"); // also part of enderman_holdable
                break;
            // base_stone_overworld
            case "STONE":
                list.add("base_stone_overworld");
                break;
            // beacon_base_blocks
            case "EMERALD_BLOCK":
            case "DIAMOND_BLOCK":
            case "IRON_BLOCK":
            case "GOLD_BLOCK":
                list.add("beacon_base_blocks");
                break;
            // beds
            case "BED_BLOCK":
                list.add("beds");
                break;
            // buttons (includes wooden_buttons)
            case "STONE_BUTTON":
                list.add("buttons");
                break;
            // cauldrons
            case "CAULDRON":
                list.add("cauldrons");
                break;
            // cave_vines
            case "VINE":
                list.add("vine");
                // vine is also climbable
            // climbable
            case "LADDER":
                list.add("climbable");
                list.add("fall_damage_resetting"); // also part of fall_damage_resetting
                break;
            // coal_ores
            case "COAL_ORE":
                list.add("coal_ores");
                break;
            // completes_find_tree_tutorial (includes logs, leaves)
            // crops
            case "BEETROOT_BLOCK":
            case "CARROT":
            case "POTATO":
            case "CROPS":
            case "MELON_STEM":
            case "PUMPKIN_STEM":
                list.add("crops");
                break;
            // dead_bush_may_place_on (includes sand, terracotta and dirt)
            // diamond_ores
            case "DIAMOND_ORE":
                list.add("diamond_ores");
                break;
            // dirt
            case "DIRT":
                list.add("dirt");
                list.add("dead_bush_may_place_on");
                list.add("enderman_holdable"); // also part of enderman_holdable
                break;
            // doors (includes wooden_doors)
            case "IRON_DOOR_BLOCK":
                list.add("doors");
                break;
            // dragon_immune
            // wither_immune
            case "BARRIER":
            case "ENDER_PORTAL_FRAME":
            case "COMMAND":
            case "COMMAND_REPEATING":
            case "COMMAND_CHAIN":
            case "STRUCTURE_BLOCK":
            case "PISTON_MOVING_PIECE":
                list.add("wither_immune");
            case "OBSIDIAN":
            case "ENDER_STONE":
            case "IRON_FENCE":
                list.add("dragon_immune");
                break;
            // dragon_transparent
            // emerald_ores
            case "EMERALD_ORE":
                list.add("emerald_ores");
                break;
            // enderman_holdable
            case "CACTUS":
            case "CLAY":
            case "GRAVEL":
            case "MELON_BLOCK":
            case "PUMPKIN":
            case "BROWN_MUSHROOM":
            case "RED_MUSHROOM":
            case "TNT":
                list.add("enderman_holdable");
                break;
            // fall_damage_resetting (includes climbable)
            case "WEB":
                list.add("fall_damage_resetting");
                break;
            // fence_gates
            case "FENCE_GATE":
            case "SPRUCE_FENCE_GATE":
            case "BIRCH_FENCE_GATE":
            case "JUNGLE_FENCE_GATE":
            case "DARK_OAK_FENCE_GATE":
            case "ACACIA_FENCE_GATE":
                list.add("fence_gates");
                break;
            // fences (includes wooden_fences)
            case "NETHER_FENCE":
                list.add("fences");
                break;
            // fire
            case "FIRE":
                list.add("fire");
                list.add("dragon_transparent"); // also part of dragon_transparent
                list.add("wither_immune"); // also part of wither_immune
                break;
            // flower_pots
            case "FLOWER_POT":
                list.add("flower_pots");
                break;
            // flowers (includes small_flowers and tall_flowers)
            // gold_ores
            case "GOLD_ORE":
                list.add("GOLD_ORE");
                break;
            // ice
            case "ICE":
            case "PACKED_ICE":
            case "FROSTED_ICE":
                list.add("ice");
                break;
            // impermeable
            case "GLASS":
            case "STAINED_GLASS":
                list.add("impermeable");
                break;
            // infiniburn_end (includes infiniburn_overworld)
            case "BEDROCK":
                list.add("dragon_immune"); // also part of dragon_immune
                list.add("wither_immune"); // also part of wither_immune
                list.add("infiniburn_end");
                break;
            // infiniburn_nether (includes infiniburn_overworld)
            // infiniburn_overworld
            case "MAGMA":
                list.add("infiniburn_overworld");
                list.add("infiniburn_end"); // also part of infiniburn_end
                list.add("infiniburn_nether"); // also part of infiniburn_nether
                break;
            // inside_step_sound_blocks
            case "SNOW":
                list.add("inside_step_sound_blocks");
                list.add("rabbits_spawnable_on"); // also part of rabbits_spawnable_on
                list.add("snow"); // also part of snow
                list.add("wolves_spawnable_on"); // also part of wolves_spawnable_on
                break;
            // iron_ores
            case "IRON_ORE":
                list.add("iron_ores");
                break;
            // lapis_ores
            case "LAPIS_ORE":
                list.add("lapis_ore");
                break;
            // leaves
            case "LEAVES":
            case "LEAVES_2":
                list.add("leaves");
                list.add("completes_find_tree_tutorial"); // also part of completes_find_tree_tutorial
                list.add("parrots_spawnable_on"); // also part of parrots_spawnable_on
                break;
            // logs (includes logs_that_burn)
            // logs_that_burn (includes *_logs)
            case "LOG":
            case "LOG_2":
                list.add("logs");
                list.add("completes_find_tree_tutorial"); // also part of completes_find_tree_tutorial
                list.add("parrots_spawnable_on"); // also part of parrots_spawnable_on
                list.add("overworld_natural_logs"); // also part of overworld_natural_logs // TODO: exclude woods (log[12-15], log2[12-13]
                break;
            // mooshrooms_spawnable_on
            case "MYCEL":
                list.add("mooshrooms_spawnable_on");
                list.add("dirt"); // also part of dirt
                list.add("mushroom_grow_block"); // also part of mushroom_grow_block
                list.add("enderman_holdable"); // also part of enderman_holdable
                break;
            // overworld_natural_logs
            // parrots_spawnable_on (includes leaves and logs)
            // planks
            case "WOOD":
                list.add("planks");
                break;
            // portals
            case "ENDER_PORTAL":
            case "END_GATEWAY":
                list.add("dragon_immune"); // also part of dragon_immune
                list.add("wither_immune"); // also part of wither_immune
            case "PORTAL":
                list.add("portals");
                break;
            // pressure_plates (includes wooden_pressure_plates adn stone_pressure_plates)
            // rabbits_spawnable_on
            // rails
            case "POWERED_RAIL":
            case "DETECTOR_RAIL":
            case "RAILS":
            case "ACTIVATOR_RAIL":
                list.add("rails");
                break;
            // redstone_ores
            case "REDSTONE_ORE":
            case "GLOWING_REDSTONE_ORE":
                list.add("redstone_ores");
                break;
            // sand
            case "SAND":
                list.add("sand");
                list.add("rabbits_spawnable_on"); // also part of rabbits_spawnable_on // TODO: exclude red_sand (sand:1)
                list.add("dead_bush_may_place_on"); // also part of dead_bush_may_place_on
                list.add("enderman_holdable"); // also part of enderman_holdable
                break;
            // saplings
            case "SAPLING":
                list.add("saplings");
                break;
            // shulker_boxes
            case "WHITE_SHULKER_BOX":
            case "ORANGE_SHULKER_BOX":
            case "MAGENTA_SHULKER_BOX":
            case "LIGHT_BLUE_SHULKER_BOX":
            case "YELLOW_SHULKER_BOX":
            case "LIME_SHULKER_BOX":
            case "PINK_SHULKER_BOX":
            case "GRAY_SHULKER_BOX":
            case "SILVER_SHULKER_BOX":
            case "CYAN_SHULKER_BOX":
            case "PURPLE_SHULKER_BOX":
            case "BLUE_SHULKER_BOX":
            case "BROWN_SHULKER_BOX":
            case "GREEN_SHULKER_BOX":
            case "RED_SHULKER_BOX":
            case "BLACK_SHULKER_BOX":
                list.add("shulker_boxes");
                break;
            // signs (includes standing_signs and wall_signs)
            // slabs (includes wooden_slabs)
            case "DOUBLE_STEP":
            case "STEP":
            case "DOUBLE_STONE_SLAB2":
            case "STONE_SLAB2":
            case "PURPUR_DOUBLE_SLAB":
            case "PURPUR_SLAB":
                list.add("slabs");
                break;
            // small_flowers
            case "YELLOW_FLOWER":
            case "RED_FLOWER":
                list.add("small_flowers");
                list.add("flowers"); // also part of flowers
                list.add("enderman_holdable"); // also part of enderman_holdable
                break;
            // snow
            case "SNOW_BLOCK":
                list.add("snow");
                list.add("wolves_spawnable_on"); // also part of wolves_spawnable_on
                break;
            // stairs (includes wooden_stairs)
            case "COBBLESTONE_STAIRS":
            case "BRICK_STAIRS":
            case "SMOOTH_STAIRS":
            case "NETHER_BRICK_STAIRS":
            case "SANDSTONE_STAIRS":
            case "QUARTZ_STAIRS":
            case "RED_SANDSTONE_STAIRS":
            case "PURPUR_STAIRS":
                list.add("stairs");
                break;
            // standing_signs
            case "SIGN_POST":
                list.add("standing_signs");
                list.add("signs"); // also part of signs
                list.add("wall_post_override"); // also part of wall_post_override
                break;
            // stone_bricks
            case "SMOOTH_BRICK":
                list.add("stone_bricks");
                break;
            // stone_pressure_plates
            case "STONE_PLATE":
                list.add("stone_pressure_plates");
                list.add("pressure_plates"); // also part of pressure_plates
                list.add("wall_post_override"); // also part of wall_post_override
                break;
            // tall_flowers
            case "DOUBLE_PLANT": // TODO: exclude double_plant:2 and double_plant:3
                list.add("tall_flowers");
                list.add("flowers"); // also part of flowers
                break;
            // terracotta
            case "HARD_CLAY":
            case "STAINED_CLAY":
                list.add("terracotta");
                list.add("dead_bush_may_place_on"); // also part of dead_bush_may_place_on
                break;
            // trapdoors (includes wooden_trapdoors)
            case "IRON_TRAPDOOR":
                list.add("trapdoors");
                break;
            // valid_spawn
            // walls
            case "COBBLE_WALL":
                list.add("walls");
                break;
            // wall_post_override
            case "TORCH":
            case "REDSTONE_TORCH_OFF":
            case "REDSTONE_TORCH_ON":
            case "TRIPWIRE":
                list.add("wall_post_override");
                break;
            // wall_signs
            case "WALL_SIGN":
                list.add("wall_signs");
                list.add("signs"); // also part of signs
                list.add("wall_post_override"); // also part of wall_post_override
                break;
            // wart_blocks
            case "NETHER_WART_BLOCK":
                list.add("wart_blocks");
                list.add("completes_find_tree_tutorial"); // also part of completes_find_tree_tutorial
                break;
            // wolves_spawnable_on
            // wooden_buttons
            case "WOOD_BUTTON":
                list.add("wooden_buttons");
                list.add("buttons"); // also part of buttons
                break;
            // wooden_doors
            case "WOODEN_DOOR":
            case "SPRUCE_DOOR":
            case "BIRCH_DOOR":
            case "JUNGLE_DOOR":
            case "ACACIA_DOOR":
            case "DARK_OAK_DOOR":
                list.add("wooden_doors");
                list.add("doors"); // also part of doors
                break;
            // wooden_fences
            case "FENCE":
            case "SPRUCE_FENCE":
            case "BIRCH_FENCE":
            case "JUNGLE_FENCE":
            case "DARK_OAK_FENCE":
            case "ACACIA_FENCE":
                list.add("wooden_fences");
                list.add("fences"); // also part of fences
                break;
            // wooden_pressure_plates
            case "WOOD_PLATE":
                list.add("wooden_pressure_plates");
                list.add("pressure_plates"); // also part of pressure_plates
                break;
            // wooden_slabs
            case "WOOD_DOUBLE_STEP":
            case "WOOD_STEP":
                list.add("wooden_slabs");
                list.add("slabs"); // also part of slabs
                break;
            // wooden_stairs
            case "WOOD_STAIRS":
            case "SPRUCE_WOOD_STAIRS":
            case "BIRCH_WOOD_STAIRS":
            case "JUNGLE_WOOD_STAIRS":
            case "ACACIA_STAIRS":
            case "DARK_OAK_STAIRS":
                list.add("wooden_stairs");
                list.add("stairs"); // also part of stairs
                break;
            // wooden_trapdoors
            case "TRAP_DOOR":
                list.add("wooden_trapdoors");
                list.add("trapdoors"); // also part of trapdoors
                break;
            // wool_carpets (old name carpets)
            case "CARPET":
                list.add("wool_carpets");
                list.add("carpets");
                break;
            // wool
            case "WOOL":
                list.add("wool");
                break;
        }
        return list;
    }
}
