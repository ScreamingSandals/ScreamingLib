package org.screamingsandals.lib.bukkit.item.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.nms.accessors.*;
import org.screamingsandals.lib.utils.GsonUtils;
import org.screamingsandals.lib.utils.Primitives;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class CraftBukkitItemData implements ItemData {
    @Getter
    private final Map<String, Object> keyNBTMap;

    @Override
    public Set<String> getKeys() {
        return keyNBTMap.keySet();
    }

    @Override
    public <T> void set(String key, T data, Class<T> tClass) {
        if (!Primitives.isWrapperType(tClass)) {
            tClass = Primitives.wrap(tClass); //Make sure we will always "switch" over the wrapper types
        }

        if (tClass == byte[].class) {
            keyNBTMap.put(key, Reflect.construct(ByteArrayTagAccessor.getConstructor0(), data));
        } else if (tClass == Byte.class) {
            keyNBTMap.put(key, ByteTagAccessor.getMethodValueOf1() != null ? Reflect.fastInvoke(ByteTagAccessor.getMethodValueOf1(), data) : Reflect.construct(ByteTagAccessor.getConstructor0(), data));
        } else if (tClass == Double.class) {
            keyNBTMap.put(key, DoubleTagAccessor.getMethodValueOf1() != null ? Reflect.fastInvoke(DoubleTagAccessor.getMethodValueOf1(), data) : Reflect.construct(DoubleTagAccessor.getConstructor0(), data));
        } else if (tClass == Float.class) {
            keyNBTMap.put(key, FloatTagAccessor.getMethodValueOf1() != null ? Reflect.fastInvoke(FloatTagAccessor.getMethodValueOf1(), data) : Reflect.construct(FloatTagAccessor.getConstructor0(), data));
        } else if (tClass == int[].class) {
            keyNBTMap.put(key, Reflect.construct(IntArrayTagAccessor.getConstructor0(), data));
        } else if (tClass == Integer.class) {
            keyNBTMap.put(key, IntTagAccessor.getMethodValueOf1() != null ? Reflect.fastInvoke(IntTagAccessor.getMethodValueOf1(), data) : Reflect.construct(IntTagAccessor.getConstructor0(), data));
        } else if (tClass == long[].class) {
            keyNBTMap.put(key, Reflect.construct(LongArrayTagAccessor.getConstructor0(), data));
        } else if (tClass == Long.class) {
            keyNBTMap.put(key, LongTagAccessor.getMethodValueOf1() != null ? Reflect.fastInvoke(LongTagAccessor.getMethodValueOf1(), data) : Reflect.construct(LongTagAccessor.getConstructor0(), data));
        } else if (tClass == Short.class) {
            keyNBTMap.put(key, ShortTagAccessor.getMethodValueOf1() != null ? Reflect.fastInvoke(ShortTagAccessor.getMethodValueOf1(), data) : Reflect.construct(ShortTagAccessor.getConstructor0(), data));
        } else if (tClass == String.class) {
            keyNBTMap.put(key, StringTagAccessor.getMethodValueOf1() != null ? Reflect.fastInvoke(StringTagAccessor.getMethodValueOf1(), data) : Reflect.construct(StringTagAccessor.getConstructor0(), data));
        } else {
            keyNBTMap.put(key, GsonUtils.gson().toJson(data)); // to json
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @Nullable T get(String key, Class<T> tClass) {
        if (!Primitives.isWrapperType(tClass)) {
            tClass = Primitives.wrap(tClass); //Make sure we will always "switch" over the wrapper types
        }

        var nbt = keyNBTMap.get(key);
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
    public <T> Optional<T> getOptional(String key, Class<T> tClass) {
        return Optional.ofNullable(get(key, tClass));
    }

    @Override
    public <T> T getOrDefault(String key, Class<T> tClass, Supplier<T> def) {
        return getOptional(key, tClass).orElseGet(def);
    }

    @Override
    public boolean contains(String key) {
        return keyNBTMap.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return keyNBTMap.isEmpty();
    }
}
