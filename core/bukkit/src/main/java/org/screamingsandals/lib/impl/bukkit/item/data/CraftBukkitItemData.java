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

package org.screamingsandals.lib.impl.bukkit.item.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.BukkitCore;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.impl.nms.accessors.*;
import org.screamingsandals.lib.impl.utils.GsonUtils;
import org.screamingsandals.lib.impl.utils.Primitives;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CraftBukkitItemData implements ItemData {
    @Getter
    private final @NotNull Map<@NotNull String, Object> keyNBTMap;

    @Override
    public @NotNull Set<@NotNull ResourceLocation> getKeys() {
        return keyNBTMap.keySet().stream().map(ResourceLocation::of).collect(Collectors.toSet());
    }

    @Override
    public <T> void set(@NotNull String key, @NotNull T data, @NotNull Class<T> tClass) {
        set(ResourceLocation.of(BukkitCore.getPlugin().getName().toLowerCase(Locale.ROOT), key.toLowerCase(Locale.ROOT)), data, tClass);
    }

    @Override
    public <T> void set(@NotNull ResourceLocation key, @NotNull T data, @NotNull Class<T> tClass) {
        if (!Primitives.isWrapperType(tClass)) {
            tClass = Primitives.wrap(tClass); //Make sure we will always "switch" over the wrapper types
        }

        if (tClass == byte[].class) {
            keyNBTMap.put(key.asString(), Reflect.construct(ByteArrayTagAccessor.getConstructor0(), data));
        } else if (tClass == Byte.class) {
            keyNBTMap.put(key.asString(), ByteTagAccessor.getMethodValueOf1() != null ? Reflect.fastInvoke(ByteTagAccessor.getMethodValueOf1(), data) : Reflect.construct(ByteTagAccessor.getConstructor0(), data));
        } else if (tClass == Double.class) {
            keyNBTMap.put(key.asString(), DoubleTagAccessor.getMethodValueOf1() != null ? Reflect.fastInvoke(DoubleTagAccessor.getMethodValueOf1(), data) : Reflect.construct(DoubleTagAccessor.getConstructor0(), data));
        } else if (tClass == Float.class) {
            keyNBTMap.put(key.asString(), FloatTagAccessor.getMethodValueOf1() != null ? Reflect.fastInvoke(FloatTagAccessor.getMethodValueOf1(), data) : Reflect.construct(FloatTagAccessor.getConstructor0(), data));
        } else if (tClass == int[].class) {
            keyNBTMap.put(key.asString(), Reflect.construct(IntArrayTagAccessor.getConstructor0(), data));
        } else if (tClass == Integer.class) {
            keyNBTMap.put(key.asString(), IntTagAccessor.getMethodValueOf1() != null ? Reflect.fastInvoke(IntTagAccessor.getMethodValueOf1(), data) : Reflect.construct(IntTagAccessor.getConstructor0(), data));
        } else if (tClass == long[].class) {
            keyNBTMap.put(key.asString(), Reflect.construct(LongArrayTagAccessor.getConstructor0(), data));
        } else if (tClass == Long.class) {
            keyNBTMap.put(key.asString(), LongTagAccessor.getMethodValueOf1() != null ? Reflect.fastInvoke(LongTagAccessor.getMethodValueOf1(), data) : Reflect.construct(LongTagAccessor.getConstructor0(), data));
        } else if (tClass == Short.class) {
            keyNBTMap.put(key.asString(), ShortTagAccessor.getMethodValueOf1() != null ? Reflect.fastInvoke(ShortTagAccessor.getMethodValueOf1(), data) : Reflect.construct(ShortTagAccessor.getConstructor0(), data));
        } else if (tClass == String.class) {
            keyNBTMap.put(key.asString(), StringTagAccessor.getMethodValueOf1() != null ? Reflect.fastInvoke(StringTagAccessor.getMethodValueOf1(), data) : Reflect.construct(StringTagAccessor.getConstructor0(), data));
        } else {
            keyNBTMap.put(key.asString(), GsonUtils.gson().toJson(data)); // to json
        }
    }

    @Override
    public <T> @Nullable T get(@NotNull String key, @NotNull Class<T> tClass) {
        return get(ResourceLocation.of(BukkitCore.getPlugin().getName().toLowerCase(Locale.ROOT), key.toLowerCase(Locale.ROOT)), tClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> @Nullable T get(@NotNull ResourceLocation key, @NotNull Class<T> tClass) {
        if (!Primitives.isWrapperType(tClass)) {
            tClass = Primitives.wrap(tClass); //Make sure we will always "switch" over the wrapper types
        }

        var nbt = keyNBTMap.get(key.asString());
        if (nbt == null) {
            return null;
        }

        if (ByteArrayTagAccessor.getType() == nbt.getClass() && tClass == byte[].class) {
            return (T) Reflect.fastInvoke(nbt, ByteArrayTagAccessor.getMethodGetAsByteArray1());
        }

        if (ByteTagAccessor.getType() == nbt.getClass() && tClass == Byte.class) {
            return (T) Reflect.fastInvoke(nbt, ByteTagAccessor.getMethodGetAsByte1());
        }

        if (DoubleTagAccessor.getType() == nbt.getClass() && tClass == Double.class) {
            return (T) Reflect.fastInvoke(nbt, DoubleTagAccessor.getMethodGetAsDouble1());
        }

        if (FloatTagAccessor.getType() == nbt.getClass() && tClass == Float.class) {
            return (T) Reflect.fastInvoke(nbt, FloatTagAccessor.getMethodGetAsFloat1());
        }

        if (IntArrayTagAccessor.getType() == nbt.getClass() && tClass == int[].class) {
            return (T) Reflect.fastInvoke(nbt, IntArrayTagAccessor.getMethodGetAsIntArray1());
        }

        if (IntTagAccessor.getType() == nbt.getClass() && tClass == Integer.class) {
            return (T) Reflect.fastInvoke(nbt, IntTagAccessor.getMethodGetAsInt1());
        }

        if (LongArrayTagAccessor.getType() == nbt.getClass() && tClass == long[].class) {
            return (T) Reflect.fastInvoke(nbt, LongArrayTagAccessor.getMethodGetAsLongArray1());
        }

        if (LongTagAccessor.getType() == nbt.getClass() && tClass == Long.class) {
            return (T) Reflect.fastInvoke(nbt, LongTagAccessor.getMethodGetAsLong1());
        }

        if (ShortTagAccessor.getType() == nbt.getClass() && tClass == Short.class) {
            return (T) Reflect.fastInvoke(nbt, ShortTagAccessor.getMethodGetAsShort1());
        }

        if (tClass == String.class) {
            return (T) Reflect.fastInvoke(nbt, TagAccessor.getMethodGetAsString1());
        }

        // TODO: Complex arrays and maps

        return GsonUtils.gson().fromJson(Reflect.fastInvoke(nbt, TagAccessor.getMethodGetAsString1()).toString(), tClass); // from json
    }

    @Override
    public boolean contains(@NotNull String key) {
        return keyNBTMap.containsKey(ResourceLocation.of(BukkitCore.getPlugin().getName().toLowerCase(Locale.ROOT), key.toLowerCase(Locale.ROOT)).asString());
    }

    @Override
    public boolean contains(@NotNull ResourceLocation key) {
        return keyNBTMap.containsKey(key.asString());
    }


    @Override
    public boolean isEmpty() {
        return keyNBTMap.isEmpty();
    }
}
