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

package org.screamingsandals.lib.nbt;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface CompoundTagModifier {
    @NotNull CompoundTag with(@NotNull Tag tag, @NotNull String @NotNull... tagKeys);

    <T extends Tag> @NotNull CompoundTag with(@NotNull TreeInspectorKey<T> key, @NotNull T tag);

    default @NotNull CompoundTag with(boolean value, @NotNull String @NotNull... tagKeys) {
        return with(value ? ByteTag.TRUE : ByteTag.FALSE, tagKeys);
    }

    default @NotNull CompoundTag with(@NotNull TreeInspectorKey<ByteTag> key, boolean value) {
        return with(key, value ? ByteTag.TRUE : ByteTag.FALSE);
    }

    default @NotNull CompoundTag with(byte @NotNull [] value, @NotNull String @NotNull... tagKeys) {
        return with(new ByteArrayTag(value), tagKeys);
    }

    default @NotNull CompoundTag with(@NotNull TreeInspectorKey<ByteArrayTag> key, byte @NotNull [] value) {
        return with(key, new ByteArrayTag(value));
    }

    default @NotNull CompoundTag with(byte value, @NotNull String @NotNull... tagKeys) {
        return with(new ByteTag(value), tagKeys);
    }

    default @NotNull CompoundTag with(@NotNull TreeInspectorKey<ByteTag> key, byte value) {
        return with(key, new ByteTag(value));
    }

    default @NotNull CompoundTag with(@NotNull Map<@NotNull String, @NotNull Tag> value, @NotNull String @NotNull... tagKeys) {
        return with(new CompoundTag(value), tagKeys);
    }

    default @NotNull CompoundTag with(@NotNull TreeInspectorKey<CompoundTag> key, @NotNull Map<@NotNull String, @NotNull Tag> value) {
        return with(key, new CompoundTag(value));
    }

    default @NotNull CompoundTag with(@NotNull TreeInspectorKey<CompoundTag> key, @NotNull CompoundTagLike value) {
        return with(key, value.asCompoundTag());
    }

    default @NotNull CompoundTag with(double value, @NotNull String @NotNull... tagKeys) {
        return with(new DoubleTag(value), tagKeys);
    }

    default @NotNull CompoundTag with(@NotNull TreeInspectorKey<DoubleTag> key, double value) {
        return with(key, new DoubleTag(value));
    }

    default @NotNull CompoundTag with(float value, @NotNull String @NotNull... tagKeys) {
        return with(new FloatTag(value), tagKeys);
    }

    default @NotNull CompoundTag with(@NotNull TreeInspectorKey<FloatTag> key, float value) {
        return with(key, new FloatTag(value));
    }

    default @NotNull CompoundTag with(int @NotNull [] value, @NotNull String @NotNull... tagKeys) {
        return with(new IntArrayTag(value), tagKeys);
    }

    default @NotNull CompoundTag with(@NotNull TreeInspectorKey<IntArrayTag> key, int @NotNull [] value) {
        return with(key, new IntArrayTag(value));
    }

    default @NotNull CompoundTag with(int value, @NotNull String @NotNull... tagKeys) {
        return with(new IntTag(value), tagKeys);
    }

    default @NotNull CompoundTag with(@NotNull TreeInspectorKey<IntTag> key, int value) {
        return with(key, new IntTag(value));
    }

    default @NotNull CompoundTag with(@NotNull List<@NotNull Tag> value, @NotNull String @NotNull... tagKeys) {
        return with(new ListTag(value), tagKeys);
    }

    default @NotNull CompoundTag with(@NotNull TreeInspectorKey<ListTag> key, @NotNull List<? extends @NotNull Tag> value) {
        //noinspection unchecked
        return with(key, new ListTag((List<Tag>) value));
    }

    default @NotNull CompoundTag with(long @NotNull [] value, @NotNull String @NotNull... tagKeys) {
        return with(new LongArrayTag(value), tagKeys);
    }

    default @NotNull CompoundTag with(@NotNull TreeInspectorKey<LongArrayTag> key, long @NotNull [] value) {
        return with(key, new LongArrayTag(value));
    }

    default @NotNull CompoundTag with(long value, @NotNull String @NotNull... tagKeys) {
        return with(new LongTag(value), tagKeys);
    }

    default @NotNull CompoundTag with(@NotNull TreeInspectorKey<LongTag> key, long value) {
        return with(key, new LongTag(value));
    }

    default @NotNull CompoundTag with(short value, @NotNull String @NotNull... tagKeys) {
        return with(new ShortTag(value), tagKeys);
    }

    default @NotNull CompoundTag with(@NotNull TreeInspectorKey<ShortTag> key, short value) {
        return with(key, new ShortTag(value));
    }

    default @NotNull CompoundTag withStringTag(@NotNull String value, @NotNull String @NotNull... tagKeys) {
        return with(new StringTag(value), tagKeys);
    }

    default @NotNull CompoundTag with(@NotNull TreeInspectorKey<StringTag> key, @NotNull String value) {
        return with(key, new StringTag(value));
    }

    @NotNull CompoundTag without(@NotNull String @NotNull... tagKeys);

    @NotNull <T extends Tag> CompoundTag without(@NotNull TreeInspectorKey<T> key);
}
