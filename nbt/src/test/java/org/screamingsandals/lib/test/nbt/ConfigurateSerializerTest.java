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

package org.screamingsandals.lib.test.nbt;

import org.junit.jupiter.api.Test;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.nbt.StringTag;
import org.screamingsandals.lib.nbt.Tag;
import org.screamingsandals.lib.nbt.configurate.TagSerializer;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurateSerializerTest {
    @Test
    public void testSerializer() throws SerializationException {
        var source = new CompoundTag(Map.of())
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

        var root = BasicConfigurationNode.root(ConfigurationOptions.defaults().serializers(builder -> builder.register(Tag.class, TagSerializer.INSTANCE)));
        root.set(source);
        var deserialized = root.get(Tag.class);

        assertEquals(source, deserialized);
    }
}
