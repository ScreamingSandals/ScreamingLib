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
        mapAliasItem("ZOMBIFIED_PIGLIN_SPAWN_EGG", "ZOMBIE_PIGMAN_SPAWN_EGG");
        mapAlias("SMOOTH_STONE_SLAB", "STONE_SLAB");
        mapAliasItem("GREEN_DYE", "CACTUS_GREEN");
        mapAliasItem("YELLOW_DYE", "DANDELION_YELLOW");
        mapAliasItem("RED_DYE", "ROSE_RED");
        mapAlias("OAK_SIGN", "SIGN");
        mapAlias("BIRCH_SIGN", "SIGN");
        mapAlias("DARK_OAK_SIGN", "SIGN");
        mapAlias("JUNGLE_SIGN", "SIGN");
        mapAlias("SPRUCE_SIGN", "SIGN");
        mapAlias("ACACIA_SIGN", "SIGN");
        mapAlias("OAK_WALL_SIGN", "WALL_SIGN");
        mapAlias("BIRCH_WALL_SIGN", "WALL_SIGN");
        mapAlias("BIRCH_WALL_SIGN", "WALL_SIGN");
        mapAlias("DARK_OAK_WALL_SIGN", "WALL_SIGN");
        mapAlias("JUNGLE_WALL_SIGN", "WALL_SIGN");
        mapAlias("SPRUCE_WALL_SIGN", "WALL_SIGN");
        mapAlias("ACACIA_WALL_SIGN", "WALL_SIGN");
        mapAlias("DIRT_PATH", "GRASS_PATH");
        mapAlias("WATER_CAULDRON", "CAULDRON");

        // non-color -> white alias
        mapAlias("WOOL", "WHITE_WOOL");
        mapAlias("CARPET", "WHITE_CARPET");
        mapAlias("CONCRETE", "WHITE_CONCRETE");
        mapAlias("CONCRETE_POWDER", "WHITE_CONCRETE_POWDER");
        mapAlias("STAINED_GLASS", "WHITE_STAINED_GLASS");
        mapAlias("STAINED_GLASS_PANE", "WHITE_STAINED_GLASS_PANE");
        mapAlias("SHULKER_BOX", "WHITE_SHULKER_BOX");
        mapAlias("BANNER", "WHITE_BANNER");
        mapAlias("GLAZED_TERRACOTTA", "WHITE_GLAZED_TERRACOTTA");

        if (!mappingFlags.contains(MappingFlags.NO_COLORED_BEDS)) {
            mapAlias("BED", "WHITE_BED");
        } else {
            mapAlias("BED", "RED_BED");
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
