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

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.world.gamerule.GameRuleHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class GameRuleHolderSerializer extends AbstractScreamingSerializer implements TypeSerializer<GameRuleHolder> {
    public static final GameRuleHolderSerializer INSTANCE = new GameRuleHolderSerializer();

    @Override
    public GameRuleHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            return GameRuleHolder.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(Type type, @Nullable GameRuleHolder obj, ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.platformName());
    }
}
