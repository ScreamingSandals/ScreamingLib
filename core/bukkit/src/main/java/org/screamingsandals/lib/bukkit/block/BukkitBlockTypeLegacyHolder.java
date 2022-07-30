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

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.bukkit.block.converter.LegacyMaterialDataToFlatteningConverter;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

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
    public BlockTypeHolder withLegacyData(byte legacyData) {
        var clone = wrappedObject.clone();
        clone.setData(legacyData);
        return new BukkitBlockTypeLegacyHolder(clone);
    }

    @Override
    @Unmodifiable
    public Map<String, String> flatteningData() {
        return Map.of(); // TODO: some sort of conversion
    }

    @Override
    public BlockTypeHolder withFlatteningData(Map<String, String> flatteningData) {
        var materialData = wrappedObject;
        // TODO: strip all block data like axis for having consistent behaviour between legacy and flattening versions (but don't strip block variants!!)
        for (var e : flatteningData.entrySet()) {
            materialData = LegacyMaterialDataToFlatteningConverter.set(materialData, e.getKey(), e.getValue());
        }
        return new BukkitBlockTypeLegacyHolder(materialData);
    }

    @Override
    public BlockTypeHolder with(String attribute, String value) {
        return new BukkitBlockTypeLegacyHolder(LegacyMaterialDataToFlatteningConverter.set(wrappedObject, attribute, value));
    }

    @Override
    public BlockTypeHolder with(String attribute, int value) {
        return new BukkitBlockTypeLegacyHolder(LegacyMaterialDataToFlatteningConverter.set(wrappedObject, attribute, String.valueOf(value)));
    }

    @Override
    public BlockTypeHolder with(String attribute, boolean value) {
        return new BukkitBlockTypeLegacyHolder(LegacyMaterialDataToFlatteningConverter.set(wrappedObject, attribute, String.valueOf(value)));
    }

    @Override
    public Optional<String> get(String attribute) {
        return Optional.empty(); // TODO: some sort of conversion
    }

    @Override
    public Optional<Integer> getInt(String attribute) {
        return Optional.empty(); // TODO: some sort of conversion
    }

    @Override
    public Optional<Boolean> getBoolean(String attribute) {
        return Optional.empty(); // TODO: some sort of conversion
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
        if (object instanceof MaterialData) {
            // TODO: This is complicated because we have variants, however as legacy versions are no longer in dev, we can just hardcode them here
            return wrappedObject.getItemType() == ((MaterialData) object).getItemType();
        }
        if (object instanceof BukkitBlockTypeLegacyHolder) {
            // TODO: This is complicated because we have variants, however as legacy versions are no longer in dev, we can just hardcode them here
            return wrappedObject.getItemType() == ((BukkitBlockTypeLegacyHolder) object).wrappedObject.getItemType();
        }
        return BlockTypeHolder.ofOptional(object).map(h -> h.platformName().equals(platformName())).orElse(false);
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
        return equals(BlockTypeHolder.ofOptional(object).orElse(null));
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
