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

package org.screamingsandals.lib.bukkit.block;

import lombok.experimental.ExtensionMethod;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.block.BlockType;
import org.screamingsandals.lib.bukkit.BukkitCore;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.utils.key.ResourceLocation;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitBlockType1_13 extends BasicWrapper<BlockData> implements BlockType {

    public static boolean NAG_AUTHOR_ABOUT_LEGACY_METHOD_USED;
    private static final @NotNull String NAG_AUTHOR_ABOUT_LEGACY_METHOD_USED_MESSAGE = "Nag author/s of this plugin about usage of BlockTypeHolder#legacyData() or #withLegacyData() in non-legacy environment!";

    public BukkitBlockType1_13(@NotNull Material type) {
        this(type.createBlockData());
        if (!type.isBlock()) {
            throw new UnsupportedOperationException("Material must be a block!");
        }
    }

    public BukkitBlockType1_13(@NotNull BlockData wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
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
    public @NotNull BlockType withLegacyData(byte legacyData) {
        if (!NAG_AUTHOR_ABOUT_LEGACY_METHOD_USED) {
            BukkitCore.getPlugin().getLogger().warning(NAG_AUTHOR_ABOUT_LEGACY_METHOD_USED_MESSAGE);
            NAG_AUTHOR_ABOUT_LEGACY_METHOD_USED = true;
        }
        // Thanks Bukkit for exposing this black magic :)
        try {
            var legacy = Bukkit.getUnsafe().toLegacy(wrappedObject.getMaterial());
            if (legacy != null && legacy.isLegacy() && legacy != Material.LEGACY_AIR) {
                return new BukkitBlockType1_13(Bukkit.getUnsafe().fromLegacy(legacy, legacyData));
            }
        } catch (Throwable ignored) {
        }
        return new BukkitBlockType1_13(wrappedObject.clone());
    }

    @Override
    public @Unmodifiable @NotNull Map<@NotNull String, String> flatteningData() {
        var data = wrappedObject.getAsString();
        if (data.contains("[") && data.contains("]")) {
            final var values = data.substring(data.indexOf("[") + 1, data.lastIndexOf("]"));
            if (values.isEmpty()) {
                return Map.of();
            }
            return Arrays.stream(values.split(","))
                    .map(next -> next.split("="))
                    .collect(Collectors.toUnmodifiableMap(next -> next[0], next1 -> next1[1]));
        }
        return Map.of();
    }

    @Override
    public @NotNull BlockType withFlatteningData(@NotNull Map<@NotNull String, String> data) {
        final var builder = new StringBuilder();
        if (!data.isEmpty()) {
            builder.append('[');
            builder.append(data
                    .entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining(",")));
            builder.append(']');
        }
        return new BukkitBlockType1_13(wrappedObject.getMaterial().createBlockData(builder.toString()));
    }

    @Override
    public @NotNull BlockType with(@NotNull String attribute, @NotNull String value) {
        return withFlatteningData(new HashMap<>(flatteningData()) {
            {
                put(attribute, value);
            }
        });
    }

    @Override
    public @NotNull BlockType with(@NotNull String attribute, int value) {
        return withFlatteningData(new HashMap<>(flatteningData()) {
            {
                put(attribute, String.valueOf(value));
            }
        });
    }

    @Override
    public @NotNull BlockType with(@NotNull String attribute, boolean value) {
        return withFlatteningData(new HashMap<>(flatteningData()) {
            {
                put(attribute, String.valueOf(value));
            }
        });
    }

    @Override
    public @Nullable String get(@NotNull String attribute) {
        return flatteningData().get(attribute);
    }

    @Override
    public @Nullable Integer getInt(@NotNull String attribute) {
        return flatteningData().get(attribute).mapOrNull(s -> {
            try {
                return Integer.valueOf(s);
            } catch (Throwable ignored) {
            }
            return null;
        });
    }

    @Override
    public @Nullable Boolean getBoolean(@NotNull String attribute) {
        return flatteningData().get(attribute).mapOrNull(Boolean::parseBoolean);
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
        ResourceLocation key;
        if (tag instanceof ResourceLocation) {
            key = (ResourceLocation) tag;
        } else {
            key = ResourceLocation.of(tag.toString());
        }
        // native tags
        var bukkitTag = Bukkit.getTag(Tag.REGISTRY_BLOCKS, new NamespacedKey(key.namespace(), key.path()), Material.class);
        if (bukkitTag != null) {
            return bukkitTag.isTagged(wrappedObject.getMaterial());
        }
        // backported tags
        if (!"minecraft".equals(key.namespace())) {
            return false;
        }
        var value = key.path();
        return BukkitBlockTypeMapper.hasTagInBackPorts(wrappedObject.getMaterial(), value);
    }

    @Override
    public boolean isSameType(@Nullable Object object) {
        if (object instanceof Material) {
            return wrappedObject.getMaterial() == object;
        }
        if (object instanceof BukkitBlockType1_13) {
            return wrappedObject.getMaterial() == ((BukkitBlockType1_13) object).wrappedObject.getMaterial();
        }
        if (object instanceof BlockData) {
            return wrappedObject.getMaterial() == ((BlockData) object).getMaterial();
        }
        var blockType = BlockType.ofNullable(object);
        if (blockType == null) {
            return false;
        }
        return blockType.platformName().equals(platformName());
    }

    @Override
    public boolean isSameType(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::isSameType);
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof BlockData || object instanceof BukkitBlockType1_13) {
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
        return equals(BlockType.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == Material.class) {
            return (T) wrappedObject.getMaterial();
        }
        return super.as(type);
    }
}
