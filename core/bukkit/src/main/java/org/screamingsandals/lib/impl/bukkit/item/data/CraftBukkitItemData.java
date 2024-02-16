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
            keyNBTMap.put(key.asString(), Reflect.construct(ByteArrayTagAccessor.CONSTRUCTOR_0.get(), data));
        } else if (tClass == Byte.class) {
            keyNBTMap.put(key.asString(), ByteTagAccessor.METHOD_VALUE_OF.get() != null ? Reflect.fastInvoke(ByteTagAccessor.METHOD_VALUE_OF.get(), data) : Reflect.construct(ByteTagAccessor.CONSTRUCTOR_0.get(), data));
        } else if (tClass == Double.class) {
            keyNBTMap.put(key.asString(), DoubleTagAccessor.METHOD_VALUE_OF.get() != null ? Reflect.fastInvoke(DoubleTagAccessor.METHOD_VALUE_OF.get(), data) : Reflect.construct(DoubleTagAccessor.CONSTRUCTOR_0.get(), data));
        } else if (tClass == Float.class) {
            keyNBTMap.put(key.asString(), FloatTagAccessor.METHOD_VALUE_OF.get() != null ? Reflect.fastInvoke(FloatTagAccessor.METHOD_VALUE_OF.get(), data) : Reflect.construct(FloatTagAccessor.CONSTRUCTOR_0.get(), data));
        } else if (tClass == int[].class) {
            keyNBTMap.put(key.asString(), Reflect.construct(IntArrayTagAccessor.CONSTRUCTOR_0.get(), data));
        } else if (tClass == Integer.class) {
            keyNBTMap.put(key.asString(), IntTagAccessor.METHOD_VALUE_OF.get() != null ? Reflect.fastInvoke(IntTagAccessor.METHOD_VALUE_OF.get(), data) : Reflect.construct(IntTagAccessor.CONSTRUCTOR_0.get(), data));
        } else if (tClass == long[].class) {
            keyNBTMap.put(key.asString(), Reflect.construct(LongArrayTagAccessor.CONSTRUCTOR_0.get(), data));
        } else if (tClass == Long.class) {
            keyNBTMap.put(key.asString(), LongTagAccessor.METHOD_VALUE_OF.get() != null ? Reflect.fastInvoke(LongTagAccessor.METHOD_VALUE_OF.get(), data) : Reflect.construct(LongTagAccessor.CONSTRUCTOR_0.get(), data));
        } else if (tClass == Short.class) {
            keyNBTMap.put(key.asString(), ShortTagAccessor.METHOD_VALUE_OF.get() != null ? Reflect.fastInvoke(ShortTagAccessor.METHOD_VALUE_OF.get(), data) : Reflect.construct(ShortTagAccessor.CONSTRUCTOR_0.get(), data));
        } else if (tClass == String.class) {
            keyNBTMap.put(key.asString(), StringTagAccessor.METHOD_VALUE_OF.get() != null ? Reflect.fastInvoke(StringTagAccessor.METHOD_VALUE_OF.get(), data) : Reflect.construct(StringTagAccessor.CONSTRUCTOR_0.get(), data));
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

        if (ByteArrayTagAccessor.TYPE.get() == nbt.getClass() && tClass == byte[].class) {
            return (T) Reflect.fastInvoke(nbt, ByteArrayTagAccessor.METHOD_GET_AS_BYTE_ARRAY.get());
        }

        if (ByteTagAccessor.TYPE.get() == nbt.getClass() && tClass == Byte.class) {
            return (T) Reflect.fastInvoke(nbt, ByteTagAccessor.METHOD_GET_AS_BYTE.get());
        }

        if (DoubleTagAccessor.TYPE.get() == nbt.getClass() && tClass == Double.class) {
            return (T) Reflect.fastInvoke(nbt, DoubleTagAccessor.METHOD_GET_AS_DOUBLE.get());
        }

        if (FloatTagAccessor.TYPE.get() == nbt.getClass() && tClass == Float.class) {
            return (T) Reflect.fastInvoke(nbt, FloatTagAccessor.METHOD_GET_AS_FLOAT.get());
        }

        if (IntArrayTagAccessor.TYPE.get() == nbt.getClass() && tClass == int[].class) {
            return (T) Reflect.fastInvoke(nbt, IntArrayTagAccessor.METHOD_GET_AS_INT_ARRAY.get());
        }

        if (IntTagAccessor.TYPE.get() == nbt.getClass() && tClass == Integer.class) {
            return (T) Reflect.fastInvoke(nbt, IntTagAccessor.METHOD_GET_AS_INT.get());
        }

        if (LongArrayTagAccessor.TYPE.get() == nbt.getClass() && tClass == long[].class) {
            return (T) Reflect.fastInvoke(nbt, LongArrayTagAccessor.METHOD_GET_AS_LONG_ARRAY.get());
        }

        if (LongTagAccessor.TYPE.get() == nbt.getClass() && tClass == Long.class) {
            return (T) Reflect.fastInvoke(nbt, LongTagAccessor.METHOD_GET_AS_LONG.get());
        }

        if (ShortTagAccessor.TYPE.get() == nbt.getClass() && tClass == Short.class) {
            return (T) Reflect.fastInvoke(nbt, ShortTagAccessor.METHOD_GET_AS_SHORT.get());
        }

        if (tClass == String.class) {
            return (T) Reflect.fastInvoke(nbt, TagAccessor.METHOD_GET_AS_STRING.get());
        }

        // TODO: Complex arrays and maps

        return GsonUtils.gson().fromJson(Reflect.fastInvoke(nbt, TagAccessor.METHOD_GET_AS_STRING.get()).toString(), tClass); // from json
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
