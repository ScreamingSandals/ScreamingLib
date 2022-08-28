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

import java.util.concurrent.atomic.AtomicBoolean;

@Builder
public class SNBTSerializer {
    @Builder.Default
    private final boolean shouldSaveLongArraysDirectly = false; // not present before 1.12

    @NotNull
    public String serialize(@NotNull Tag tag) {
        if (tag instanceof BooleanTag) {
            return ((BooleanTag) tag).value() ? "true" : "false";
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

    public Tag deserialize(String value) {
        return null; // TODO
    }
}
