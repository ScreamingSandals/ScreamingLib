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

import com.destroystokyo.paper.MaterialTags;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.bukkit.BukkitCore;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BukkitBlockTypeHolder extends BasicWrapper<BlockData> implements BlockTypeHolder {

    public static boolean NAG_AUTHOR_ABOUT_LEGACY_METHOD_USED;
    private static final String NAG_AUTHOR_ABOUT_LEGACY_METHOD_USED_MESSAGE = "Nag author/s of this plugin about usage of BlockTypeHolder#legacyData() or #withLegacyData() in non-legacy environment!";

    public BukkitBlockTypeHolder(Material type) {
        this(type.createBlockData());
        if (!type.isBlock()) {
            throw new UnsupportedOperationException("Material must be a block!");
        }
    }

    public BukkitBlockTypeHolder(BlockData wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.getMaterial().name();
    }

    @Override
    public byte legacyData() {
        if (!NAG_AUTHOR_ABOUT_LEGACY_METHOD_USED) {
            BukkitCore.getPlugin().getLogger().warning(NAG_AUTHOR_ABOUT_LEGACY_METHOD_USED_MESSAGE);
            NAG_AUTHOR_ABOUT_LEGACY_METHOD_USED = true;
        }
        // Thanks Bukkit for not exposing this black magic :(
        try {
            var legacy = Reflect.getMethod(ClassStorage.CB.UNSAFE_EVIL_GET_OUT_getCraftLegacy(), "toLegacyData", Material.class).invokeStatic(wrappedObject.getMaterial());
            if (legacy instanceof MaterialData) {
                return ((MaterialData) legacy).getData();
            }
        } catch (Throwable ignored) {
        }
        return 0;
    }

    @Override
    public BlockTypeHolder withLegacyData(byte legacyData) {
        if (!NAG_AUTHOR_ABOUT_LEGACY_METHOD_USED) {
            BukkitCore.getPlugin().getLogger().warning(NAG_AUTHOR_ABOUT_LEGACY_METHOD_USED_MESSAGE);
            NAG_AUTHOR_ABOUT_LEGACY_METHOD_USED = true;
        }
        // Thanks Bukkit for exposing this black magic :)
        try {
            var legacy = Bukkit.getUnsafe().toLegacy(wrappedObject.getMaterial());
            if (legacy != null && legacy.isLegacy() && legacy != Material.LEGACY_AIR) {
                return new BukkitBlockTypeHolder(Bukkit.getUnsafe().fromLegacy(legacy, legacyData));
            }
        } catch (Throwable ignored) {
        }
        return new BukkitBlockTypeHolder(wrappedObject.clone());
    }

    @Override
    @Unmodifiable
    public Map<String, String> flatteningData() {
        var data = wrappedObject.getAsString();
        if (data.contains("[") && data.contains("]")) {
            final var values = data.substring(data.indexOf("[") + 1, data.lastIndexOf("]"));
            if (values.isEmpty()) {
                return Map.of();
            }
            return Arrays.stream(values.split(","))
                    .map(next -> next.split("="))
                    .collect(Collectors.toMap(next -> next[0], next1 -> next1[1]));
        }
        return Map.of();
    }

    @Override
    public BlockTypeHolder withFlatteningData(Map<String, String> data) {
        final var builder = new StringBuilder();
        if (data != null && !data.isEmpty()) {
            builder.append('[');
            builder.append(data
                    .entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining(",")));
            builder.append(']');
        }
        return new BukkitBlockTypeHolder(wrappedObject.getMaterial().createBlockData(builder.toString()));
    }

    @Override
    public BlockTypeHolder with(String attribute, String value) {
        return withFlatteningData(new HashMap<>(flatteningData()) {
            {
                put(attribute, value);
            }
        });
    }

    @Override
    public BlockTypeHolder with(String attribute, int value) {
        return withFlatteningData(new HashMap<>(flatteningData()) {
            {
                put(attribute, String.valueOf(value));
            }
        });
    }

    @Override
    public BlockTypeHolder with(String attribute, boolean value) {
        return withFlatteningData(new HashMap<>(flatteningData()) {
            {
                put(attribute, String.valueOf(value));
            }
        });
    }

    @Override
    public Optional<String> get(String attribute) {
        return Optional.ofNullable(flatteningData().get(attribute));
    }

    @Override
    public Optional<Integer> getInt(String attribute) {
        return Optional.ofNullable(flatteningData().get(attribute)).flatMap(s -> {
            try {
                return Optional.of(Integer.valueOf(s));
            } catch (Throwable ignored) {
                return Optional.empty();
            }
        });
    }

    @Override
    public Optional<Boolean> getBoolean(String attribute) {
        return Optional.ofNullable(flatteningData().get(attribute)).map(Boolean::parseBoolean);
    }

    @Override
    public boolean isSolid() {
        return wrappedObject.getMaterial().isSolid();
    }

    @Override
    public boolean isTransparent() {
        return wrappedObject.getMaterial().isTransparent();
    }

    @Override
    public boolean isFlammable() {
        return wrappedObject.getMaterial().isFlammable();
    }

    @Override
    public boolean isBurnable() {
        return wrappedObject.getMaterial().isBurnable();
    }

    @Override
    public boolean isOccluding() {
        return wrappedObject.getMaterial().isOccluding();
    }

    @Override
    public boolean hasGravity() {
        return wrappedObject.getMaterial().hasGravity();
    }

    @Override
    public boolean hasTag(@NotNull Object tag) {
        NamespacedMappingKey key;
        if (tag instanceof NamespacedMappingKey) {
            key = (NamespacedMappingKey) tag;
        } else {
            key = NamespacedMappingKey.of(tag.toString());
        }
        // native tags
        var bukkitTag = Bukkit.getTag(Tag.REGISTRY_BLOCKS, new NamespacedKey(key.namespace(), key.value()), Material.class);
        if (bukkitTag != null) {
            return bukkitTag.isTagged(wrappedObject.getMaterial());
        }
        // backported tags
        if (!key.namespace().equals("minecraft")) {
            return false;
        }
        var value = key.value();
        return BukkitBlockTypeMapper.hasTagInBackPorts(wrappedObject.getMaterial(), value);
    }

    @Override
    public boolean isSameType(Object object) {
        if (object instanceof Material) {
            return wrappedObject.getMaterial() == object;
        }
        if (object instanceof BukkitBlockTypeHolder) {
            return wrappedObject.getMaterial() == ((BukkitBlockTypeHolder) object).wrappedObject.getMaterial();
        }
        if (object instanceof BlockData) {
            return wrappedObject.getMaterial() == ((BlockData) object).getMaterial();
        }
        return BlockTypeHolder.ofOptional(object).map(h -> h.platformName().equals(platformName())).orElse(false);
    }

    @Override
    public boolean isSameType(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::isSameType);
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof BlockData || object instanceof BukkitBlockTypeHolder) {
            return equals(object);
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
            return (T) wrappedObject.getMaterial();
        }
        return super.as(type);
    }
}
