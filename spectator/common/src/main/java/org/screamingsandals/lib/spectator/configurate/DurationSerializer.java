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

package org.screamingsandals.lib.spectator.configurate;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.function.Predicate;

public class DurationSerializer extends ScalarSerializer<Duration> {
    public static final @NotNull DurationSerializer INSTANCE = new DurationSerializer();

    protected DurationSerializer() {
        super(Duration.class);
    }

    @Override
    public @NotNull Duration deserialize(@NotNull Type type, @NotNull Object obj) throws SerializationException {
        if (obj instanceof CharSequence) {
            try {
                var value = obj.toString();
                if (!value.startsWith("P") && !value.startsWith("p")) {
                    value = "P" + value;
                }

                return Duration.parse(value);
            } catch (Throwable ex) {
                throw new SerializationException(ex);
            }
        }
        throw new SerializationException("Value was not of appropriate type");
    }

    @Override
    protected @NotNull Object serialize(@NotNull Duration item, @NotNull Predicate<Class<?>> typeSupported) {
        return item.toString();
    }
}
