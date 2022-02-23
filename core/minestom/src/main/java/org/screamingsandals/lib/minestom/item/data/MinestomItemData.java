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

package org.screamingsandals.lib.minestom.item.data;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.json.NBTGsonReader;
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.utils.GsonUtils;
import org.screamingsandals.lib.utils.Primitives;

import java.io.StringReader;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class MinestomItemData implements ItemData {
    private final MutableNBTCompound nbtCompound;

    @Override
    public Set<String> getKeys() {
        return nbtCompound.getKeys();
    }

    @Override
    public <T> void set(String key, T data, Class<T> tClass) {
        if (!Primitives.isWrapperType(tClass)) {
            tClass = Primitives.wrap(tClass); // Make sure we will always "switch" over the wrapper types
        }

        if (tClass == byte[].class) {
            nbtCompound.setByteArray(key, (byte[]) data);
        } else if (tClass == Byte.class) {
            nbtCompound.setByte(key, (Byte) data);
        } else if (tClass == Double.class) {
            nbtCompound.setDouble(key, (Double) data);
        } else if (tClass == Float.class) {
            nbtCompound.setFloat(key, (Float) data);
        } else if (tClass == int[].class) {
            nbtCompound.setIntArray(key, (int[]) data);
        } else if (tClass == Integer.class) {
            nbtCompound.setInt(key, (Integer) data);
        } else if (tClass == long[].class) {
            nbtCompound.setLongArray(key, (long[]) data);
        } else if (tClass == Long.class) {
            nbtCompound.setLong(key, (Long) data);
        } else if (tClass == Short.class) {
            nbtCompound.setShort(key, (Short) data);
        } else if (tClass == String.class) {
            nbtCompound.setString(key, (String) data);
        } else {
            nbtCompound.set(key, new NBTGsonReader(new StringReader(GsonUtils.gson().toJson(data, tClass))).readWithGuess());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @Nullable T get(String key, Class<T> tClass) {
        if (!Primitives.isWrapperType(tClass)) {
            tClass = Primitives.wrap(tClass); // Make sure we will always "switch" over the wrapper types
        }

        if (!nbtCompound.containsKey(key)) {
            return null;
        }

        if (tClass == byte[].class) {
            return (T) Objects.requireNonNull(nbtCompound.getByteArray(key)).copyArray();
        }

        if (tClass == Byte.class) {
            return (T) nbtCompound.getAsByte(key);
        }

        if (tClass == Double.class) {
            return (T) nbtCompound.getAsDouble(key);
        }

        if (tClass == Float.class) {
            return (T) nbtCompound.getAsFloat(key);
        }

        if (tClass == int[].class) {
            return (T) Objects.requireNonNull(nbtCompound.getIntArray(key)).copyArray();
        }

        if (tClass == Integer.class) {
            return (T) nbtCompound.getAsInt(key);
        }

        if (tClass == long[].class) {
            return (T) Objects.requireNonNull(nbtCompound.getLongArray(key)).copyArray();
        }

        if (tClass == Long.class) {
            return (T) nbtCompound.getAsLong(key);
        }

        if (tClass == Short.class) {
            return (T) nbtCompound.getAsShort(key);
        }

        if (tClass == String.class) {
            return (T) nbtCompound.getString(key);
        }

        // TODO: Complex arrays and maps

        return GsonUtils.gson().fromJson(nbtCompound.toSNBT(), tClass); // from json
    }

    @Override
    public <T> Optional<T> getOptional(String key, Class<T> tClass) {
        return Optional.ofNullable(get(key, tClass));
    }

    @Override
    public <T> T getOrDefault(String key, Class<T> tClass, Supplier<T> def) {
        return getOptional(key, tClass).orElseGet(def);
    }

    @Override
    public boolean contains(String key) {
        return nbtCompound.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return nbtCompound.isEmpty();
    }
}
