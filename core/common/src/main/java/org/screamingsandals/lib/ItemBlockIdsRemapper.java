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

package org.screamingsandals.lib;

import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.block.BlockType;
import org.screamingsandals.lib.block.BlockTypeRegistry;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.item.ItemTypeRegistry;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Class responsible for remapping item and block id's.
 */
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
@ProvidedService
@RequiredArgsConstructor
public abstract class ItemBlockIdsRemapper {
    protected final @NotNull ItemTypeRegistry itemTypeMapper;
    protected final @NotNull BlockTypeRegistry blockTypeMapper;
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
    public static final @NotNull Map<@NotNull Predicate<BlockType>, Function<String, Optional<BlockType>>> colorableBlocks = new HashMap<>();
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
        var list = new ArrayList<BlockType>();
        COLORS.forEach(s -> BlockType.ofNullable(s + "_" + baseName).ifNotNull(materialHolder -> {
            if (!list.contains(materialHolder)) {
                list.add(materialHolder);
            }
        }));

        BlockType.ofNullable(notColoredName).ifNotNull(materialHolder -> {
            if (!list.contains(materialHolder)) {
                list.add(materialHolder);
            }
        });

        if (!list.isEmpty()) { // if list is empty, we don't have this material
            colorableBlocks.put(list::contains, s -> {
                if (COLORS.contains(s.toUpperCase(Locale.ROOT))) {
                    return Optional.ofNullable(BlockType.ofNullable(s.toUpperCase(Locale.ROOT) + "_" + baseName));
                }
                return Optional.empty();
            });
        }
    }

    private void makeColorableItem(@NotNull String baseName, @NotNull String notColoredName) {
        List<ItemType> list = new ArrayList<>();
        COLORS.forEach(s -> ItemType.ofNullable(s + "_" + baseName).ifNotNull(materialHolder -> {
            if (!list.contains(materialHolder)) {
                list.add(materialHolder);
            }
        }));

        ItemType.ofNullable(notColoredName).ifNotNull(materialHolder -> {
            if (!list.contains(materialHolder)) {
                list.add(materialHolder);
            }
        });

        if (!list.isEmpty()) { // if list is empty, we don't have this material
            colorableItems.put(list::contains, s -> {
                if (COLORS.contains(s.toUpperCase(Locale.ROOT))) {
                    return ItemType.ofNullable(s.toUpperCase(Locale.ROOT) + "_" + baseName).toOptional();
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
