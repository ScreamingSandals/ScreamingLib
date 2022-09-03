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

package org.screamingsandals.lib.test.nbt;

import org.junit.jupiter.api.Test;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.nbt.SNBTSerializer;
import org.screamingsandals.lib.nbt.StringTag;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SNBTSerializerTest {
    @Test
    public void testSerializing() {
        var serializer = SNBTSerializer.builder().build();

        var compound = new CompoundTag(Map.of())
                .with("isItTrue?", true)
                .with("lotsOfBytes", new byte[] {1,2,3,4,5,6,7,8,9,10})
                .with("abcdEfg123", (byte) 12)
                .with("nested", new CompoundTag(Map.of())
                        .with("exampleDouble", 3.14)
                        .with("exampleFloat", 3.14F)
                )
                .with("lotsOfInts", new int[] {1,2,3,4,5,6,7,8,9,10})
                .with("regularInt",  654654)
                .with("aCollection", List.of(new StringTag("Bruh??????????"), new StringTag("Not \"bruh\"")))
                .with("veryLongArray", new long[] {1,2,3,4,5,6,7,8,9,10})
                .with("long", 420L)
                .with("short", (short) 69)
                .with("invalid name", "Hello world!");

        var actual = serializer.serialize(compound);

        var expected = "{lotsOfBytes:[B;1b,2b,3b,4b,5b,6b,7b,8b,9b,10b],aCollection:[\"Bruh??????????\",\"Not \\\"bruh\\\"\"],veryLongArray:[1L,2L,3L,4L,5L,6L,7L,8L,9L,10L],lotsOfInts:[I;1,2,3,4,5,6,7,8,9,10],\"invalid name\":\"Hello world!\",short:69s,\"isItTrue?\":true,abcdEfg123:12b,nested:{exampleDouble:3.14d,exampleFloat:3.14f},regularInt:654654,long:420L}";
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializingLA() {
        var serializer = SNBTSerializer.builder().shouldSaveLongArraysDirectly(true).build();

        var compound = new CompoundTag(Map.of())
                .with("isItTrue?", true)
                .with("lotsOfBytes", new byte[] {1,2,3,4,5,6,7,8,9,10})
                .with("abcdEfg123", (byte) 12)
                .with("nested", new CompoundTag(Map.of())
                        .with("exampleDouble", 3.14)
                        .with("exampleFloat", 3.14F)
                )
                .with("lotsOfInts", new int[] {1,2,3,4,5,6,7,8,9,10})
                .with("regularInt",  654654)
                .with("aCollection", List.of(new StringTag("Bruh??????????"), new StringTag("Not \"bruh\"")))
                .with("veryLongArray", new long[] {1,2,3,4,5,6,7,8,9,10})
                .with("long", 420L)
                .with("short", (short) 69)
                .with("invalid name", "Hello world!");

        var actual = serializer.serialize(compound);

        var expected = "{lotsOfBytes:[B;1b,2b,3b,4b,5b,6b,7b,8b,9b,10b],aCollection:[\"Bruh??????????\",\"Not \\\"bruh\\\"\"],veryLongArray:[L;1L,2L,3L,4L,5L,6L,7L,8L,9L,10L],lotsOfInts:[I;1,2,3,4,5,6,7,8,9,10],\"invalid name\":\"Hello world!\",short:69s,\"isItTrue?\":true,abcdEfg123:12b,nested:{exampleDouble:3.14d,exampleFloat:3.14f},regularInt:654654,long:420L}";
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializing() {
        var serializer = SNBTSerializer.builder().build();

        var string = "{lotsOfBytes:[B;true,2b,3b,4b,5b,6b,7b,8b,9b,10],aCollection:[\"Bruh??????????\",\"Not \\\"bruh\\\"\"],    \"veryLongArray\"           :   [L;1L,2L,3b,4,5s,6,7L,8,9L,10L],lotsOfInts:[I;1,2,3,4,5,6,7,8,9,10],\"invalid name\":   \"Hello world!\",short:69s            ,\"isItTrue?\":                 true             ,abcdEfg123:12b,nested:{exampleDouble:3.14d,exampleFloat:3.14f},regularInt:654654,long:420L}";

        var tag = serializer.deserialize(string);

        var expected = new CompoundTag(Map.of())
                .with("isItTrue?", true)
                .with("lotsOfBytes", new byte[] {1,2,3,4,5,6,7,8,9,10})
                .with("abcdEfg123", (byte) 12)
                .with("nested", new CompoundTag(Map.of())
                        .with("exampleDouble", 3.14)
                        .with("exampleFloat", 3.14F)
                )
                .with("lotsOfInts", new int[] {1,2,3,4,5,6,7,8,9,10})
                .with("regularInt",  654654)
                .with("aCollection", List.of(new StringTag("Bruh??????????"), new StringTag("Not \"bruh\"")))
                .with("veryLongArray", new long[] {1,2,3,4,5,6,7,8,9,10})
                .with("long", 420L)
                .with("short", (short) 69)
                .with("invalid name", "Hello world!");

        assertEquals(expected, tag);
    }
}
