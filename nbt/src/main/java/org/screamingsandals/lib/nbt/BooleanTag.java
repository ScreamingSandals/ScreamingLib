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

@Data
@Accessors(fluent = true)
public final class BooleanTag implements Tag, NumericTag {
    private final boolean value;

    @Override
    public int intValue() {
        return value ? 1 : 0;
    }

    @Override
    public long longValue() {
        return value ? 1L : 0L;
    }

    @Override
    public float floatValue() {
        return value ? 1F : 0F;
    }

    @Override
    public double doubleValue() {
        return value ? 1.0 : 0.0;
    }

    @Override
    public byte byteValue() {
        return value ? (byte) 1 : (byte) 0;
    }

    @Override
    public short shortValue() {
        return value ? (short) 1 : (short) 0;
    }

    @Override
    public boolean booleanValue() {
        return value;
    }
}
