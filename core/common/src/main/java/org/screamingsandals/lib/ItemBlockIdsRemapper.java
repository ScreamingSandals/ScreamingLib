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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.block.BlockTypeMapper;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.ItemTypeMapper;
import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.utils.key.*;

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
    protected final @NotNull ItemTypeMapper itemTypeMapper;
    protected final @NotNull BlockTypeMapper blockTypeMapper;
    @Getter
    protected final @NotNull Platform platform;
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
    public static final @NotNull Map<@NotNull Predicate<BlockTypeHolder>, Function<String, Optional<BlockTypeHolder>>> colorableBlocks = new HashMap<>();
    public static final @NotNull Map<@NotNull Predicate<ItemTypeHolder>, Function<String, Optional<ItemTypeHolder>>> colorableItems = new HashMap<>();


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

    private void makeColorable(String baseName) {
        makeColorableItem(baseName, baseName);
        makeColorableBlock(baseName, baseName);
    }

    private void makeColorableBlock(String baseName) {
        makeColorableBlock(baseName, baseName);
    }

    private void makeColorableItem(String baseName) {
        makeColorableItem(baseName, baseName);
    }

    private void makeColorable(String baseName, String notColoredName) {
        makeColorableBlock(baseName, notColoredName);
        makeColorableItem(baseName, notColoredName);
    }

    private void makeColorableBlock(String baseName, String notColoredName) {
        var list = new ArrayList<BlockTypeHolder>();
        COLORS.forEach(s -> BlockTypeHolder.ofNullable(s + "_" + baseName).ifNotNull(materialHolder -> {
            if (!list.contains(materialHolder)) {
                list.add(materialHolder);
            }
        }));

        BlockTypeHolder.ofNullable(notColoredName).ifNotNull(materialHolder -> {
            if (!list.contains(materialHolder)) {
                list.add(materialHolder);
            }
        });

        if (!list.isEmpty()) { // if list is empty, we don't have this material
            colorableBlocks.put(list::contains, s -> {
                if (COLORS.contains(s.toUpperCase(Locale.ROOT))) {
                    return Optional.ofNullable(BlockTypeHolder.ofNullable(s.toUpperCase(Locale.ROOT) + "_" + baseName));
                }
                return Optional.empty();
            });
        }
    }

    private void makeColorableItem(String baseName, String notColoredName) {
        List<ItemTypeHolder> list = new ArrayList<>();
        COLORS.forEach(s -> ItemTypeHolder.ofNullable(s + "_" + baseName).ifNotNull(materialHolder -> {
            if (!list.contains(materialHolder)) {
                list.add(materialHolder);
            }
        }));

        ItemTypeHolder.ofNullable(notColoredName).ifNotNull(materialHolder -> {
            if (!list.contains(materialHolder)) {
                list.add(materialHolder);
            }
        });

        if (!list.isEmpty()) { // if list is empty, we don't have this material
            colorableItems.put(list::contains, s -> {
                if (COLORS.contains(s.toUpperCase(Locale.ROOT))) {
                    return ItemTypeHolder.ofNullable(s.toUpperCase(Locale.ROOT) + "_" + baseName).toOptional();
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

    protected void mapAlias(String mappingKey, String alias) {
        itemTypeMapper.mapAlias(mappingKey, alias);
        blockTypeMapper.mapAlias(mappingKey, alias);
    }

    protected void mapAliasItem(String mappingKey, String alias) {
        itemTypeMapper.mapAlias(mappingKey, alias);
    }

    protected void mapAliasBlock(String mappingKey, String alias) {
        blockTypeMapper.mapAlias(mappingKey, alias);
    }

    protected void f2lColored(String material, int legacyId) {
        f2lColored(material, material, legacyId);
    }

    protected void f2lColoredBlock(String material, int legacyId) {
        f2lColoredBlock(material, material, legacyId);
    }

    protected void f2lColoredItem(String material, int legacyId) {
        f2lColoredItem(material, material, legacyId);
    }

    protected void f2lColored(String flatteningMaterialSuffix, String legacyMaterial, int legacyId) {
        f2lColored(flatteningMaterialSuffix, legacyMaterial, legacyId, null);
    }

    protected void f2lColoredBlock(String flatteningMaterialSuffix, String legacyMaterial, int legacyId) {
        f2lColoredBlock(flatteningMaterialSuffix, legacyMaterial, legacyId, null);
    }

    protected void f2lColoredItem(String flatteningMaterialSuffix, String legacyMaterial, int legacyId) {
        f2lColoredItem(flatteningMaterialSuffix, legacyMaterial, legacyId, null);
    }

    protected void f2lColored(String flatteningMaterialSuffix, String legacyMaterial, int legacyId, String alternativeLegacyName) {
        f2lColoredBlock(flatteningMaterialSuffix, legacyMaterial, legacyId, alternativeLegacyName);
        f2lColoredItem(flatteningMaterialSuffix, legacyMaterial, legacyId, alternativeLegacyName);
    }

    protected void f2lColoredItem(String flatteningMaterialSuffix, String legacyMaterial, int legacyId, String alternativeLegacyName) {
        if (flatteningMaterialSuffix == null || legacyMaterial == null) {
            throw new IllegalArgumentException("Both materials mustn't be null!");
        }
        for (int i = 0; i <= 15; i++) {
            f2lItem(COLORS.get(i) + "_" + flatteningMaterialSuffix, legacyMaterial, legacyId, i, alternativeLegacyName);
        }
    }

    protected void f2lColoredBlock(String flatteningMaterialSuffix, String legacyMaterial, int legacyId, String alternativeLegacyName) {
        if (flatteningMaterialSuffix == null || legacyMaterial == null) {
            throw new IllegalArgumentException("Both materials mustn't be null!");
        }
        for (int i = 0; i <= 15; i++) {
            f2lBlock(COLORS.get(i) + "_" + flatteningMaterialSuffix, legacyMaterial, legacyId, i, alternativeLegacyName);
        }
    }

    protected void f2lColoredToNonColored(String material, int legacyId) {
        f2lColoredToNonColored(material, material, legacyId);
    }

    protected void f2lColoredToNonColoredBlock(String material, int legacyId) {
        f2lColoredToNonColoredBlock(material, material, legacyId);
    }

    protected void f2lColoredToNonColoredItem(String material, int legacyId) {
        f2lColoredToNonColoredItem(material, material, legacyId);
    }

    protected void f2lColoredToNonColored(String flatteningMaterialSuffix, String legacyMaterial, int legacyId) {
        f2lColoredToNonColored(flatteningMaterialSuffix, legacyMaterial, legacyId, null);
    }

    protected void f2lColoredToNonColoredBlock(String flatteningMaterialSuffix, String legacyMaterial, int legacyId) {
        f2lColoredToNonColoredBlock(flatteningMaterialSuffix, legacyMaterial, legacyId, null);
    }

    protected void f2lColoredToNonColoredBlock(String flatteningMaterialSuffix, Map<String, String> state, String legacyMaterial, int legacyId, int data) {
        f2lColoredToNonColoredBlock(flatteningMaterialSuffix, state, legacyMaterial, legacyId, data, null);
    }

    protected void f2lColoredToNonColoredItem(String flatteningMaterialSuffix, String legacyMaterial, int legacyId) {
        f2lColoredToNonColoredItem(flatteningMaterialSuffix, legacyMaterial, legacyId, null);
    }

    protected void f2lColoredToNonColored(String flatteningMaterialSuffix, String legacyMaterial, int legacyId, String alternativeLegacyName) {
        f2lColoredToNonColoredBlock(flatteningMaterialSuffix, legacyMaterial, legacyId, alternativeLegacyName);
        f2lColoredToNonColoredItem(flatteningMaterialSuffix, legacyMaterial, legacyId, alternativeLegacyName);
    }

    protected void f2lColoredToNonColoredBlock(String flatteningMaterialSuffix, String legacyMaterial, int legacyId, int data) {
        f2lColoredToNonColoredBlock(flatteningMaterialSuffix, legacyMaterial, legacyId, data, null);
    }

    protected void f2lColoredToNonColoredBlock(String flatteningMaterialSuffix, String legacyMaterial, int legacyId, String alternativeLegacyName) {
        f2lColoredToNonColoredBlock(flatteningMaterialSuffix, legacyMaterial, legacyId, 0, alternativeLegacyName);
    }

    protected void f2lColoredToNonColoredBlock(String flatteningMaterialSuffix, String legacyMaterial, int legacyId, int data, String alternativeLegacyName) {
        if (flatteningMaterialSuffix == null || legacyMaterial == null) {
            throw new IllegalArgumentException("Both materials mustn't be null!");
        }
        COLORS.forEach(s ->
                f2lBlock(s + "_" + flatteningMaterialSuffix, legacyMaterial, legacyId, data, alternativeLegacyName)
        );
    }

    protected void f2lColoredToNonColoredBlock(String flatteningMaterialSuffix, Map<String, String> state, String legacyMaterial, int legacyId, int data, String alternativeLegacyName) {
        if (flatteningMaterialSuffix == null || legacyMaterial == null) {
            throw new IllegalArgumentException("Both materials mustn't be null!");
        }
        COLORS.forEach(s ->
                f2lBlock(s + "_" + flatteningMaterialSuffix, state, legacyMaterial, legacyId, data, alternativeLegacyName)
        );
    }

    protected void f2lColoredToNonColoredItem(String flatteningMaterialSuffix, String legacyMaterial, int legacyId, String alternativeLegacyName) {
        if (flatteningMaterialSuffix == null || legacyMaterial == null) {
            throw new IllegalArgumentException("Both materials mustn't be null!");
        }
        COLORS.forEach(s ->
                f2lItem(s + "_" + flatteningMaterialSuffix, legacyMaterial, legacyId, alternativeLegacyName)
        );
    }

    /* For Materials where the name is same */
    protected void f2l(String material, int legacyId) {
        f2l(material, material, legacyId);
    }

    protected void f2lItem(String material, int legacyId) {
        f2lItem(material, material, legacyId);
    }

    protected void f2lBlock(String material, int legacyId) {
        f2lBlock(material, material, legacyId);
    }

    protected void f2l(String material, int legacyId, String alternativeLegacyName) {
        f2l(material, material, legacyId, alternativeLegacyName);
    }

    protected void f2lItem(String material, int legacyId, String alternativeLegacyName) {
        f2lItem(material, material, legacyId, alternativeLegacyName);
    }

    protected void f2lBlock(String material, int legacyId, String alternativeLegacyName) {
        f2lBlock(material, material, legacyId, alternativeLegacyName);
    }

    /* For Materials where the name is changed */
    protected void f2l(String flatteningMaterial, String legacyMaterial, int legacyId) {
        f2l(flatteningMaterial, legacyMaterial, legacyId, 0);
    }

    protected void f2lItem(String flatteningMaterial, String legacyMaterial, int legacyId) {
        f2lItem(flatteningMaterial, legacyMaterial, legacyId, 0);
    }

    protected void f2lBlock(String flatteningMaterial, String legacyMaterial, int legacyId) {
        f2lBlock(flatteningMaterial, legacyMaterial, legacyId, 0);
    }

    protected void f2l(String flatteningMaterial, String legacyMaterial, int legacyId, String alternativeLegacyName) {
        f2l(flatteningMaterial, legacyMaterial, legacyId, 0, alternativeLegacyName);
    }

    protected void f2lItem(String flatteningMaterial, String legacyMaterial, int legacyId, String alternativeLegacyName) {
        f2lItem(flatteningMaterial, legacyMaterial, legacyId, 0, alternativeLegacyName);
    }

    protected void f2lBlock(String flatteningMaterial, String legacyMaterial, int legacyId, String alternativeLegacyName) {
        f2lBlock(flatteningMaterial, legacyMaterial, legacyId, 0, alternativeLegacyName);
    }

    /* For Materials where the name is changed and data is not zero*/
    protected void f2l(String flatteningMaterial, String legacyMaterial, int legacyId, int data) {
        f2l(flatteningMaterial, legacyMaterial, legacyId, data, null);
    }

    protected void f2lItem(String flatteningMaterial, String legacyMaterial, int legacyId, int data) {
        f2lItem(flatteningMaterial, legacyMaterial, legacyId, data, null);
    }

    protected void f2lBlock(String flatteningMaterial, String legacyMaterial, int legacyId, int data) {
        f2lBlock(flatteningMaterial, legacyMaterial, legacyId, data, null);
    }

    protected void f2l(String flatteningMaterial, String legacyMaterial, int legacyId, int data, String alternativeLegacyName) {
        f2lBlock(flatteningMaterial, legacyMaterial, legacyId, data, alternativeLegacyName);
        f2lItem(flatteningMaterial, legacyMaterial, legacyId, data, alternativeLegacyName);
    }

    protected void f2lItem(String flatteningMaterial, String legacyMaterial, int legacyId, int data, String alternativeLegacyName) {
        if (flatteningMaterial == null || legacyMaterial == null) {
            throw new IllegalArgumentException("Both flattening and legacy materials mustn't be null!");
        }
        var flatteningMaterialNamespaced = ResourceLocation.of(flatteningMaterial);
        var legacyMaterialNamespaced = ResourceLocation.of(legacyMaterial);
        var alternativeLegacyNamespaced =
                alternativeLegacyName != null && !alternativeLegacyName.equalsIgnoreCase(legacyMaterial) ? ResourceLocation.of(alternativeLegacyName) : null;

        var mapping = itemTypeMapper.getUNSAFE_mapping();
        ItemTypeHolder holder = null;
        if (platform.isUsingLegacyNames() && (mapping.containsKey(legacyMaterialNamespaced) || (alternativeLegacyNamespaced != null && mapping.containsKey(alternativeLegacyNamespaced)))) {
            if (mapping.containsKey(legacyMaterialNamespaced)) {
                holder = mapping.get(legacyMaterialNamespaced).withForcedDurability((short) data);
                if (!mapping.containsKey(flatteningMaterialNamespaced) && !flatteningMaterialNamespaced.equals(legacyMaterialNamespaced)) {
                    mapping.put(flatteningMaterialNamespaced, holder);
                }
                if (data == 0 && alternativeLegacyNamespaced != null && !mapping.containsKey(alternativeLegacyNamespaced)) {
                    mapping.put(alternativeLegacyNamespaced, holder);
                }
                if (alternativeLegacyNamespaced != null && !mapping.containsKey(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)))) {
                    mapping.put(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)), holder);
                }
            } else if (alternativeLegacyNamespaced != null && mapping.containsKey(alternativeLegacyNamespaced)) {
                holder = mapping.get(alternativeLegacyNamespaced).withForcedDurability((short) data);
                if (!mapping.containsKey(flatteningMaterialNamespaced) && !flatteningMaterialNamespaced.equals(legacyMaterialNamespaced)) {
                    mapping.put(flatteningMaterialNamespaced, holder);
                }
                if (data == 0 && !mapping.containsKey(legacyMaterialNamespaced)) {
                    mapping.put(legacyMaterialNamespaced, holder);
                }
                if (!mapping.containsKey(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)))) {
                    mapping.put(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)), holder);
                }
            }
        } else if (!platform.isUsingLegacyNames() && mapping.containsKey(flatteningMaterialNamespaced)) {
            holder = mapping.get(flatteningMaterialNamespaced);
            if (data == 0 && !mapping.containsKey(legacyMaterialNamespaced) && !flatteningMaterialNamespaced.equals(legacyMaterialNamespaced)) {
                mapping.put(legacyMaterialNamespaced, holder);
            }
            if (!mapping.containsKey(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)))) {
                mapping.put(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)), holder);
            }
            if (data == 0 && alternativeLegacyNamespaced != null && !mapping.containsKey(alternativeLegacyNamespaced) && !flatteningMaterialNamespaced.equals(alternativeLegacyNamespaced)) {
                mapping.put(alternativeLegacyNamespaced, holder);
            }
            if (alternativeLegacyNamespaced != null && !mapping.containsKey(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)))) {
                mapping.put(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)), holder);
            }
        }

        if (holder != null) {
            var legacyIdKey = NumericMappingKey.of(legacyId);
            if (!mapping.containsKey(legacyIdKey) && data == 0) {
                mapping.put(legacyIdKey, holder);
            }
            var legacyIdDataKey = ComplexMappingKey.of(legacyIdKey, NumericMappingKey.of(data));
            if (!mapping.containsKey(legacyIdDataKey)) {
                mapping.put(legacyIdDataKey, holder);
            }
        }
    }


    protected void f2lBlock(String flatteningMaterial, String legacyMaterial, int legacyId, int data, String alternativeLegacyName) {
        if (flatteningMaterial == null || legacyMaterial == null) {
            throw new IllegalArgumentException("Both flattening and legacy materials mustn't be null!");
        }
        var flatteningMaterialNamespaced = ResourceLocation.of(flatteningMaterial);
        var legacyMaterialNamespaced = ResourceLocation.of(legacyMaterial);
        var alternativeLegacyNamespaced =
                alternativeLegacyName != null && !alternativeLegacyName.equalsIgnoreCase(legacyMaterial) ? ResourceLocation.of(alternativeLegacyName) : null;

        var mapping = blockTypeMapper.getUNSAFE_mapping();
        BlockTypeHolder holder = null;
        if (platform.isUsingLegacyNames() && (mapping.containsKey(legacyMaterialNamespaced) || (alternativeLegacyNamespaced != null && mapping.containsKey(alternativeLegacyNamespaced)))) {
            if (mapping.containsKey(legacyMaterialNamespaced)) {
                holder = mapping.get(legacyMaterialNamespaced).withLegacyData((byte) data);
                if (!mapping.containsKey(flatteningMaterialNamespaced) && !flatteningMaterialNamespaced.equals(legacyMaterialNamespaced)) {
                    mapping.put(flatteningMaterialNamespaced, holder);
                }
                if (data == 0 && alternativeLegacyNamespaced != null && !mapping.containsKey(alternativeLegacyNamespaced)) {
                    mapping.put(alternativeLegacyNamespaced, holder);
                }
                if (alternativeLegacyNamespaced != null && !mapping.containsKey(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)))) {
                    mapping.put(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)), holder);
                }
            } else if (alternativeLegacyNamespaced != null && mapping.containsKey(alternativeLegacyNamespaced)) {
                holder = mapping.get(alternativeLegacyNamespaced).withLegacyData((byte) data);
                if (!mapping.containsKey(flatteningMaterialNamespaced) && !flatteningMaterialNamespaced.equals(legacyMaterialNamespaced)) {
                    mapping.put(flatteningMaterialNamespaced, holder);
                }
                if (data == 0 && !mapping.containsKey(legacyMaterialNamespaced)) {
                    mapping.put(legacyMaterialNamespaced, holder);
                }
                if (!mapping.containsKey(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)))) {
                    mapping.put(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)), holder);
                }
            }
        } else if (!platform.isUsingLegacyNames() && mapping.containsKey(flatteningMaterialNamespaced)) {
            holder = mapping.get(flatteningMaterialNamespaced);
            if (data == 0 && !mapping.containsKey(legacyMaterialNamespaced) && !flatteningMaterialNamespaced.equals(legacyMaterialNamespaced)) {
                mapping.put(legacyMaterialNamespaced, holder);
            }
            if (!mapping.containsKey(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)))) {
                mapping.put(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)), holder);
            }
            if (data == 0 && alternativeLegacyNamespaced != null && !mapping.containsKey(alternativeLegacyNamespaced) && !flatteningMaterialNamespaced.equals(alternativeLegacyNamespaced)) {
                mapping.put(alternativeLegacyNamespaced, holder);
            }
            if (alternativeLegacyNamespaced != null && !mapping.containsKey(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)))) {
                mapping.put(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)), holder);
            }
        }

        if (holder != null) {
            var legacyIdKey = NumericMappingKey.of(legacyId);
            if (!mapping.containsKey(legacyIdKey) && data == 0) {
                mapping.put(legacyIdKey, holder);
            }
            var legacyIdDataKey = ComplexMappingKey.of(legacyIdKey, NumericMappingKey.of(data));
            if (!mapping.containsKey(legacyIdDataKey)) {
                mapping.put(legacyIdDataKey, holder);
            }
        }
    }

    protected void f2lBlock(String flatteningMaterial, Map<String, String> state, int legacyId) {
        f2lBlock(flatteningMaterial, state, legacyId, 0);
    }

    protected void f2lBlock(String flatteningMaterial, Map<String, String> state, int legacyId, String alternativeLegacyName) {
        f2lBlock(flatteningMaterial, state, legacyId, 0, alternativeLegacyName);
    }

    protected void f2lBlock(String flatteningMaterial, Map<String, String> state, int legacyId, int data) {
        f2lBlock(flatteningMaterial, state, legacyId, data, null);
    }

    protected void f2lBlock(String flatteningMaterial, Map<String, String> state, int legacyId, int data, String alternativeLegacyName) {
        f2lBlock(flatteningMaterial, state, flatteningMaterial, legacyId, data, alternativeLegacyName);
    }

    protected void f2lBlock(String flatteningMaterial, Map<String, String> state, String legacyMaterial, int legacyId, int data) {
        f2lBlock(flatteningMaterial, state, legacyMaterial, legacyId, data, null);
    }

    protected void f2lBlock(String flatteningMaterial, Map<String, String> state, String legacyMaterial, int legacyId, int data, String alternativeLegacyName) {
        if (flatteningMaterial == null || legacyMaterial == null) {
            throw new IllegalArgumentException("Both flattening and legacy materials mustn't be null!");
        }
        var flatteningMaterialNamespaced = ResourceLocation.of(flatteningMaterial);
        var legacyMaterialNamespaced = ResourceLocation.of(legacyMaterial);
        var alternativeLegacyNamespaced =
                alternativeLegacyName != null && !alternativeLegacyName.equalsIgnoreCase(legacyMaterial) ? ResourceLocation.of(alternativeLegacyName) : null;
        var stateNamespaced = StringMapMappingKey.of(state);
        var completeFlatteningMaterialNamespaced = ComplexMappingKey.of(flatteningMaterialNamespaced, stateNamespaced);

        var mapping = blockTypeMapper.getUNSAFE_mapping();
        BlockTypeHolder holder = null;
        if (platform.isUsingLegacyNames() && (mapping.containsKey(legacyMaterialNamespaced) || (alternativeLegacyNamespaced != null && mapping.containsKey(alternativeLegacyNamespaced)))) {
            if (mapping.containsKey(legacyMaterialNamespaced)) {
                holder = mapping.get(legacyMaterialNamespaced).withLegacyData((byte) data);
                if (!mapping.containsKey(completeFlatteningMaterialNamespaced)/* && !flatteningMaterialNamespaced.equals(legacyMaterialNamespaced)*/) {
                    mapping.put(completeFlatteningMaterialNamespaced, holder);
                }
                if (data == 0 && alternativeLegacyNamespaced != null && !mapping.containsKey(alternativeLegacyNamespaced)) {
                    mapping.put(alternativeLegacyNamespaced, holder);
                }
                if (alternativeLegacyNamespaced != null && !mapping.containsKey(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)))) {
                    mapping.put(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)), holder);
                }
            } else if (alternativeLegacyNamespaced != null && mapping.containsKey(alternativeLegacyNamespaced)) {
                holder = mapping.get(alternativeLegacyNamespaced).withLegacyData((byte) data);
                if (!mapping.containsKey(completeFlatteningMaterialNamespaced)/* && !flatteningMaterialNamespaced.equals(legacyMaterialNamespaced)*/) {
                    mapping.put(completeFlatteningMaterialNamespaced, holder);
                }
                if (data == 0 && !mapping.containsKey(legacyMaterialNamespaced)) {
                    mapping.put(legacyMaterialNamespaced, holder);
                }
                if (!mapping.containsKey(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)))) {
                    mapping.put(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)), holder);
                }
            }
        } else if (!platform.isUsingLegacyNames() && mapping.containsKey(flatteningMaterialNamespaced)) {
            holder = mapping.get(flatteningMaterialNamespaced).withFlatteningData(state);
            if (data == 0 && !mapping.containsKey(legacyMaterialNamespaced) && !flatteningMaterialNamespaced.equals(legacyMaterialNamespaced)) {
                mapping.put(legacyMaterialNamespaced, holder);
            }
            if (!mapping.containsKey(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)))) {
                mapping.put(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)), holder);
            }
            if (data == 0 && alternativeLegacyNamespaced != null && !mapping.containsKey(alternativeLegacyNamespaced) && !flatteningMaterialNamespaced.equals(alternativeLegacyNamespaced)) {
                mapping.put(alternativeLegacyNamespaced, holder);
            }
            if (alternativeLegacyNamespaced != null && !mapping.containsKey(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)))) {
                mapping.put(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)), holder);
            }
        }

        if (holder != null) {
            var legacyIdKey = NumericMappingKey.of(legacyId);
            if (!mapping.containsKey(legacyIdKey) && data == 0) {
                mapping.put(legacyIdKey, holder);
            }
            var legacyIdDataKey = ComplexMappingKey.of(legacyIdKey, NumericMappingKey.of(data));
            if (!mapping.containsKey(legacyIdDataKey)) {
                mapping.put(legacyIdDataKey, holder);
            }
        }
    }

    public enum MappingFlags {
        NO_COLORED_BEDS
    }
}
