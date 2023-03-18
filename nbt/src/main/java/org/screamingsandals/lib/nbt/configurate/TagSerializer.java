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

package org.screamingsandals.lib.nbt.configurate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.nbt.*;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class TagSerializer implements TypeSerializer<Tag> {
    public static final @NotNull TagSerializer INSTANCE = new TagSerializer();

    private static final @NotNull String SPECIAL_TYPE_KEY = "_==";
    private static final @NotNull String SPECIAL_TYPE_VALUES_KEY = "values";
    private static final @NotNull String BYTE_ARRAY_VALUE = "NBTByteArray";
    private static final @NotNull String INT_ARRAY_VALUE = "NBTIntArray";
    private static final @NotNull String LONG_ARRAY_VALUE = "NBTLongArray";

    @Override
    public @NotNull Tag deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            if (node.isMap()) {
                if (!node.node(SPECIAL_TYPE_KEY).virtual()) {
                    var specialType = node.node(SPECIAL_TYPE_KEY).getString();
                    if (BYTE_ARRAY_VALUE.equalsIgnoreCase(specialType)) {
                        // Byte array
                        var values = node.node(SPECIAL_TYPE_VALUES_KEY);
                        var bytes = new ArrayList<Byte>();
                        for (var child : values.childrenList()) {
                            try {
                                bytes.add(child.get(Integer.class, 0).byteValue());
                            } catch (SerializationException exception) {
                                var str = child.getString("");
                                if ("true".equalsIgnoreCase(str)) { // in NBT, booleans are numbers
                                    bytes.add((byte) 1);
                                } else if ("false".equalsIgnoreCase(str)) {
                                    bytes.add((byte) 0);
                                } else { // probably suffixed with b or B
                                    bytes.add(Byte.parseByte(str.replaceAll("[bB]", "")));
                                }
                            }
                        }
                        var result = new byte[bytes.size()];
                        for (int j = 0; j < bytes.size(); j++) {
                            result[j] = bytes.get(j);
                        }
                        return new ByteArrayTag(result);
                    } else if (INT_ARRAY_VALUE.equalsIgnoreCase(specialType)) {
                        // Int array
                        var values = node.node(SPECIAL_TYPE_VALUES_KEY);
                        var stream = IntStream.builder();
                        for (var child : values.childrenList()) {
                            try {
                                stream.add(child.get(Integer.class, 0));
                            } catch (SerializationException exception) {
                                var str = child.getString("");
                                if ("true".equalsIgnoreCase(str)) { // in NBT, booleans are numbers
                                    stream.add(1);
                                } else if ("false".equalsIgnoreCase(str)) {
                                    stream.add(0);
                                } else { // probably ending with i or I, s or S, b or B
                                    stream.add(Integer.parseInt(str.replaceAll("[iIsSbB]", "")));
                                }
                            }
                        }
                        return new IntArrayTag(stream.build().toArray());
                    } else if (LONG_ARRAY_VALUE.equalsIgnoreCase(specialType)) {
                        // Long array
                        var values = node.node(SPECIAL_TYPE_VALUES_KEY);
                        var stream = LongStream.builder();
                        for (var child : values.childrenList()) {
                            try {
                                stream.add(child.get(Long.class, 0L));
                            } catch (SerializationException exception) {
                                var str = child.getString("");
                                if ("true".equalsIgnoreCase(str)) { // in NBT, booleans are numbers
                                    stream.add(1);
                                } else if ("false".equalsIgnoreCase(str)) {
                                    stream.add(0);
                                } else { // probably ending with l or L, i or I, s or S, b or B
                                    stream.add(Long.parseLong(child.getString("").replaceAll("[lLiIsSbB]", "")));
                                }
                            }
                        }
                        return new LongArrayTag(stream.build().toArray());
                    }
                }

                // Compound
                var map = new HashMap<String, Tag>();
                for (var entry : node.childrenMap().entrySet()) {
                    var tag = entry.getValue().get(Tag.class);
                    if (tag == null) {
                        throw new IllegalArgumentException("Entry of NBT compound tag can't be null");
                    }
                    map.put(entry.getKey().toString(), tag);
                }
                return new CompoundTag(map);
            } else if (node.isList()) {
                var list = new ArrayList<Tag>();
                for (var child : node.childrenList()) {
                    var tag = child.get(Tag.class);
                    if (tag == null) {
                        throw new IllegalArgumentException("Entry of NBT list can't be null");
                    }
                    list.add(tag);
                }
                return new ListTag(list);
            } else {
                Double number = null;
                try {
                    number = node.get(Double.class);
                } catch (SerializationException ignored) {
                }
                if (number != null) {
                    if (number.isInfinite() || number.isNaN()) {
                        throw new SerializationException("NBT numbers can't be NaN or Infinite");
                    }

                    if (number == number.intValue()) {
                        return new IntTag(number.intValue());
                    }

                    if (number == number.longValue()) {
                        return new LongTag(number.longValue());
                    }

                    var string = node.getString("");
                    if (string.endsWith("f") || string.endsWith("F")) {
                        return new FloatTag(number.floatValue());
                    }

                    return new DoubleTag(number);
                }

                var string = node.getString("");
                if ("true".equalsIgnoreCase(string)) {
                    return ByteTag.TRUE;
                } else if ("false".equalsIgnoreCase(string)) {
                    return ByteTag.FALSE;
                } else if (string.endsWith("b") || string.endsWith("B")) {
                    try {
                        return new ByteTag(Byte.parseByte(string.substring(0, string.length() - 1)));
                    } catch (NumberFormatException ignored) {
                    }
                } else if (string.endsWith("s") || string.endsWith("S")) {
                    try {
                        return new ShortTag(Short.parseShort(string.substring(0, string.length() - 1)));
                    } catch (NumberFormatException ignored) {
                    }
                } else if (string.endsWith("i") || string.endsWith("I")) {
                    try {
                        return new IntTag(Integer.parseInt(string.substring(0, string.length() - 1)));
                    } catch (NumberFormatException ignored) {
                    }
                } else if (string.endsWith("l") || string.endsWith("L")) {
                    try {
                        return new LongTag(Long.parseLong(string.substring(0, string.length() - 1)));
                    } catch (NumberFormatException ignored) {
                    }
                } else if (string.endsWith("f") || string.endsWith("F")) {
                    try {
                        return new FloatTag(Float.parseFloat(string.substring(0, string.length() - 1)));
                    } catch (NumberFormatException ignored) {
                    }
                } else if (string.endsWith("d") || string.endsWith("D")) {
                    try {
                        return new DoubleTag(Double.parseDouble(string.substring(0, string.length() - 1)));
                    } catch (NumberFormatException ignored) {
                    }
                }

                return new StringTag(string);
            }
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable Tag obj, @NotNull ConfigurationNode node) throws SerializationException {
        node.set(null); // clear node
        if (obj instanceof ByteArrayTag) {
            node.node(SPECIAL_TYPE_KEY).set(BYTE_ARRAY_VALUE);
            var baseNode = node.node(SPECIAL_TYPE_VALUES_KEY);
            for (var byteVal : (ByteArrayTag) obj) {
                baseNode.appendListNode().set(byteVal);
            }
        } else if (obj instanceof ByteTag) {
            node.set(((ByteTag) obj).value() + "b");
        } else if (obj instanceof CompoundTag) {
            node.set(Map.of());
            for (var childEntry : ((CompoundTag) obj).value().entrySet()) {
                node.node(childEntry.getKey()).set(Tag.class, childEntry.getValue());
            }
        } else if (obj instanceof DoubleTag) {
            node.set(((DoubleTag) obj).value() + "d");
        } else if (obj instanceof FloatTag) {
            node.set(((FloatTag) obj).value() + "f");
        } else if (obj instanceof IntArrayTag) {
            node.node(SPECIAL_TYPE_KEY).set(INT_ARRAY_VALUE);
            var baseNode = node.node(SPECIAL_TYPE_VALUES_KEY);
            for (var intVal : (IntArrayTag) obj) {
                baseNode.appendListNode().set(intVal);
            }
        } else if (obj instanceof IntTag) {
            node.set(((IntTag) obj).value());
        } else if (obj instanceof ListTag) {
            node.setList(Tag.class, List.of());
            for (var child : ((ListTag) obj).value()) {
                node.appendListNode().set(Tag.class, child);
            }
        } else if (obj instanceof LongArrayTag) {
            node.node(SPECIAL_TYPE_KEY).set(LONG_ARRAY_VALUE);
            var baseNode = node.node(SPECIAL_TYPE_VALUES_KEY);
            for (var longVal : (LongArrayTag) obj) {
                baseNode.appendListNode().set(longVal);
            }
        } else if (obj instanceof LongTag) {
            node.set(((LongTag) obj).value() + "L");
        } else if (obj instanceof ShortTag) {
            node.set(((ShortTag) obj).value() + "s");
        } else if (obj instanceof StringTag) {
            node.set(((StringTag) obj).value());
        } else {
            throw new SerializationException("Unknown tag " + obj);
        }
    }
}
