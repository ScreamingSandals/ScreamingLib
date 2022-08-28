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

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

@Data
@Accessors(fluent = true)
public final class CompoundTag implements Tag {
    @NotNull
    private final Map<@NotNull String, @NotNull Tag> value;

    @NotNull
    @Contract(value = "-> new", pure = true)
    public Map<@NotNull String, @NotNull Tag> value() {
        return Map.copyOf(value); // keep this class immutable
    }

    public boolean hasTag(@NotNull String name) {
        return value.containsKey(name);
    }

    @Nullable
    public Tag tag(@NotNull String name) {
        return value.get(name);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public CompoundTag with(@NotNull String name, @NotNull Tag tag) {
        var clone = new HashMap<>(value);
        clone.put(name, tag);
        return new CompoundTag(clone);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public CompoundTag with(@NotNull String name, boolean value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new BooleanTag(value));
        return new CompoundTag(clone);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public CompoundTag with(@NotNull String name, byte @NotNull [] value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new ByteArrayTag(value));
        return new CompoundTag(clone);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public CompoundTag with(@NotNull String name, byte value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new ByteTag(value));
        return new CompoundTag(clone);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public CompoundTag with(@NotNull String name, @NotNull Map<@NotNull String, @NotNull Tag> value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new CompoundTag(value));
        return new CompoundTag(clone);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public CompoundTag with(@NotNull String name, double value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new DoubleTag(value));
        return new CompoundTag(clone);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public CompoundTag with(@NotNull String name, float value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new FloatTag(value));
        return new CompoundTag(clone);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public CompoundTag with(@NotNull String name, int @NotNull [] value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new IntArrayTag(value));
        return new CompoundTag(clone);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public CompoundTag with(@NotNull String name, int value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new IntTag(value));
        return new CompoundTag(clone);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public CompoundTag with(@NotNull String name, @NotNull List<@NotNull Tag> value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new ListTag(value));
        return new CompoundTag(clone);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public CompoundTag with(@NotNull String name, long @NotNull [] value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new LongArrayTag(value));
        return new CompoundTag(clone);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public CompoundTag with(@NotNull String name, long value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new LongTag(value));
        return new CompoundTag(clone);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public CompoundTag with(@NotNull String name, short value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new ShortTag(value));
        return new CompoundTag(clone);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public CompoundTag with(@NotNull String name, @NotNull String value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new StringTag(value));
        return new CompoundTag(clone);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public CompoundTag without(@NotNull String name) {
        var clone = new HashMap<>(value);
        clone.remove(name);
        return new CompoundTag(clone);
    }

    public void forEach(@NotNull BiConsumer<@NotNull String, @NotNull Tag> consumer) {
        for (var entry : value.entrySet()) {
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }

    @NotNull
    public Stream<Map.@NotNull Entry<@NotNull String, @NotNull Tag>> stream() {
        return value.entrySet().stream();
    }

}
