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

package org.screamingsandals.lib.bukkit.block;

import lombok.experimental.ExtensionMethod;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.bukkit.block.converter.LegacyMaterialDataToFlatteningConverter;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;
import java.util.Map;

@ExtensionMethod(value = {NullableExtension.class}, suppressBaseMethods = false)
public class BukkitBlockTypeLegacyHolder extends BasicWrapper<MaterialData> implements BlockTypeHolder {

    public BukkitBlockTypeLegacyHolder(Material material) {
        this(material.getNewData((byte) 0));
    }

    public BukkitBlockTypeLegacyHolder(Material material, byte legacyData) {
        this(material.getNewData(legacyData));
    }

    public BukkitBlockTypeLegacyHolder(MaterialData wrappedObject) {
        super(wrappedObject);
        if (!wrappedObject.getItemType().isBlock()) {
            throw new UnsupportedOperationException("Material must be a block!");
        }
    }

    @Override
    public String platformName() {
        return wrappedObject.getItemType().name();
    }

    @Override
    public byte legacyData() {
        return wrappedObject.getData();
    }

    @Override
    @NotNull
    public BlockTypeHolder withLegacyData(byte legacyData) {
        var clone = wrappedObject.clone();
        clone.setData(legacyData);
        return new BukkitBlockTypeLegacyHolder(clone);
    }

    @Override
    @Unmodifiable
    @NotNull
    public Map<@NotNull String, @NotNull String> flatteningData() {
        return LegacyMaterialDataToFlatteningConverter.get(wrappedObject);
    }

    @Override
    @NotNull
    public BlockTypeHolder withFlatteningData(@NotNull Map<@NotNull String, @NotNull String> flatteningData) {
        var materialName = wrappedObject.getItemType().name();
        byte baseData = 0;

        // Code below makes sure the comparison behaves the same on legacy as on flattening

        // Materials (lit/unlit > default lit state, double slab > slab, retarded invalid comparator > valid comparator)
        switch (materialName) {
            case "BURNING_FURNACE":
                materialName = "FURNACE";
                break;
            case "DIODE_BLOCK_ON":
                materialName = "DIODE_BLOCK_OFF";
                break;
            case "REDSTONE_COMPARATOR_ON":
                materialName = "REDSTONE_COMPARATOR_OFF";
                break;
            case "DAYLIGHT_DETECTOR_INVERTED":
                materialName = "DAYLIGHT_DETECTOR";
                break;
            case "REDSTONE_TORCH_OFF":
                materialName = "REDSTONE_TORCH_ON";
                break;
            case "REDSTONE_LAMP_ON":
                materialName = "REDSTONE_LAMP_OFF";
                break;
            case "DOUBLE_STEP":
                materialName = "STEP";
                break;
            case "WOOD_DOUBLE_STEP":
                materialName = "WOOD_STEP";
                break;
            case "DOUBLE_STONE_SLAB2":
                materialName = "STONE_SLAB2";
                break;
            case "PURPUR_DOUBLE_SLAB":
                materialName = "PURPUR_SLAB";
                break;
        }

        // State data (persist variant)
        switch (materialName) {
            case "LOG":
            case "LOG_2":
            case "LEAVES":
            case "LEAVES_2": // wood type
                baseData = (byte) (wrappedObject.getData() & 0x3); // 0x2 and 0x1 bits (together 0x3) are used for wood type
                break;
            case "SAPLING": // sapling type
            case "STEP":
            case "WOOD_STEP":
            case "STONE_SLAB2":
            case "PURPUR_SLAB": // slab material
            case "DOUBLE_PLANT": // different tall flowers
                baseData = (byte) (wrappedObject.getData() & 0x7); // only 0x8 bit is used for state data, the rest is variant
                break;
            case "SAND": // sand and red_sand
            case "LONG_GRASS": // tall_grass and fern
            case "RED_ROSE": // different small flowers
            case "WOOL":
            case "STAINED_CLAY":
            case "STAINED_GLASS":
            case "STAINED_GLASS_PANE":
            case "CARPET":
            case "CONCRETE":
            case "CONCRETE_POWDER": // color
            case "SMOOTH_BRICK": // different stone bricks
            case "MONSTER_EGGS": // infested blocks
            case "PRISMARINE": // different prismarine blocks
            case "SPONGE": // sponge and wet_sponge
            case "COBBLE_WALL": // cobblestone_wall and mossy_cobblestone_wall
            case "WOOD": // planks
            case "DIRT": // dirt, coarse_dirt, podzol
                baseData = wrappedObject.getData(); // all bits are used for variant
                break;
            case "TORCH":
            case "REDSTONE_TORCH_ON":
                baseData = (byte) ((wrappedObject.getData() == 0x5) ? 0x5 : 0x0); // torches and wall torches
                break;
            case "SKULL": // TODO: tile entity data (in flattening split by mob type)
                baseData = (byte) ((wrappedObject.getData() == 0x1) ? 0x1 : 0x0); // wall head and floor head
                break;
            case "QUARTZ_BLOCK":
                baseData = wrappedObject.getData() > 0x1 ? (byte) 0x2 : wrappedObject.getData(); // > 0x1 is pillar, 0x0 and 0x1 are another blocks
                break;
            case "ANVIL":
                baseData = (byte) (wrappedObject.getData() & 0xC); // 0x1 and 0x2 are used for additional state data, 0x8 and 0x4 (together 0xC) are used for variant
                break;
            //case "FLOWER_POT": // TODO: tile entity data (in flattening split by flower type)
            //case "BED_BLOCK": // TODO: tile entity data (in flattening split by color)
        }

        var materialData = Material.valueOf(materialName).getNewData(baseData);
        for (var e : flatteningData.entrySet()) {
            materialData = LegacyMaterialDataToFlatteningConverter.set(materialData, e.getKey(), e.getValue());
        }
        return new BukkitBlockTypeLegacyHolder(materialData);
    }

    @Override
    @NotNull
    public BlockTypeHolder with(@NotNull String attribute, @NotNull String value) {
        return new BukkitBlockTypeLegacyHolder(LegacyMaterialDataToFlatteningConverter.set(wrappedObject, attribute, value));
    }

    @Override
    @NotNull
    public BlockTypeHolder with(@NotNull String attribute, int value) {
        return new BukkitBlockTypeLegacyHolder(LegacyMaterialDataToFlatteningConverter.set(wrappedObject, attribute, String.valueOf(value)));
    }

    @Override
    @NotNull
    public BlockTypeHolder with(@NotNull String attribute, boolean value) {
        return new BukkitBlockTypeLegacyHolder(LegacyMaterialDataToFlatteningConverter.set(wrappedObject, attribute, String.valueOf(value)));
    }

    @Override
    public @Nullable String get(@NotNull String attribute) {
        return LegacyMaterialDataToFlatteningConverter.get(wrappedObject, attribute);
    }

    @Override
    public @Nullable Integer getInt(@NotNull String attribute) {
        return LegacyMaterialDataToFlatteningConverter.get(wrappedObject, attribute).mapOrNull(s -> {
            try {
                return Integer.valueOf(s);
            } catch (Throwable ignored) {
            }
            return null;
        });
    }

    @Override
    public @Nullable Boolean getBoolean(@NotNull String attribute) {
        return LegacyMaterialDataToFlatteningConverter.get(wrappedObject, attribute).mapOrNull(Boolean::parseBoolean);
    }

    @Override
    public boolean isSolid() {
        return wrappedObject.getItemType().isSolid();
    }

    @Override
    public boolean isTransparent() {
        return wrappedObject.getItemType().isTransparent();
    }

    @Override
    public boolean isFlammable() {
        return wrappedObject.getItemType().isFlammable();
    }

    @Override
    public boolean isBurnable() {
        return wrappedObject.getItemType().isBurnable();
    }

    @Override
    public boolean isOccluding() {
        return wrappedObject.getItemType().isOccluding();
    }

    @Override
    public boolean hasGravity() {
        return wrappedObject.getItemType().hasGravity();
    }

    @Override
    public boolean hasTag(@NotNull Object tag) {
        NamespacedMappingKey key;
        if (tag instanceof NamespacedMappingKey) {
            key = (NamespacedMappingKey) tag;
        } else {
            key = NamespacedMappingKey.of(tag.toString());
        }
        if (!key.namespace().equals("minecraft")) {
            return false;
        }
        var value = key.value();
        return BukkitBlockTypeMapper.hasTagInBackPorts(wrappedObject.getItemType(), value);
    }

    @Override
    public boolean isSameType(Object object) {
        if (object instanceof Material) {
            return wrappedObject.getItemType() == object;
        }

        MaterialData material;
        if (object instanceof MaterialData) {
            material = (MaterialData) object;
        } else if (object instanceof BukkitBlockTypeLegacyHolder) {
            material = ((BukkitBlockTypeLegacyHolder) object).wrappedObject;
        } else {
            var block = BlockTypeHolder.ofNullable(object);
            if (block == null) {
                return false;
            }
            material = ((BukkitBlockTypeLegacyHolder) block).wrappedObject;
        }
        var materialName = material.getItemType().name();
        var ourMaterialName = wrappedObject.getItemType().name();

        // Code below makes sure the comparison behaves the same on legacy as on flattening

        // Materials
        switch (materialName) {
            case "FURNACE":
            case "BURNING_FURNACE":
                if ("FURNACE".equals(ourMaterialName) || "BURNING_FURNACE".equals(ourMaterialName)) {
                    return true; // no data needs to be checked as all data are just state data
                }
                break;
            case "DIODE_BLOCK_OFF":
            case "DIODE_BLOCK_ON":
                if ("DIODE_BLOCK_OFF".equals(ourMaterialName) || "DIODE_BLOCK_ON".equals(ourMaterialName)) {
                    return true; // no data needs to be checked as all data are just state data
                }
                break;
            case "REDSTONE_COMPARATOR_ON":
            case "REDSTONE_COMPARATOR_OFF":
                if ("REDSTONE_COMPARATOR_ON".equals(ourMaterialName) || "REDSTONE_COMPARATOR_OFF".equals(ourMaterialName)) {
                    return true; // no data needs to be checked as all data are just state data
                }
                break;
            case "DAYLIGHT_DETECTOR":
            case "DAYLIGHT_DETECTOR_INVERTED":
                if ("DAYLIGHT_DETECTOR".equals(ourMaterialName) || "DAYLIGHT_DETECTOR_INVERTED".equals(ourMaterialName)) {
                    return true; // no data needs to be checked as all data are just state data
                }
                break;
            case "REDSTONE_TORCH_OFF":
            case "REDSTONE_TORCH_ON":
                if ("REDSTONE_TORCH_OFF".equals(ourMaterialName) || "REDSTONE_TORCH_ON".equals(ourMaterialName)) {
                    return true; // no data needs to be checked as all data are just state data
                }
                break;
            case "REDSTONE_LAMP_ON":
            case "REDSTONE_LAMP_OFF":
                if ("REDSTONE_LAMP_ON".equals(ourMaterialName) || "REDSTONE_LAMP_OFF".equals(ourMaterialName)) {
                    return true; // no data needs to be checked as all data are just state data
                }
                break;
            case "STEP":
            case "DOUBLE_STEP":
                if (!"STEP".equals(ourMaterialName) && !"DOUBLE_STEP".equals(ourMaterialName)) {
                    return false; // only check if our material is one of these and return false if not
                }
                break;
            case "WOOD_STEP":
            case "WOOD_DOUBLE_STEP":
                if (!"WOOD_STEP".equals(ourMaterialName) && !"WOOD_DOUBLE_STEP".equals(ourMaterialName)) {
                    return false; // only check if our material is one of these and return false if not
                }
                break;
            case "STONE_SLAB2":
            case "DOUBLE_STONE_SLAB2":
                if (!"STONE_SLAB2".equals(ourMaterialName) && !"DOUBLE_STONE_SLAB2".equals(ourMaterialName)) {
                    return false; // only check if our material is one of these and return false if not
                }
                break;
            case "PURPUR_SLAB":
            case "PURPUR_DOUBLE_SLAB":
                if (!"PURPUR_SLAB".equals(ourMaterialName) && !"PURPUR_DOUBLE_SLAB".equals(ourMaterialName)) {
                    return false; // only check if our material is one of these and return false if not
                }
                break;
            default:
                if (wrappedObject.getItemType() != material.getItemType()) {
                    return false;
                }
                break;
        }

        // State data
        switch (materialName) {
            case "LOG":
            case "LOG_2":
            case "LEAVES":
            case "LEAVES_2": // wood type
                return (material.getData() & 0x3) == (wrappedObject.getData() & 0x3); // 0x8 and 0x4 bits are used for additional state data and not variant
            case "SAPLING": // sapling type
            case "STEP":
            case "WOOD_STEP":
            case "STONE_SLAB2":
            case "PURPUR_SLAB":
            case "DOUBLE_STEP":
            case "WOOD_DOUBLE_STEP":
            case "DOUBLE_STONE_SLAB2":
            case "PURPUR_DOUBLE_SLAB": // slab material
            case "DOUBLE_PLANT": // different tall flowers
                return (material.getData() & 0x7) == (wrappedObject.getData() & 0x7); // 0x8 bit is used for additional state data and not variant
            case "SAND": // sand and red_sand
            case "LONG_GRASS": // tall_grass and fern
            case "RED_ROSE": // different small flowers
            case "WOOL":
            case "STAINED_CLAY":
            case "STAINED_GLASS":
            case "STAINED_GLASS_PANE":
            case "CARPET":
            case "CONCRETE":
            case "CONCRETE_POWDER": // color
            case "SMOOTH_BRICK": // different stone bricks
            case "MONSTER_EGGS": // infested blocks
            case "PRISMARINE": // different prismarine blocks
            case "SPONGE": // sponge and wet_sponge
            case "COBBLE_WALL": // cobblestone_wall and mossy_cobblestone_wall
            case "WOOD": // planks
            case "DIRT": // dirt, coarse_dirt, podzol
                return material.getData() == wrappedObject.getData(); // all bits are used for variant
            case "TORCH":
            case "REDSTONE_TORCH_ON":
            case "REDSTONE_TORCH_OFF":
                return (material.getData() == 0x5 && wrappedObject.getData() == 0x5) || (material.getData() != 0x5 && wrappedObject.getData() != 0x5); // torches and wall torches
            case "SKULL": // TODO: tile entity data (in flattening split by mob type)
                return (material.getData() == 0x1 && wrappedObject.getData() == 0x1) || (material.getData() != 0x1 && wrappedObject.getData() != 0x1); // <mob_type>_head and wall_<mob_type>_head
            case "QUARTZ_BLOCK":
                return (material.getData() > 0x1 && wrappedObject.getData() > 0x1) || material.getData() == wrappedObject.getData(); // > 0x1 is pillar, 0x0 and 0x1 are another blocks
            case "ANVIL":
                return (material.getData() & 0xC) == (wrappedObject.getData() & 0xC); // 0x1 and 0x2 are used for additional state data, 0x8 and 0x4 (together 0xC) are used for variant
            //case "FLOWER_POT": // TODO: tile entity data (in flattening split by flower type)
            //case "BED_BLOCK": // TODO: tile entity data (in flattening split by color)
            default:
                return true; // all bits are used for state data
        }
    }

    @Override
    public boolean isSameType(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::isSameType);
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof MaterialData || object instanceof BukkitBlockTypeLegacyHolder) {
            return equals(object);
        }
        if (object instanceof String) {
            var str = (String) object;
            if (str.startsWith("#")) {
                // seems like a tag
                return hasTag(str.substring(1));
            } else if (str.endsWith("[*]")) {
                return isSameType(str.substring(0, str.length() - 3));
            }
        }
        return equals(BlockTypeHolder.ofNullable(object));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type == Material.class) {
            return (T) wrappedObject.getItemType();
        }
        return super.as(type);
    }
}
