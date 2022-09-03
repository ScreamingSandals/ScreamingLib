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

import lombok.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Builder
public class SNBTSerializer {
    @Builder.Default
    private final boolean shouldSaveLongArraysDirectly = false; // not present before 1.12
    @Builder.Default
    private final boolean convertBooleanTagsToByteTags = false;

    @NotNull
    public String serialize(@NotNull Tag tag) {
        if (tag instanceof BooleanTag) {
            if (convertBooleanTagsToByteTags) {
                return ((BooleanTag) tag).value() ? "1b" : "0b";
            } else {
                return ((BooleanTag) tag).value() ? "true" : "false";
            }
        } else if (tag instanceof ByteArrayTag) {
            var builder = new StringBuilder();
            builder.append("[B;");
            var first = true;
            for (var number : ((ByteArrayTag) tag).value()) {
                if (!first) {
                    builder.append(",");
                } else {
                    first = false;
                }
                builder.append(number).append("b");
            }
            builder.append("]");
            return builder.toString();
        } else if (tag instanceof ByteTag) {
            return ((ByteTag) tag).value() + "b";
        } else if (tag instanceof CompoundTag) {
            var builder = new StringBuilder();
            builder.append("{");
            var first = new AtomicBoolean(true);
            ((CompoundTag) tag).forEach((s, tag1) -> {
                if (!first.get()) {
                    builder.append(",");
                } else {
                    first.set(false);
                }
                if (s.matches("[a-zA-Z][a-zA-Z\\d]*")) {
                    builder.append(s).append(":");
                } else {
                    builder.append("\"").append(s.replace("\"", "\\\"")).append("\":");
                }
                builder.append(serialize(tag1));
            });
            builder.append("}");
            return builder.toString();
        } else if (tag instanceof DoubleTag) {
            return ((DoubleTag) tag).value() + "d";
        } else if (tag instanceof FloatTag) {
            return ((FloatTag) tag).value() + "f";
        } else if (tag instanceof IntArrayTag) {
            var builder = new StringBuilder();
            builder.append("[I;");
            var first = true;
            for (var number : ((IntArrayTag) tag).value()) {
                if (!first) {
                    builder.append(",");
                } else {
                    first = false;
                }
                builder.append(number);
            }
            builder.append("]");
            return builder.toString();
        } else if (tag instanceof IntTag) {
            return String.valueOf(((IntTag) tag).value());
        } else if (tag instanceof ListTag) {
            var builder = new StringBuilder();
            builder.append("[");
            var first = true;
            for (var t : ((ListTag) tag).value()) {
                if (!first) {
                    builder.append(",");
                } else {
                    first = false;
                }
                builder.append(serialize(t));
            }
            builder.append("]");
            return builder.toString();
        } else if (tag instanceof LongArrayTag) {
            var builder = new StringBuilder();
            if (shouldSaveLongArraysDirectly) {
                builder.append("[L;");
            } else {
                builder.append("[");
            }
            var first = true;
            for (var number : ((LongArrayTag) tag).value()) {
                if (!first) {
                    builder.append(",");
                } else {
                    first = false;
                }
                builder.append(number).append("L");
            }
            builder.append("]");
            return builder.toString();
        } else if (tag instanceof LongTag) {
            return ((LongTag) tag).value() + "L";
        } else if (tag instanceof ShortTag) {
            return ((ShortTag) tag).value() + "s";
        } else if (tag instanceof StringTag) {
            return "\"" + ((StringTag) tag).value().replace("\"", "\\\"") + "\"";
        }
        throw new IllegalArgumentException("Unknown tag " + tag);
    }

    @NotNull
    public Tag deserialize(@NotNull String input) {
        var chars = input.toCharArray();
        var i = new AtomicInteger(0);
        return deserialize(chars, i);
    }

    @NotNull
    private Tag deserialize(char @NotNull [] chars, @NotNull AtomicInteger i) {
        skipWhitespaces(chars, i);
        var c = chars[i.get()];
        if (c == '{') {
            i.incrementAndGet();
            var map = new HashMap<String, Tag>();
            skipWhitespaces(chars, i);
            while (i.get() < chars.length) {
                if (chars[i.get()] == ',') {
                    i.incrementAndGet();
                    continue; // skip separator
                }
                if (chars[i.get()] == '}') {
                    i.incrementAndGet();
                    break;
                }
                skipWhitespaces(chars, i);
                String key;
                if (chars[i.get()] == '\"' || chars[i.get()] == '\'') {
                    var usedQuote = chars[i.get()];
                    i.incrementAndGet();
                    key = readStringUntil(chars, i, usedQuote);
                } else {
                    key = readUntilControlSymbol(chars, i);
                }
                skipWhitespaces(chars, i);
                if (i.get() >= chars.length || chars[i.get()] != ':') {
                    throw new IllegalArgumentException("Invalid compound tag: there is no value for key " + key + " (position " + i.get() + ")");
                }
                i.incrementAndGet();
                skipWhitespaces(chars, i);
                if (i.get() >= chars.length) {
                    throw new IllegalArgumentException("Invalid compound tag: there is no value for key " + key + " (position " + i.get() + ")");
                }
                map.put(key, deserialize(chars, i));
                skipWhitespaces(chars, i);
            }
            return new CompoundTag(map); // yeah, we allow not ended compound tags somehow
        } else if (c == '[') {
            i.incrementAndGet();
            if (chars.length > i.get() + 1 && chars[i.get() + 1] == ';') {
                // Arrays
                var firstChar = chars[i.get()];
                i.getAndAdd(2);
                skipWhitespaces(chars, i);
                if (firstChar == 'B') {
                    var byteArrayString = readStringUntil(chars, i, ']');
                    var bytes = new ArrayList<Byte>();
                    for (var byteValue : byteArrayString.split(",")) {
                        byteValue = byteValue.trim();
                        try {
                            if (byteValue.endsWith("b") || byteValue.endsWith("B")) {
                                bytes.add(Byte.parseByte(byteValue.substring(0, byteValue.length() - 1)));
                                // We are kinda lenient and allow something that is not part of the definition
                            } else if ("true".equalsIgnoreCase(byteValue)) {
                                bytes.add((byte) 1);
                            } else if ("false".equalsIgnoreCase(byteArrayString)) {
                                bytes.add((byte) 0);
                            } else {
                                bytes.add(Byte.parseByte(byteValue));
                            }
                        } catch (NumberFormatException exception) {
                            throw new IllegalArgumentException("Invalid byte tag: " + byteValue + " (position " + i.get() + ")", exception);
                        }
                    }
                    var result = new byte[bytes.size()];
                    for (int j = 0; j < bytes.size(); j++) {
                        result[j] = bytes.get(j);
                    }
                    i.incrementAndGet();
                    return new ByteArrayTag(result);
                } else if (firstChar == 'I') {
                    var intArrayString = readStringUntil(chars, i, ']');
                    var stream = IntStream.builder();
                    for (var intValue : intArrayString.split(",")) {
                        intValue = intValue.trim();
                        try {
                            // We are kinda lenient because we allow suffixes. We also allow any short or byte value because they are compatible with int
                            if (intValue.endsWith("i")
                                    || intValue.endsWith("I")
                                    || intValue.endsWith("s")
                                    || intValue.endsWith("S")
                                    || intValue.endsWith("b")
                                    || intValue.endsWith("B")
                            ) {
                                stream.add(Integer.parseInt(intValue.substring(0, intValue.length() - 1)));
                            } else if ("true".equalsIgnoreCase(intValue)) {
                                stream.add(1);
                            } else if ("false".equalsIgnoreCase(intValue)) {
                                stream.add(0);
                            } else {
                                stream.add(Integer.parseInt(intValue));
                            }
                        } catch (NumberFormatException exception) {
                            throw new IllegalArgumentException("Invalid integer tag: " + intValue + " (position " + i.get() + ")", exception);
                        }
                    }
                    i.incrementAndGet();
                    return new IntArrayTag(stream.build().toArray());
                } else if (firstChar == 'L') {
                    var longArrayString = readStringUntil(chars, i, ']');
                    var stream = LongStream.builder();
                    for (var longValue : longArrayString.split(",")) {
                        longValue = longValue.trim();
                        try {
                            // We are lenient because we allow any integer, short or byte value because they are compatible with long
                            if (longValue.endsWith("l")
                                    || longValue.endsWith("L")
                                    || longValue.endsWith("i")
                                    || longValue.endsWith("I")
                                    || longValue.endsWith("s")
                                    || longValue.endsWith("S")
                                    || longValue.endsWith("b")
                                    || longValue.endsWith("B")
                            ) {
                                stream.add(Long.parseLong(longValue.substring(0, longValue.length() - 1)));
                            } else if ("true".equalsIgnoreCase(longValue)) {
                                stream.add(1);
                            } else if ("false".equalsIgnoreCase(longValue)) {
                                stream.add(0);
                            } else {
                                stream.add(Long.parseLong(longValue));
                            }
                        } catch (NumberFormatException exception) {
                            throw new IllegalArgumentException("Invalid long tag: " + longValue + " (position " + i.get() + ")", exception);
                        }
                    }
                    i.incrementAndGet();
                    return new LongArrayTag(stream.build().toArray());
                } else {
                    throw new IllegalArgumentException("Unknown array of type " + firstChar + " (position " + i.get() + ")");
                }
            }
            // Lists
            var list = new ArrayList<Tag>();
            while (i.get() < chars.length) {
                if (chars[i.get()] == ']') {
                    i.incrementAndGet();
                    break;
                }
                if (chars[i.get()] == ',') {
                    i.incrementAndGet();
                    continue; // skip separator
                }
                skipWhitespaces(chars, i);
                if (i.get() >= chars.length) {
                    break;
                }
                list.add(deserialize(chars, i));
                skipWhitespaces(chars, i);
            }
            return new ListTag(list);
        } else if (c == '\'' || c == '\"') {
            i.incrementAndGet();
            return readUntil(chars, i, c);
        } else {
            var value = readUntilControlSymbol(chars, i);
            if ("true".equalsIgnoreCase(value)) {
                if (convertBooleanTagsToByteTags) {
                    return new ByteTag((byte) 1);
                } else {
                    return BooleanTag.TRUE;
                }
            } else if ("false".equalsIgnoreCase(value)) {
                if (convertBooleanTagsToByteTags) {
                    return new ByteTag((byte) 0);
                } else {
                    return BooleanTag.FALSE;
                }
            }
            try {
                var lastChar = value.charAt(value.length() - 1);
                var substring = value.substring(0, value.length() - 1);
                if (lastChar == 'b' || lastChar == 'B') {
                    return new ByteTag(Byte.parseByte(substring));
                } else if (lastChar == 's' || lastChar == 'S') {
                    return new ShortTag(Short.parseShort(substring));
                } else if (lastChar == 'i' || lastChar == 'I') {
                    // not defined by SNBT, but Adventure NBT library understands it as well
                    return new IntTag(Integer.parseInt(substring));
                } else if (lastChar == 'l' || lastChar == 'L') {
                    return new LongTag(Long.parseLong(substring));
                } else if (lastChar == 'f' || lastChar == 'F') {
                    var number = Float.parseFloat(substring);
                    if (Float.isFinite(number)) { // don't accept NaN and Infinity
                        return new FloatTag(number);
                    }
                } else if (lastChar == 'd' || lastChar == 'D') {
                    var number = Double.parseDouble(substring);
                    if (Double.isFinite(number)) { // don't accept NaN and Infinity
                        return new DoubleTag(number);
                    }
                } else if (value.contains(".")) {
                    var number = Double.parseDouble(value);
                    if (Double.isFinite(number)) { // don't accept NaN and Infinity
                        return new DoubleTag(number);
                    }
                } else {
                    return new IntTag(Integer.parseInt(value));
                }
            } catch (Throwable ignored) {
            }
            return new StringTag(value);
        }
    }

    @NotNull
    private StringTag readUntil(char @NotNull [] chars, @NotNull AtomicInteger i, char c) {
        return new StringTag(readStringUntil(chars, i, c));
    }

    @NotNull
    private String readStringUntil(char @NotNull [] chars, @NotNull AtomicInteger i, char c) {
        var builder = new StringBuilder();
        var escaped = false;
        for (; i.get() < chars.length; i.incrementAndGet()) {
            var cc = chars[i.get()];
            if (escaped) {
                escaped = false;
                builder.append(cc);
            } else if (cc == c){
                i.incrementAndGet();
                return builder.toString();
            } else if (cc == '\\') {
                escaped = true;
            } else {
                builder.append(cc);
            }
        }
        throw new IllegalArgumentException("String not properly ended!");
    }

    @NotNull
    private String readUntilControlSymbol(char @NotNull [] chars, @NotNull AtomicInteger i) {
        var builder = new StringBuilder();
        var escaped = false;
        for (; i.get() < chars.length; i.incrementAndGet()) {
            var cc = chars[i.get()];
            if (escaped) { // escaping is something the format does not allow, however we are lenient
                escaped = false;
                if (cc == ',' || cc == ']' || cc == '}' || cc == ':') { // but don't escape endings
                    builder.append('\\');
                    break;
                }
            } else if (Character.isWhitespace(cc) || cc == ',' || cc == ']' || cc == '}' || cc == ':') {
                break;
            } else if (cc == '\\') {
                escaped = true;
            } else {
                builder.append(cc);
            }
        }
        return builder.toString();
    }

    private void skipWhitespaces(char @NotNull [] chars, @NotNull AtomicInteger i) {
        while (i.get() < chars.length && Character.isWhitespace(chars[i.get()])) {
            i.incrementAndGet();
        }
    }
}
