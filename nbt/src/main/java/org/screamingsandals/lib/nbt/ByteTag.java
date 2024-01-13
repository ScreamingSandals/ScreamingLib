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

package org.screamingsandals.lib.nbt;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Data
@Accessors(fluent = true)
public final class ByteTag implements Tag, NumericTag {
    public static final @NotNull ByteTag TRUE = new ByteTag((byte) 1);
    public static final @NotNull ByteTag FALSE = new ByteTag((byte) 0);

    private final byte value;

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public byte byteValue() {
        return value;
    }

    @Override
    public short shortValue() {
        return value;
    }

    @Override
    public boolean booleanValue() {
        return value != 0;
    }

    @Override
    public boolean canHoldDataOfTag(@NotNull NumericTag tag) {
        return tag instanceof ByteTag;
    }

    @Override
    public @NotNull NumericTag convert(@NotNull NumericTag tag) {
        return new ByteTag(tag.byteValue());
    }
}
