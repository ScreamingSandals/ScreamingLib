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

package org.screamingsandals.lib.nbt;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

@Data
@Accessors(fluent = true)
public final class CompoundTag implements Tag, CompoundTagTreeInspector, CompoundTagModifier {
    public static final @NotNull CompoundTag EMPTY = new CompoundTag(Map.of());

    private final @NotNull Map<@NotNull String, @NotNull Tag> value;

    @Contract(value = "-> new", pure = true)
    public @NotNull Map<@NotNull String, @NotNull Tag> value() {
        return Map.copyOf(value); // keep this class immutable
    }

    public boolean hasTag(@NotNull String name) {
        return value.containsKey(name);
    }

    public @Nullable Tag tag(@NotNull String name) {
        return value.get(name);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public @NotNull CompoundTag with(@NotNull String name, @NotNull Tag tag) {
        var clone = new HashMap<>(value);
        clone.put(name, tag);
        return new CompoundTag(clone);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public @NotNull CompoundTag with(@NotNull String name, boolean value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, value ? ByteTag.TRUE : ByteTag.FALSE);
        return new CompoundTag(clone);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public @NotNull CompoundTag with(@NotNull String name, byte @NotNull [] value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new ByteArrayTag(value));
        return new CompoundTag(clone);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public @NotNull CompoundTag with(@NotNull String name, byte value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new ByteTag(value));
        return new CompoundTag(clone);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public @NotNull CompoundTag with(@NotNull String name, @NotNull Map<@NotNull String, @NotNull Tag> value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new CompoundTag(value));
        return new CompoundTag(clone);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public @NotNull CompoundTag with(@NotNull String name, double value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new DoubleTag(value));
        return new CompoundTag(clone);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public @NotNull CompoundTag with(@NotNull String name, float value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new FloatTag(value));
        return new CompoundTag(clone);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public @NotNull CompoundTag with(@NotNull String name, int @NotNull [] value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new IntArrayTag(value));
        return new CompoundTag(clone);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public @NotNull CompoundTag with(@NotNull String name, int value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new IntTag(value));
        return new CompoundTag(clone);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public @NotNull CompoundTag with(@NotNull String name, @NotNull List<@NotNull Tag> value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new ListTag(value));
        return new CompoundTag(clone);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public @NotNull CompoundTag with(@NotNull String name, long @NotNull [] value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new LongArrayTag(value));
        return new CompoundTag(clone);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public @NotNull CompoundTag with(@NotNull String name, long value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new LongTag(value));
        return new CompoundTag(clone);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public @NotNull CompoundTag with(@NotNull String name, short value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new ShortTag(value));
        return new CompoundTag(clone);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public @NotNull CompoundTag with(@NotNull String name, @NotNull String value) {
        var clone = new HashMap<>(this.value);
        clone.put(name, new StringTag(value));
        return new CompoundTag(clone);
    }

    @Contract(value = "_ -> new", pure = true)
    public @NotNull CompoundTag without(@NotNull String name) {
        var clone = new HashMap<>(value);
        clone.remove(name);
        return new CompoundTag(clone);
    }

    public void forEach(@NotNull BiConsumer<@NotNull String, @NotNull Tag> consumer) {
        for (var entry : value.entrySet()) {
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }

    public @NotNull Stream<Map.@NotNull Entry<@NotNull String, @NotNull Tag>> stream() {
        return value.entrySet().stream();
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public int size() {
        return value.size();
    }

    @Override
    public @Nullable Tag findTag(@NotNull String @NotNull ... tagKeys) {
        if (tagKeys.length == 0) {
            return null;
        }
        if (tagKeys.length == 1) {
            return tag(tagKeys[0]);
        }
        @Nullable Tag currentTag = this;
        for (var key : tagKeys) {
            if (currentTag instanceof CompoundTag) {
                currentTag = ((CompoundTag) currentTag).tag(key);
            } else if (currentTag instanceof CollectionTag) {
                try {
                    if (key.startsWith("[")) {
                        key = key.substring(1);
                    }
                    if (key.endsWith("]")) {
                        key = key.substring(0, key.length() - 1);
                    }
                    var number = Integer.parseInt(key);
                    if (number < ((CollectionTag) currentTag).size()) {
                        currentTag = ((CollectionTag) currentTag).getAsTag(number);
                    } else {
                        currentTag = null;
                    }
                } catch (Throwable ignored) {
                }
            } else {
                return null;
            }
        }
        return currentTag;
    }

    // TODO: test code below
    @Override
    public @NotNull CompoundTag with(@NotNull Tag tag, @NotNull String @NotNull ... tagKeys) {
        if (tagKeys.length == 0) {
            return this;
        }
        if (tagKeys.length == 1) {
            return with(tagKeys[0], tag);
        }
        return (CompoundTag) withInternal(this, tagKeys, 0, tag);
    }

    @Override
    public @NotNull <T extends Tag> CompoundTag with(@NotNull TreeInspectorKey<T> treeKey, @NotNull T tag) {
        var tagKeys = treeKey.getTagKeys();
        if (tagKeys.length == 0) {
            return this;
        }
        if (tagKeys.length == 1) {
            return with(tagKeys[0], tag);
        }
        return (CompoundTag) withInternal(this, tagKeys, 0, tag);
    }

    private @NotNull Tag withInternal(@NotNull Tag tag, @NotNull String @NotNull[] tagKeys, int index, @NotNull Tag value) {
        if (index >= tagKeys.length) {
            return tag;
        }

        var relativeSize = tagKeys.length - index;
        if (tag instanceof CompoundTag) {
            if (relativeSize == 1) {
                return ((CompoundTag) tag).with(tagKeys[index], value);
            } else {
                var innerTag = ((CompoundTag) tag).tag(tagKeys[index]);
                if (innerTag != null) {
                    if (innerTag instanceof CompoundTag || innerTag instanceof ListTag) {
                        return ((CompoundTag) tag).with(tagKeys[index], withInternal(innerTag, tagKeys, index + 1, value));
                    }
                    return tag;
                } else {
                    // look at the next key
                    var useList = false;
                    var key = tagKeys[index + 1];
                    if ("[]".equals(key)) {
                        useList = true;
                    } else {
                        try {
                            if (key.startsWith("[")) {
                                key = key.substring(1);
                            }
                            if (key.endsWith("]")) {
                                key = key.substring(0, key.length() - 1);
                            }
                            Integer.parseInt(key);
                            useList = true;
                        } catch (Throwable ignored) {
                        }
                    }

                    if (useList) {
                        return ((CompoundTag) tag).with(tagKeys[index], withInternal(ListTag.EMPTY, tagKeys, index + 1, value));
                    } else {
                        return ((CompoundTag) tag).with(tagKeys[index], withInternal(CompoundTag.EMPTY, tagKeys, index + 1, value));
                    }
                }
            }
        } else if (tag instanceof ListTag) {
            if (relativeSize == 1) {
                var key = tagKeys[index];
                if ("[]".equals(key)) {
                    return ((ListTag) tag).with(value);
                } else {
                    try {
                        if (key.startsWith("[")) {
                            key = key.substring(1);
                        }
                        if (key.endsWith("]")) {
                            key = key.substring(0, key.length() - 1);
                        }
                        var number = Integer.parseInt(key);
                        if (number < ((ListTag) tag).size()) {
                            return ((ListTag) tag).withAt(number, value);
                        }
                    } catch (Throwable ignored) {
                    }
                    return tag;
                }
            } else {
                Tag innerTag = null;
                int numericIndex = -1;
                var thisKey = tagKeys[index];
                if (!"[]".equals(thisKey)) {
                    try {
                        if (thisKey.startsWith("[")) {
                            thisKey = thisKey.substring(1);
                        }
                        if (thisKey.endsWith("]")) {
                            thisKey = thisKey.substring(0, thisKey.length() - 1);
                        }
                        numericIndex = Integer.parseInt(thisKey);
                        if (numericIndex < ((ListTag) tag).size()) {
                            innerTag = ((ListTag) tag).getAsTag(index);
                        } else {
                            return tag;
                        }
                    } catch (Throwable ignored) {
                        return tag;
                    }
                }

                if (innerTag != null) {
                    if (innerTag instanceof CompoundTag || innerTag instanceof ListTag) {
                        if (numericIndex == -1) {
                            return ((ListTag) tag).with(withInternal(innerTag, tagKeys, index + 1, value));
                        } else {
                            return ((ListTag) tag).withAt(numericIndex, withInternal(innerTag, tagKeys, index + 1, value));
                        }
                    }
                    return tag;
                } else {
                    // look at the next key
                    var useList = false;
                    var key = tagKeys[index + 1];
                    if ("[]".equals(key)) {
                        useList = true;
                    } else {
                        try {
                            if (key.startsWith("[")) {
                                key = key.substring(1);
                            }
                            if (key.endsWith("]")) {
                                key = key.substring(0, key.length() - 1);
                            }
                            Integer.parseInt(key);
                            useList = true;
                        } catch (Throwable ignored) {
                        }
                    }

                    if (useList) {
                        return ((ListTag) tag).with(withInternal(ListTag.EMPTY, tagKeys, index + 1, value));
                    } else {
                        return ((ListTag) tag).with(withInternal(CompoundTag.EMPTY, tagKeys, index + 1, value));
                    }
                }
            }
        } else {
            return tag; // wtf?
        }
    }

    @Override
    public @NotNull CompoundTag without(@NotNull String @NotNull ... tagKeys) {
        if (tagKeys.length == 0) {
            return this;
        }
        if (tagKeys.length == 1) {
            return without(tagKeys[0]);
        }
        @NotNull List<Map.@NotNull Entry<@NotNull String, @NotNull Tag>> parents = new ArrayList<>();
        @Nullable Tag currentTag = this;
        for (var key : tagKeys) {
            if (currentTag instanceof CompoundTag) {
                parents.add(Map.entry(key, currentTag));
                currentTag = ((CompoundTag) currentTag).tag(key);
            } else if (currentTag instanceof ListTag) {
                try {
                    if (key.startsWith("[")) {
                        key = key.substring(1);
                    }
                    if (key.endsWith("]")) {
                        key = key.substring(0, key.length() - 1);
                    }
                    var number = Integer.parseInt(key);
                    if (number < ((ListTag) currentTag).size()) {
                        parents.add(Map.entry(key, currentTag));
                        currentTag = ((ListTag) currentTag).getAsTag(number);
                    } else {
                        currentTag = null;
                    }
                } catch (Throwable ignored) {
                }
            } else {
                return this; // nothing to remove
            }
        }
        if (currentTag != null) {
            Collections.reverse(parents);
            Tag latestChild = currentTag;
            for (var parent : parents) {
                if (parent.getValue() instanceof CompoundTag) {
                    latestChild = ((CompoundTag) parent.getValue()).with(parent.getKey(), latestChild);
                } else if (parent.getValue() instanceof ListTag) {
                    try {
                        var number = Integer.parseInt(parent.getKey());
                        latestChild = ((ListTag) parent.getValue()).without(number);
                    } catch (Throwable ignored) {
                    }
                }
            }
            return (CompoundTag) latestChild;
        }
        return this;
    }

    @Override
    public @NotNull <T extends Tag> CompoundTag without(@NotNull TreeInspectorKey<T> treeKey) {
        var tagKeys = treeKey.getTagKeys();
        if (tagKeys.length == 0) {
            return this;
        }
        if (tagKeys.length == 1) {
            if (treeKey.getTagClass().isInstance(value.get(tagKeys[0]))) {
                return without(tagKeys[0]);
            } else {
                return this;
            }
        }
        @NotNull List<Map.@NotNull Entry<@NotNull String, @NotNull Tag>> parents = new ArrayList<>();
        @Nullable Tag currentTag = this;
        for (var key : tagKeys) {
            if (currentTag instanceof CompoundTag) {
                parents.add(Map.entry(key, currentTag));
                currentTag = ((CompoundTag) currentTag).tag(key);
            } else if (currentTag instanceof ListTag) {
                try {
                    if (key.startsWith("[")) {
                        key = key.substring(1);
                    }
                    if (key.endsWith("]")) {
                        key = key.substring(0, key.length() - 1);
                    }
                    var number = Integer.parseInt(key);
                    if (number < ((ListTag) currentTag).size()) {
                        parents.add(Map.entry(key, currentTag));
                        currentTag = ((ListTag) currentTag).getAsTag(number);
                    } else {
                        currentTag = null;
                    }
                } catch (Throwable ignored) {
                }
            } else {
                return this; // nothing to remove
            }
        }
        if (currentTag != null && treeKey.getTagClass().isInstance(currentTag)) {
            Collections.reverse(parents);
            Tag latestChild = currentTag;
            for (var parent : parents) {
                if (parent.getValue() instanceof CompoundTag) {
                    latestChild = ((CompoundTag) parent.getValue()).with(parent.getKey(), latestChild);
                } else if (parent.getValue() instanceof ListTag) {
                    try {
                        var number = Integer.parseInt(parent.getKey());
                        latestChild = ((ListTag) parent.getValue()).without(number);
                    } catch (Throwable ignored) {
                    }
                }
            }
            return (CompoundTag) latestChild;
        }
        return this;
    }
}
