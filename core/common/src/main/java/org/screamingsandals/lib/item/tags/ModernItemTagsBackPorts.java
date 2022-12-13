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

package org.screamingsandals.lib.item.tags;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.tags.TagPortHelper;

import java.util.List;
import java.util.function.Predicate;

public class ModernItemTagsBackPorts {
    @Nullable
    public static List<@NotNull String> getPortedTags(@NotNull ItemTypeHolder itemType, @NotNull Predicate<@NotNull String> nativeTagChecker) {
        if (!Server.isVersion(1, 13)) {
            /* This file doesn't support legacy version backports (these are supported by Bukkit-specific implementation, because Bukkit is the only legacy platform) */
            return List.of();
        }

        /*
        Only item tag additions and renames are backported!!! (And removals are ported to newer versions if it makes sense)

        Modifying existing tag may lead into unexpected behaviour!
        (the tag is updated only in slib, so the plugin would have different tag then the game logic)

        Also tags that are connected only with the new features and their usage doesn't make any sense in older versions are not backported.

        Use Helper to check tags here! ItemTypeHolder#hasTag doesn't have any ported tags yet!
         */

        var helper = new TagPortHelper(nativeTagChecker);

        if (!Server.isVersion(1, 14)) {
            if (itemType.is(
                    "music_disc_13",
                    "music_disc_cat",
                    "music_disc_blocks",
                    "music_disc_chirp",
                    "music_disc_far",
                    "music_disc_mall",
                    "music_disc_mellohi",
                    "music_disc_stal",
                    "music_disc_strad",
                    "music_disc_ward",
                    "music_disc_11",
                    "music_disc_wait"
            )) {
                helper.port("music_discs");
            }
            if (itemType.is(
                    "oak_sign",
                    "spruce_sign",
                    "birch_sign",
                    "acacia_sign",
                    "jungle_sign",
                    "dark_oak_sign"
            )) {
                helper.port("signs");
            }
            if (itemType.is(
                    "dandelion",
                    "poppy",
                    "blue_orchid",
                    "allium",
                    "azure_bluet",
                    "red_tulip",
                    "orange_tulip",
                    "white_tulip",
                    "pink_tulip",
                    "oxeye_daisy"
            )) {
                helper.port("small_flowers");
            }
            if (itemType.is("cobblestone_wall", "mossy_cobblestone_wall")) {
                helper.port("walls");
            }
            if (itemType.is(
                    "red_bed",
                    "black_bed",
                    "blue_bed",
                    "brown_bed",
                    "cyan_bed",
                    "gray_bed",
                    "green_bed",
                    "light_blue_bed",
                    "light_gray_bed",
                    "lime_bed",
                    "magenta_bed",
                    "orange_bed",
                    "pink_bed",
                    "purple_bed",
                    "white_bed",
                    "yellow_bed"
            )) {
                helper.port("beds");
            }
            if (itemType.is(
                    "oak_fence",
                    "acacia_fence",
                    "dark_oak_fence",
                    "spruce_fence",
                    "birch_fence",
                    "jungle_fence"
            )) {
                helper.port("wooden_fences");
            }
            if (helper.hasTag("wooden_fences") || itemType.is("nether_brick_fence")) {
                helper.port("fences");
            }
        }

        if (!Server.isVersion(1, 15)) {
            if (itemType.is("sunflower", "lilac", "peony", "rose_bush")) {
                helper.port("tall_flowers");
            }
            if (helper.hasTag("small_flowers", "tall_flowers")) {
                helper.port("flowers");
            }
            if (itemType.is("written_book", "writable_book")) {
                helper.port("lectern_books");
            }
        }

        if (!Server.isVersion(1, 16)) {
            if (itemType.is("emerald", "diamond", "gold_ingot", "iron_ingot")) {
                helper.port("beacon_payment_items");
            }
            if (itemType.is("gold_ore")) {
                helper.port("gold_ores");
            }
            if (helper.hasTag("dark_oak_logs", "oak_logs", "acacia_logs", "birch_logs", "jungle_logs", "spruce_logs")) {
                helper.port("logs_that_burn");
            }
            if (itemType.is("cobblestone")) {
                helper.port("furnace_materials");
                helper.port("stone_tool_materials");
            }
            if (helper.hasTag("music_discs")) { // all musics in pre-1.16 music_discs are also part of creeper_drop_music_discs
                helper.port("creeper_drop_music_discs");
            }
        }

        if (!Server.isVersion(1, 16, 2)) {
            // furnace_material -> stone_crafting_materials
            if (helper.hasTag("furnace_material")) {
                helper.port("stone_crafting_materials");
            }
        } else {
            // Older version of stone_crafting_materials was furnace_material
            // NOTE: remove this mapping if it causes collision in future
            if (helper.hasTag("stone_crafting_materials")) {
                helper.port("furnace_material");
            }
        }

        if (!Server.isVersion(1, 17)) {
            if (itemType.is("leather")) {
                helper.port("ignored_by_piglin_babies");
            }
            if (itemType.is("porkchop", "cooked_porkchop")) {
                helper.port("piglin_food");
            }
            if (itemType.is("sweet_berries", "glow_berries")) {
                helper.port("fox_food");
            }
            if (itemType.is("diamond_ore")) {
                helper.port("diamond_ores");
            }
            if (itemType.is("iron_ore")) {
                helper.port("iron_ores");
            }
            if (itemType.is("lapis_ore")) {
                helper.port("lapis_ores");
            }
            if (itemType.is("redstone_ore")) {
                helper.port("redstone_ores");
            }
            if (itemType.is("coal_ore")) {
                helper.port("coal_ores");
            }
            if (itemType.is("emerald_ore")) {
                helper.port("emerald_ores");
            }
        }

        if (!Server.isVersion(1, 18)) {
            if (itemType.is("dirt", "grass_block", "podzol", "coarse_dirt", "mycelium", "rooted_dirt", "moss_block")) {
                helper.port("dirt");
            }
            if (itemType.is(
                    "terracotta",
                    "white_terracotta",
                    "orange_terracotta",
                    "magenta_terracotta",
                    "light_blue_terracotta",
                    "yellow_terracotta",
                    "lime_terracotta",
                    "pink_terracotta",
                    "gray_terracotta",
                    "light_gray_terracotta",
                    "cyan_terracotta",
                    "purple_terracotta",
                    "blue_terracotta",
                    "brown_terracotta",
                    "green_terracotta",
                    "red_terracotta",
                    "black_terracotta"
            )) {
                helper.port("terracotta");
            }
        }

        if (!Server.isVersion(1, 19)) {
            // carpets -> wool_carpets
            if (helper.hasTag("carpets")) {
                helper.port("wool_carpets");
            }
            if (itemType.is("compass")) {
                helper.port("compasses");
            }
            if (itemType.is("nether_wart_block", "warped_wart_block")) {
                helper.port("wart_blocks");
            }
            if (helper.hasTag("logs", "leaves", "wart_blocks")) {
                helper.port("completes_find_tree_tutorial");
            }
            // occludes_vibration_signals -> dampens_vibrations
            if (helper.hasTag("occludes_vibration_signals")) {
                helper.port("dampens_vibrations");
            }
            if (itemType.is("acacia_log", "birch_log", "oak_log", "jungle_log", "spruce_log", "dark_oak_log")) {
                helper.port("overworld_natural_logs");
            }
        } else {
            // Older version of wool_carpets was carpets
            // NOTE: remove this mapping if it causes collision in future
            if (helper.hasTag("wool_carpets")) {
                helper.port("carpets");
            }
            // Older version of occludes_vibration_signals was dampens_vibrations
            // NOTE: remove this mapping if it causes collision in future
            if (helper.hasTag("dampens_vibrations")) {
                helper.port("occludes_vibration_signals");
            }
        }

        if (!Server.isVersion(1, 19, 3)) {
            if (itemType.is("flint_and_steel", "fire_charge")) {
                helper.port("creeper_igniters");
            }
            if (itemType.is("book", "written_book", "enchanted_book", "writable_book")) {
                helper.port("bookshelf_books");
            }
            if (itemType.is(
                    "acacia_fence_gate",
                    "birch_fence_gate",
                    "dark_oak_fence_gate",
                    "jungle_fence_gate",
                    "oak_fence_gate",
                    "spruce_fence_gate",
                    "crimson_fence_gate",
                    "warped_fence_gate",
                    "mangrove_fence_gate"
            )) {
                helper.port("fence_gates");
            }
        } else {
            if (itemType.is("acacia_log", "birch_log", "oak_log", "jungle_log", "spruce_log", "dark_oak_log")) {
                helper.port("overworld_natural_logs");
            }
        }

        return helper.getPorts();
    }
}
