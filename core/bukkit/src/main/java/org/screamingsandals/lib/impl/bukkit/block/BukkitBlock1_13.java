/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.block;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BukkitBlock1_13 extends BasicWrapper<BlockData> implements Block {

    public BukkitBlock1_13(@NotNull Material type) {
        this(type.createBlockData());
        if (!type.isBlock()) {
            throw new UnsupportedOperationException("Material must be a block!");
        }
    }

    public BukkitBlock1_13(@NotNull BlockData wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.getMaterial().name();
    }

    @Override
    public @Unmodifiable @NotNull Map<@NotNull String, String> stateData() {
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
    public @NotNull Block withStateData(@NotNull Map<@NotNull String, String> stateData) {
        final var builder = new StringBuilder();
        if (!stateData.isEmpty()) {
            builder.append('[');
            builder.append(stateData
                    .entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining(",")));
            builder.append(']');
        }
        return new BukkitBlock1_13(wrappedObject.getMaterial().createBlockData(builder.toString()));
    }

    @Override
    public @NotNull Block with(@NotNull String attribute, @NotNull String value) {
        return withStateData(new HashMap<>(stateData()) {
            {
                put(attribute, value);
            }
        });
    }

    @Override
    public @NotNull Block with(@NotNull String attribute, int value) {
        return withStateData(new HashMap<>(stateData()) {
            {
                put(attribute, String.valueOf(value));
            }
        });
    }

    @Override
    public @NotNull Block with(@NotNull String attribute, boolean value) {
        return withStateData(new HashMap<>(stateData()) {
            {
                put(attribute, String.valueOf(value));
            }
        });
    }

    @Override
    public @Nullable String get(@NotNull String attribute) {
        return stateData().get(attribute);
    }

    @Override
    public @Nullable Integer getInt(@NotNull String attribute) {
        var value = stateData().get(attribute);
        if (value != null) {
            try {
                return Integer.valueOf(value);
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

    @Override
    public @Nullable Boolean getBoolean(@NotNull String attribute) {
        var value = stateData().get(attribute);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return null;
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
        return BukkitBlockRegistry1_13.hasTagInBackPorts(wrappedObject.getMaterial(), value);
    }

    @Override
    public boolean isSameType(@Nullable Object object) {
        if (object instanceof Material) {
            return wrappedObject.getMaterial() == object;
        }
        if (object instanceof BukkitBlock1_13) {
            return wrappedObject.getMaterial() == ((BukkitBlock1_13) object).wrappedObject.getMaterial();
        }
        if (object instanceof BlockData) {
            return wrappedObject.getMaterial() == ((BlockData) object).getMaterial();
        }
        var blockType = Block.ofNullable(object);
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
        if (object instanceof BlockData || object instanceof BukkitBlock1_13) {
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
        return equals(Block.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public @NotNull String completeState() {
        return wrappedObject.getAsString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == Material.class) {
            return (T) wrappedObject.getMaterial();
        }
        return super.as(type);
    }

    @Override
    public @NotNull ResourceLocation location() {
        var key = wrappedObject.getMaterial().getKey();
        return ResourceLocation.of(key.getNamespace(), key.getKey());
    }
}
