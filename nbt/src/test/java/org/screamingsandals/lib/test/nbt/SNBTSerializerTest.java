/*
 * Copyright 2025 ScreamingSandals
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
import org.screamingsandals.lib.nbt.ByteTag;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.nbt.IntArrayTag;
import org.screamingsandals.lib.nbt.ListTag;
import org.screamingsandals.lib.nbt.SNBTSerializer;
import org.screamingsandals.lib.nbt.StringTag;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        var expected = "{lotsOfBytes:[B;1b,2b,3b,4b,5b,6b,7b,8b,9b,10b],aCollection:[\"Bruh??????????\",\"Not \\\"bruh\\\"\"],veryLongArray:[1L,2L,3L,4L,5L,6L,7L,8L,9L,10L],lotsOfInts:[I;1,2,3,4,5,6,7,8,9,10],\"invalid name\":\"Hello world!\",short:69s,\"isItTrue?\":1b,abcdEfg123:12b,nested:{exampleDouble:3.14d,exampleFloat:3.14f},regularInt:654654,long:420L}";
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

        var expected = "{lotsOfBytes:[B;1b,2b,3b,4b,5b,6b,7b,8b,9b,10b],aCollection:[\"Bruh??????????\",\"Not \\\"bruh\\\"\"],veryLongArray:[L;1L,2L,3L,4L,5L,6L,7L,8L,9L,10L],lotsOfInts:[I;1,2,3,4,5,6,7,8,9,10],\"invalid name\":\"Hello world!\",short:69s,\"isItTrue?\":1b,abcdEfg123:12b,nested:{exampleDouble:3.14d,exampleFloat:3.14f},regularInt:654654,long:420L}";
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

    @Test
    public void testDeserializingHeterogeneous() {
        var serializer = SNBTSerializer.builder().build();

        var string = "{list:[1B,test,{a: b}]}";

        var tag = serializer.deserialize(string);

        var expected = new CompoundTag(Map.of())
                .with("list", new ListTag(List.of(
                        CompoundTag.wrapper(new ByteTag((byte) 1)),
                        CompoundTag.wrapper(new StringTag("test")),
                        new CompoundTag(Map.of("a", new StringTag("b")))
                )));

        assertEquals(expected, tag);
    }

    @Test
    public void testSerializingHeterogeneous() {
        var serializer = SNBTSerializer.builder().build();

        var tag = new CompoundTag(Map.of())
                .with("list", new ListTag(List.of(
                        CompoundTag.wrapper(new ByteTag((byte) 1)),
                        CompoundTag.wrapper(new StringTag("test")),
                        new CompoundTag(Map.of("a", new StringTag("b")))
                )));

        var actual = serializer.serialize(tag);

        var expected = "{list:[{\"\":1b},{\"\":\"test\"},{a:\"b\"}]}";

        assertEquals(expected, actual);
    }

    @Test
    public void testSerializingHeterogeneousNonWrapped() {
        var serializer = SNBTSerializer.builder().serializeHeterogeneousListNonWrapped(true).build();

        var tag = new CompoundTag(Map.of())
                .with("list", new ListTag(List.of(
                        CompoundTag.wrapper(new ByteTag((byte) 1)),
                        CompoundTag.wrapper(new StringTag("test")),
                        new CompoundTag(Map.of("a", new StringTag("b")))
                )));

        var actual = serializer.serialize(tag);

        var expected = "{list:[1b,\"test\",{a:\"b\"}]}";

        assertEquals(expected, actual);
    }

    @Test
    public void testBoolFunctionTrue() {
        var serializer = SNBTSerializer.builder().build();

        var string = "bool(true)";

        var actual = serializer.deserialize(string);

        var expected = ByteTag.TRUE;

        assertEquals(expected, actual);
    }

    @Test
    public void testBoolFunctionFalse() {
        var serializer = SNBTSerializer.builder().build();

        var string = "bool(false)";

        var actual = serializer.deserialize(string);

        var expected = ByteTag.FALSE;

        assertEquals(expected, actual);
    }

    @Test
    public void testBoolFunctionNonZero() {
        var serializer = SNBTSerializer.builder().build();

        var string = "bool(55)";

        var actual = serializer.deserialize(string);

        var expected = ByteTag.TRUE;

        assertEquals(expected, actual);
    }

    @Test
    public void testBoolFunctionZero() {
        var serializer = SNBTSerializer.builder().build();

        var string = "bool(0)";

        var actual = serializer.deserialize(string);

        var expected = ByteTag.FALSE;

        assertEquals(expected, actual);
    }

    @Test
    public void testBoolFunctionInvalid() {
        var serializer = SNBTSerializer.builder().build();

        var string = "bool(text)";

        assertThrows(IllegalArgumentException.class, () -> serializer.deserialize(string));
    }

    @Test
    public void testUuidFunction() {
        var serializer = SNBTSerializer.builder().build();

        var string = "uuid(f81d4fae-7dec-11d0-a765-00a0c91e6bf6)";

        var actual = serializer.deserialize(string);

        var expected = new IntArrayTag(new int[] {-132296786, 2112623056, -1486552928, -920753162});

        assertEquals(expected, actual);
    }

    @Test
    public void testUuidFunctionInvalid() {
        var serializer = SNBTSerializer.builder().build();

        var string = "bool(text)";

        assertThrows(IllegalArgumentException.class, () -> serializer.deserialize(string));
    }
}
