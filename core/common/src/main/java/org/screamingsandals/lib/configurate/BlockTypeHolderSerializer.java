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

package org.screamingsandals.lib.configurate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.stream.Collectors;

public class BlockTypeHolderSerializer extends AbstractScreamingSerializer implements TypeSerializer<BlockTypeHolder> {
    public static final @NotNull BlockTypeHolderSerializer INSTANCE = new BlockTypeHolderSerializer();

    @Override
    public @NotNull BlockTypeHolder deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            return BlockTypeHolder.of(type);
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable BlockTypeHolder obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        if (Server.isVersion(1, 13)) {
            var builder = new StringBuilder(obj.platformName());
            var data = obj.flatteningData();
            if (!data.isEmpty()) {
                builder.append('[');
                builder.append(data
                        .entrySet()
                        .stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .collect(Collectors.joining(",")));
                builder.append(']');
            }
            node.set(builder.toString());
        } else {
            node.set(obj.platformName() + ":" + obj.legacyData());
        }
    }
}
