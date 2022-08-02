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
    public static List<@NotNull String> getPortedTags(@NotNull ItemTypeHolder blockType, @NotNull Predicate<@NotNull String> nativeTagChecker) {
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

        // TODO: replace all comments below with tag backports

        var helper = new TagPortHelper(nativeTagChecker);

        if (!Server.isVersion(1, 14)) {
            // music_discs
            // signs
            // small_flowers
            // walls
            // beds
            // fences
        }

        if (!Server.isVersion(1, 15)) {
            // flowers
            // tall_flowers
            // lectern_books
        }

        if (!Server.isVersion(1, 16)) {
            // beacon_payment_items
            // gold_ores
            // logs_that_burn
            // furnace_materials
            // stone_tool_materials
            // creeper_drop_music_discs
        }

        if (!Server.isVersion(1, 16, 2)) {
            // stone_crafting_materials
        } else {
            // re-add furnace_materials
        }

        if (!Server.isVersion(1, 17)) {
            // ignored_by_piglin_babies
            // piglin_food
            // fox_food
            // diamond_ores
            // iron_ores
            // lapis_ores
            // redstone_ores
            // coal_ores
            // emerald_ores
            // cluster_max_harvestables????
        }

        if (!Server.isVersion(1, 18)) {
            // dirt
            // terracotta
        }

        if (!Server.isVersion(1, 19)) {
            // carpets -> wool_carpets
            // compasses
            // completes_find_tree_tutorial
            // wart_blocks
            // occludes_vibration_signals -> dampens_vibrations
            // overworld_natural_logs
        } else {
            // wool_carpets -> carpets
            // dampens_vibrations -> occludes_vibration_signals
        }

        return helper.getPorts();
    }
}
