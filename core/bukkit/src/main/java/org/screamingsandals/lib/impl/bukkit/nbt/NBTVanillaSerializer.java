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

package org.screamingsandals.lib.impl.bukkit.nbt;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.nbt.*;
import org.screamingsandals.lib.impl.nms.accessors.*;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

// TODO: move to core-vanilla when work on sponge-impl starts
@UtilityClass
public class NBTVanillaSerializer {
    public static @NotNull Tag deserialize(@NotNull Object nmsTag) {
        Preconditions.checkArgument(TagAccessor.TYPE.get().isInstance(nmsTag), "nmsTag must be of type " + TagAccessor.TYPE.get().getName() + ", got " + nmsTag);

        if (ByteTagAccessor.TYPE.get().isInstance(nmsTag)) {
            return new ByteTag((byte) Reflect.fastInvoke(nmsTag, ByteTagAccessor.METHOD_GET_AS_BYTE.get()));
        } else if (ShortTagAccessor.TYPE.get().isInstance(nmsTag)) {
            return new ShortTag((short) Reflect.fastInvoke(nmsTag, ShortTagAccessor.METHOD_GET_AS_SHORT.get()));
        } else if (IntTagAccessor.TYPE.get().isInstance(nmsTag)) {
            return new IntTag((int) Reflect.fastInvoke(nmsTag, IntTagAccessor.METHOD_GET_AS_INT.get()));
        } else if (LongTagAccessor.TYPE.get().isInstance(nmsTag)) {
            return new LongTag((long) Reflect.fastInvoke(nmsTag, LongTagAccessor.METHOD_GET_AS_LONG.get()));
        } else if (StringTagAccessor.TYPE.get().isInstance(nmsTag)) {
            return new StringTag((String) Reflect.fastInvoke(nmsTag, TagAccessor.METHOD_GET_AS_STRING.get()));
        } else if (FloatTagAccessor.TYPE.get().isInstance(nmsTag)) {
            return new FloatTag((float) Reflect.fastInvoke(nmsTag, FloatTagAccessor.METHOD_GET_AS_FLOAT.get()));
        } else if (DoubleTagAccessor.TYPE.get().isInstance(nmsTag)) {
            return new DoubleTag((double) Reflect.fastInvoke(nmsTag, DoubleTagAccessor.METHOD_GET_AS_DOUBLE.get()));
        } else if (CompoundTagAccessor.TYPE.get().isInstance(nmsTag)) {
            var map = new HashMap<String, Tag>();
            for (var key : (Iterable<?>) Reflect.fastInvoke(nmsTag, CompoundTagAccessor.METHOD_GET_ALL_KEYS.get())) {
                map.put(key.toString(), deserialize(Reflect.fastInvoke(nmsTag, CompoundTagAccessor.METHOD_GET.get(), key)));
            }
            return new CompoundTag(map);
        } else if (ListTagAccessor.TYPE.get().isInstance(nmsTag)) {
            var size = (int) Reflect.fastInvoke(nmsTag, ListTagAccessor.METHOD_SIZE.get());
            var list = new ArrayList<Tag>(size);
            var isJavaUtilList = nmsTag instanceof List;
            for (var i = 0; i < size; i++) {
                if (isJavaUtilList) {
                    list.add(deserialize(((List<?>) nmsTag).get(i)));
                } else {
                    var entry = Reflect.fastInvoke(nmsTag, ListTagAccessor.METHOD_GET.get(), i);
                    list.add(deserialize(entry));
                }
            }
            return new ListTag(list);
        } else if (ByteArrayTagAccessor.TYPE.get().isInstance(nmsTag)) {
            return new ByteArrayTag((byte[]) Reflect.fastInvoke(nmsTag, ByteArrayTagAccessor.METHOD_GET_AS_BYTE_ARRAY.get()));
        } else if (IntArrayTagAccessor.TYPE.get().isInstance(nmsTag)) {
            return new IntArrayTag((int[]) Reflect.fastInvoke(nmsTag, IntArrayTagAccessor.METHOD_GET_AS_INT_ARRAY.get()));
        } else if (LongArrayTagAccessor.TYPE.get() != null && LongArrayTagAccessor.TYPE.get().isInstance(nmsTag)) {
            if (LongArrayTagAccessor.METHOD_GET_AS_LONG_ARRAY.get() != null) {
                return new LongArrayTag((long[]) Reflect.fastInvoke(nmsTag, LongArrayTagAccessor.METHOD_GET_AS_LONG_ARRAY.get()));
            } else {
                return new LongArrayTag((long[]) Objects.requireNonNull(Reflect.getField(nmsTag, LongArrayTagAccessor.FIELD_FIELD_193587_B.get())));
            }
        } else {
            throw new IllegalArgumentException("Unknown tag " + nmsTag);
        }
    }

    public static @NotNull Object serialize(@NotNull Tag tag) {
        if (tag instanceof ByteTag) {
            return Reflect.construct(ByteTagAccessor.CONSTRUCTOR_0.get(), ((NumericTag) tag).byteValue());
        } else if (tag instanceof ShortTag) {
            return Reflect.construct(ShortTagAccessor.CONSTRUCTOR_0.get(), ((NumericTag) tag).shortValue());
        } else if (tag instanceof IntTag) {
            return Reflect.construct(IntTagAccessor.CONSTRUCTOR_0.get(), ((NumericTag) tag).intValue());
        } else if (tag instanceof LongTag) {
            return Reflect.construct(LongTagAccessor.CONSTRUCTOR_0.get(), ((NumericTag) tag).longValue());
        } else if (tag instanceof StringTag) {
            return Reflect.construct(StringTagAccessor.CONSTRUCTOR_0.get(), ((StringTag) tag).value());
        } else if (tag instanceof FloatTag) {
            return Reflect.construct(FloatTagAccessor.CONSTRUCTOR_0.get(), ((NumericTag) tag).floatValue());
        } else if (tag instanceof DoubleTag) {
            return Reflect.construct(DoubleTagAccessor.CONSTRUCTOR_0.get(), ((NumericTag) tag).doubleValue());
        } else if (tag instanceof CompoundTag) {
            var compound = Reflect.construct(CompoundTagAccessor.CONSTRUCTOR_0.get());
            for (var entry : ((CompoundTag) tag).value().entrySet()) {
                Reflect.fastInvoke(compound, CompoundTagAccessor.METHOD_PUT.get(), entry.getKey(), serialize(entry.getValue()));
            }
            return compound;
        } else if (tag instanceof ListTag) {
            var list = Reflect.construct(ListTagAccessor.CONSTRUCTOR_0.get());
            var isJavaUtilList = list instanceof List;
            for (var entry : (ListTag) tag) {
                if (isJavaUtilList) {
                    //noinspection unchecked,rawtypes
                    ((List) list).add(serialize(entry));
                } else {
                    Reflect.fastInvoke(list, ListTagAccessor.METHOD_ADD_1.get(), serialize(entry));
                }
            }
            return list;
        } else if (tag instanceof ByteArrayTag) {
            return Reflect.construct(ByteArrayTagAccessor.CONSTRUCTOR_0.get(), new Object[] {((ByteArrayTag) tag).value()});
        } else if (tag instanceof IntArrayTag) {
            return Reflect.construct(IntArrayTagAccessor.CONSTRUCTOR_0.get(), new Object[] {((IntArrayTag) tag).value()});
        } else if (tag instanceof LongArrayTag) {
            if (LongArrayTagAccessor.TYPE.get() != null) {
                return Reflect.construct(LongArrayTagAccessor.CONSTRUCTOR_0.get(), new Object[]{((LongArrayTag) tag).value()});
            } else {
                var list = Reflect.construct(ListTagAccessor.CONSTRUCTOR_0.get());
                var isJavaUtilList = list instanceof List;
                for (var entry : (LongArrayTag) tag) {
                    if (isJavaUtilList) {
                        //noinspection unchecked,rawtypes
                        ((List) list).add(Reflect.construct(LongTagAccessor.CONSTRUCTOR_0.get(), entry));
                    } else {
                        Reflect.fastInvoke(list, ListTagAccessor.METHOD_ADD_1.get(), Reflect.construct(LongTagAccessor.CONSTRUCTOR_0.get(), entry));
                    }
                }
                return list;
            }
        } else {
            throw new IllegalArgumentException("Unknown tag " + tag);
        }
    }
}
