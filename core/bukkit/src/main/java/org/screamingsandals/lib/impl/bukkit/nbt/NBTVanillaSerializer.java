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
        Preconditions.checkArgument(TagAccessor.getType().isInstance(nmsTag), "nmsTag must be of type " + TagAccessor.getType().getName() + ", got " + nmsTag);

        if (ByteTagAccessor.getType().isInstance(nmsTag)) {
            return new ByteTag((byte) Reflect.fastInvoke(nmsTag, ByteTagAccessor.getMethodGetAsByte1()));
        } else if (ShortTagAccessor.getType().isInstance(nmsTag)) {
            return new ShortTag((short) Reflect.fastInvoke(nmsTag, ShortTagAccessor.getMethodGetAsShort1()));
        } else if (IntTagAccessor.getType().isInstance(nmsTag)) {
            return new IntTag((int) Reflect.fastInvoke(nmsTag, IntTagAccessor.getMethodGetAsInt1()));
        } else if (LongTagAccessor.getType().isInstance(nmsTag)) {
            return new LongTag((long) Reflect.fastInvoke(nmsTag, LongTagAccessor.getMethodGetAsLong1()));
        } else if (StringTagAccessor.getType().isInstance(nmsTag)) {
            return new StringTag((String) Reflect.fastInvoke(nmsTag, TagAccessor.getMethodGetAsString1()));
        } else if (FloatTagAccessor.getType().isInstance(nmsTag)) {
            return new FloatTag((float) Reflect.fastInvoke(nmsTag, FloatTagAccessor.getMethodGetAsFloat1()));
        } else if (DoubleTagAccessor.getType().isInstance(nmsTag)) {
            return new DoubleTag((double) Reflect.fastInvoke(nmsTag, DoubleTagAccessor.getMethodGetAsDouble1()));
        } else if (CompoundTagAccessor.getType().isInstance(nmsTag)) {
            var map = new HashMap<String, Tag>();
            for (var key : (Iterable<?>) Reflect.fastInvoke(nmsTag, CompoundTagAccessor.getMethodGetAllKeys1())) {
                map.put(key.toString(), deserialize(Reflect.fastInvoke(nmsTag, CompoundTagAccessor.getMethodGet1(), key)));
            }
            return new CompoundTag(map);
        } else if (ListTagAccessor.getType().isInstance(nmsTag)) {
            var size = (int) Reflect.fastInvoke(nmsTag, ListTagAccessor.getMethodSize1());
            var list = new ArrayList<Tag>(size);
            var isJavaUtilList = nmsTag instanceof List;
            for (var i = 0; i < size; i++) {
                if (isJavaUtilList) {
                    list.add(deserialize(((List<?>) nmsTag).get(i)));
                } else {
                    var entry = Reflect.fastInvoke(nmsTag, ListTagAccessor.getMethodGet1(), i);
                    list.add(deserialize(entry));
                }
            }
            return new ListTag(list);
        } else if (ByteArrayTagAccessor.getType().isInstance(nmsTag)) {
            return new ByteArrayTag((byte[]) Reflect.fastInvoke(nmsTag, ByteArrayTagAccessor.getMethodGetAsByteArray1()));
        } else if (IntArrayTagAccessor.getType().isInstance(nmsTag)) {
            return new IntArrayTag((int[]) Reflect.fastInvoke(nmsTag, IntArrayTagAccessor.getMethodGetAsIntArray1()));
        } else if (LongArrayTagAccessor.getType() != null && LongArrayTagAccessor.getType().isInstance(nmsTag)) {
            if (LongArrayTagAccessor.getMethodGetAsLongArray1() != null) {
                return new LongArrayTag((long[]) Reflect.fastInvoke(nmsTag, LongArrayTagAccessor.getMethodGetAsLongArray1()));
            } else {
                return new LongArrayTag((long[]) Objects.requireNonNull(Reflect.getField(nmsTag, LongArrayTagAccessor.getFieldField_193587_b())));
            }
        } else {
            throw new IllegalArgumentException("Unknown tag " + nmsTag);
        }
    }

    public static @NotNull Object serialize(@NotNull Tag tag) {
        if (tag instanceof ByteTag) {
            return Reflect.construct(ByteTagAccessor.getConstructor0(), ((NumericTag) tag).byteValue());
        } else if (tag instanceof ShortTag) {
            return Reflect.construct(ShortTagAccessor.getConstructor0(), ((NumericTag) tag).shortValue());
        } else if (tag instanceof IntTag) {
            return Reflect.construct(IntTagAccessor.getConstructor0(), ((NumericTag) tag).intValue());
        } else if (tag instanceof LongTag) {
            return Reflect.construct(LongTagAccessor.getConstructor0(), ((NumericTag) tag).longValue());
        } else if (tag instanceof StringTag) {
            return Reflect.construct(StringTagAccessor.getConstructor0(), ((StringTag) tag).value());
        } else if (tag instanceof FloatTag) {
            return Reflect.construct(FloatTagAccessor.getConstructor0(), ((NumericTag) tag).floatValue());
        } else if (tag instanceof DoubleTag) {
            return Reflect.construct(DoubleTagAccessor.getConstructor0(), ((NumericTag) tag).doubleValue());
        } else if (tag instanceof CompoundTag) {
            var compound = Reflect.construct(CompoundTagAccessor.getConstructor0());
            for (var entry : ((CompoundTag) tag).value().entrySet()) {
                Reflect.fastInvoke(compound, CompoundTagAccessor.getMethodPut1(), entry.getKey(), serialize(entry.getValue()));
            }
            return compound;
        } else if (tag instanceof ListTag) {
            var list = Reflect.construct(ListTagAccessor.getConstructor0());
            var isJavaUtilList = list instanceof List;
            for (var entry : (ListTag) tag) {
                if (isJavaUtilList) {
                    //noinspection unchecked,rawtypes
                    ((List) list).add(serialize(entry));
                } else {
                    Reflect.fastInvoke(list, ListTagAccessor.getMethodAdd1(), serialize(entry));
                }
            }
            return list;
        } else if (tag instanceof ByteArrayTag) {
            return Reflect.construct(ByteArrayTagAccessor.getConstructor0(), new Object[] {((ByteArrayTag) tag).value()});
        } else if (tag instanceof IntArrayTag) {
            return Reflect.construct(IntArrayTagAccessor.getConstructor0(), new Object[] {((IntArrayTag) tag).value()});
        } else if (tag instanceof LongArrayTag) {
            if (LongArrayTagAccessor.getType() != null) {
                return Reflect.construct(LongArrayTagAccessor.getConstructor0(), new Object[]{((LongArrayTag) tag).value()});
            } else {
                var list = Reflect.construct(ListTagAccessor.getConstructor0());
                var isJavaUtilList = list instanceof List;
                for (var entry : (LongArrayTag) tag) {
                    if (isJavaUtilList) {
                        //noinspection unchecked,rawtypes
                        ((List) list).add(Reflect.construct(LongTagAccessor.getConstructor0(), entry));
                    } else {
                        Reflect.fastInvoke(list, ListTagAccessor.getMethodAdd1(), Reflect.construct(LongTagAccessor.getConstructor0(), entry));
                    }
                }
                return list;
            }
        } else {
            throw new IllegalArgumentException("Unknown tag " + tag);
        }
    }
}
