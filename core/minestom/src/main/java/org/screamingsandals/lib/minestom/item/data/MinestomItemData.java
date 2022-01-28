package org.screamingsandals.lib.minestom.item.data;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.json.NBTGsonReader;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.utils.GsonUtils;
import org.screamingsandals.lib.utils.Primitives;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.io.Reader;
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
            // TODO
        }
    }

    @Override
    public <T> @Nullable T get(String key, Class<T> tClass) {
        return null;
    }

    @Override
    public <T> Optional<T> getOptional(String key, Class<T> tClass) {
        return Optional.empty();
    }

    @Override
    public <T> T getOrDefault(String key, Class<T> tClass, Supplier<T> def) {
        return null;
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
