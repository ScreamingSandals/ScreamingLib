/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.bukkit.item.tags;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

// because we support legacy only on bukkit and this doesn't affect flattening versions, we don't need it in common module
public class BukkitLegacyItemTagResolution {
    @NotNull
    public static List<String> constructTags(@NotNull Material material) {
        var list = new ArrayList<String>();
        switch (material.name()) {
            // TODO: acacia_logs
            // TODO: birch_logs
            // TODO: dark_oak_logs
            // TODO: jungle_logs
            // TODO: oak_logs
            // TODO: spruce_logs

            // anvil
            case "ANVIL":
                list.add("anvil");
                break;
            // arrows
            case "ARROW":
            case "SPECTRAL_ARROW":
            case "TIPPED_ARROW":
                list.add("arrows");
                break;
            // banners
            case "BANNER":
                list.add("banners");
                break;
            // beacon_payment_items
            case "EMERALD":
            case "DIAMOND":
            case "GOLD_INGOT":
            case "IRON_INGOT":
                list.add("beacon_payment_items");
                break;
            // beds
            case "BED":
                list.add("beds");
                break;
            // boats
            case "BOAT":
            case "BOAT_SPRUCE":
            case "BOAT_BIRCH":
            case "BOAT_JUNGLE":
            case "BOAT_ACACIA":
            case "BOAT_DARK_OAK":
                list.add("boats");
                break;
            // buttons (includes wooden_buttons)
            case "STONE_BUTTON":
                list.add("buttons");
                break;
            // coal_ores
            case "COAL_ORE":
                list.add("coal_ores");
                break;
            // coals
            case "COAL":
                list.add("coals");
                break;
            // compasses
            case "COMPASS":
                list.add("compasses");
                break;
            // completes_find_tree_tutorial (includes logs, leaves, wart_blocks)
            // diamond_ores
            case "DIAMOND_ORE":
                list.add("diamond_ores");
                break;
            // doors (includes wooden_doors)
            case "IRON_DOOR":
                list.add("doors");
                break;
            // emerald_ores
            case "EMERALD_ORE":
                list.add("emerald_ores");
                break;
            // fences (includes wooden_fences)
            case "NETHER_FENCE":
                list.add("fences");
                break;
            // fishes
            case "RAW_FISH":
            case "COOKED_FISH":
                list.add("fishes");
                break;
            // flowers (includes small_flowers, tall_flowers)
            // gold_ores
            case "GOLD_ORE":
                list.add("GOLD_ORE");
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
                break;
            // lectern_books
            case "WRITTEN_BOOK":
            case "BOOK_AND_QUILL":
                list.add("lectern_books");
                break;
            // logs (includes logs_that_burn)
            // logs_that_burn (includes all logs)
            case "LOG":
            case "LOG_2":
                list.add("logs");
                list.add("completes_find_tree_tutorial"); // also part of completes_find_tree_tutorial
                list.add("overworld_natural_logs"); // also part of overworld_natural_logs // TODO: exclude woods (log[12-15], log2[12-13])
                break;
            // creeper_drop_music_discs
            // music_discs (includes creeper_drop_music_discs)
            case "GOLD_RECORD":
            case "GREEN_RECORD":
            case "RECORD_3":
            case "RECORD_4":
            case "RECORD_5":
            case "RECORD_6":
            case "RECORD_7":
            case "RECORD_8":
            case "RECORD_9":
            case "RECORD_10":
            case "RECORD_11":
            case "RECORD_12":
                list.add("music_discs");
                list.add("creeper_drop_music_discs");
                break;
            // overworld_natural_logs (basically all logs)
            // planks
            case "WOOD":
                list.add("planks");
                break;
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
                break;
            // saplings
            case "SAPLING":
                list.add("saplings");
            // signs
            case "SIGN":
                list.add("signs");
                break;
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
            // stone_bricks
            case "SMOOTH_BRICK":
                list.add("stone_bricks");
                break;
            // stone_crafting_materials (old name furnace_materials)
            // stone_tool_materials
            case "COBBLESTONE":
                list.add("stone_tool_materials");
                list.add("stone_crafting_materials");
                list.add("furnace_materials");
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
                break;
            // trapdoors (includes wooden_trapdoors)
            case "IRON_TRAPDOOR":
                list.add("trapdoors");
                break;
            // walls
            case "COBBLE_WALL":
                list.add("walls");
                break;
            // wart_blocks
            case "NETHER_WART_BLOCK":
                list.add("wart_blocks");
                list.add("completes_find_tree_tutorial"); // also part of completes_find_tree_tutorial
                break;
            // wooden_buttons
            case "WOOD_BUTTON":
                list.add("wooden_buttons");
                list.add("buttons"); // also part of buttons
                break;
            // wooden_doors
            case "WOOD_DOOR":
            case "SPRUCE_DOOR_ITEM":
            case "BIRCH_DOOR_ITEM":
            case "JUNGLE_DOOR_ITEM":
            case "ACACIA_DOOR_ITEM":
            case "DARK_OAK_DOOR_ITEM":
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
