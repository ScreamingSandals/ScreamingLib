/*
 * Copyright 2025 ScreamingSandals
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

package org.screamingsandals.lib.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.impl.block.BlockRegistry;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.impl.item.ItemTypeRegistry;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Class responsible for remapping item and block id's.
 */
@ProvidedService
@RequiredArgsConstructor
@ApiStatus.Internal
public abstract class ItemBlockIdsRemapper {
    protected final @NotNull ItemTypeRegistry itemTypeMapper;
    protected final @NotNull BlockRegistry blockTypeMapper;
    protected final @NotNull List<@NotNull MappingFlags> mappingFlags = new ArrayList<>();
    public static final @NotNull List<@NotNull String> COLORS = List.of(
            "WHITE",
            "ORANGE",
            "MAGENTA",
            "LIGHT_BLUE",
            "YELLOW",
            "LIME",
            "PINK",
            "GRAY",
            "LIGHT_GRAY",
            "CYAN",
            "PURPLE",
            "BLUE",
            "BROWN",
            "GREEN",
            "RED",
            "BLACK"
    );
    public static final @NotNull Map<@NotNull Predicate<Block>, Function<String, Optional<Block>>> colorableBlocks = new HashMap<>();
    public static final @NotNull Map<@NotNull Predicate<ItemType>, Function<String, Optional<ItemType>>> colorableItems = new HashMap<>();


    /**
     * Starts the remapping upon construction.
     */
    @OnPostConstruct
    public void doMapping() {
        flatteningMapping();
        javaAutoColorable();
    }

    private void javaAutoColorable() {
        makeColorable("WOOL");
        makeColorable("CARPET");
        makeColorable("CONCRETE");
        makeColorable("CONCRETE_POWDER");
        makeColorable("TERRACOTTA");
        makeColorable("STAINED_GLASS", "GLASS");
        makeColorable("STAINED_GLASS_PANE", "GLASS_PANE");
        makeColorable("SHULKER_BOX");
        makeColorable("BANNER");
        makeColorable("GLAZED_TERRACOTTA");

        if (!mappingFlags.contains(MappingFlags.NO_COLORED_BEDS)) {
            makeColorable("BED");
        }
    }

    private void makeColorable(@NotNull String baseName) {
        makeColorableItem(baseName, baseName);
        makeColorableBlock(baseName, baseName);
    }

    private void makeColorableBlock(@NotNull String baseName) {
        makeColorableBlock(baseName, baseName);
    }

    private void makeColorableItem(@NotNull String baseName) {
        makeColorableItem(baseName, baseName);
    }

    private void makeColorable(@NotNull String baseName, @NotNull String notColoredName) {
        makeColorableBlock(baseName, notColoredName);
        makeColorableItem(baseName, notColoredName);
    }

    private void makeColorableBlock(@NotNull String baseName, @NotNull String notColoredName) {
        var list = new ArrayList<Block>();
        COLORS.forEach(s -> {
            var block = Block.ofNullable(s + "_" + baseName);
            if (block != null) {
                if (!list.contains(block)) {
                    list.add(block);
                }
            };
        });

        var block = Block.ofNullable(notColoredName);
        if (block != null) {
            if (!list.contains(block)) {
                list.add(block);
            }
        }

        if (!list.isEmpty()) { // if list is empty, we don't have this material
            colorableBlocks.put(list::contains, s -> {
                if (COLORS.contains(s.toUpperCase(Locale.ROOT))) {
                    return Optional.ofNullable(Block.ofNullable(s.toUpperCase(Locale.ROOT) + "_" + baseName));
                }
                return Optional.empty();
            });
        }
    }

    private void makeColorableItem(@NotNull String baseName, @NotNull String notColoredName) {
        List<ItemType> list = new ArrayList<>();
        COLORS.forEach(s -> {
            var item = ItemType.ofNullable(s + "_" + baseName);
            if (item != null) {
                if (!list.contains(item)) {
                    list.add(item);
                }
            }
        });

        var item = ItemType.ofNullable(notColoredName);
        if (item != null) {
            if (!list.contains(item)) {
                list.add(item);
            }
        }

        if (!list.isEmpty()) { // if list is empty, we don't have this material
            colorableItems.put(list::contains, s -> {
                if (COLORS.contains(s.toUpperCase(Locale.ROOT))) {
                    return Optional.ofNullable(ItemType.ofNullable(s.toUpperCase(Locale.ROOT) + "_" + baseName));
                }
                return Optional.empty();
            });
        }
    }

    private void flatteningMapping() {
        // Flattening remapping
        mapAliasItem("zombified_piglin_spawn_egg", "zombie_pigman_spawn_egg");
        mapAlias("smooth_stone_slab", "stone_slab");
        mapAliasItem("green_dye", "cactus_green");
        mapAliasItem("yellow_dye", "dandelion_yellow");
        mapAliasItem("red_dye", "rose_red");
        mapAliasItem("turtle_scute", "scute");
        mapAlias("oak_sign", "sign");
        mapAlias("birch_sign", "sign");
        mapAlias("dark_oak_sign", "sign");
        mapAlias("jungle_sign", "sign");
        mapAlias("spruce_sign", "sign");
        mapAlias("acacia_sign", "sign");
        mapAlias("oak_wall_sign", "wall_sign");
        mapAlias("birch_wall_sign", "wall_sign");
        mapAlias("birch_wall_sign", "wall_sign");
        mapAlias("dark_oak_wall_sign", "wall_sign");
        mapAlias("jungle_wall_sign", "wall_sign");
        mapAlias("spruce_wall_sign", "wall_sign");
        mapAlias("acacia_wall_sign", "wall_sign");
        mapAlias("dirt_path", "grass_path");
        mapAlias("water_cauldron", "cauldron");
        mapAlias("short_grass", "grass");

        // non-color -> white alias
        mapAlias("wool", "white_wool");
        mapAlias("carpet", "white_carpet");
        mapAlias("concrete", "white_concrete");
        mapAlias("concrete_powder", "white_concrete_powder");
        mapAlias("stained_glass", "white_stained_glass");
        mapAlias("stained_glass_pane", "white_stained_glass_pane");
        mapAlias("banner", "white_banner");
        mapAlias("glazed_terracotta", "white_glazed_terracotta");

        if (!mappingFlags.contains(MappingFlags.NO_COLORED_BEDS)) {
            mapAlias("bed", "white_bed");
        } else {
            mapAlias("bed", "red_bed");
        }
    }

    protected void mapAlias(@NotNull String mappingKey, @NotNull String alias) {
        itemTypeMapper.mapAlias(mappingKey, alias);
        blockTypeMapper.mapAlias(mappingKey, alias);
    }

    protected void mapAliasItem(@NotNull String mappingKey, @NotNull String alias) {
        itemTypeMapper.mapAlias(mappingKey, alias);
    }

    protected void mapAliasBlock(@NotNull String mappingKey, @NotNull String alias) {
        blockTypeMapper.mapAlias(mappingKey, alias);
    }


    public enum MappingFlags {
        NO_COLORED_BEDS
    }
}
