/*
 * Copyright 2023 ScreamingSandals
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

package org.screamingsandals.lib.impl.firework;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.FireworkEffectSerializer;
import org.screamingsandals.lib.firework.FireworkEffect;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistry;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.*;

@ProvidedService
@ApiStatus.Internal
public abstract class FireworkEffectRegistry extends SimpleRegistry<FireworkEffect> {
    private static @Nullable FireworkEffectRegistry registry;

    public FireworkEffectRegistry() {
        super(FireworkEffect.class);
        Preconditions.checkArgument(registry == null, "FireworkEffectRegistry is already initialized!");
        registry = this;

        specialType(ConfigurationNode.class, node -> {
            try {
                return FireworkEffectSerializer.INSTANCE.deserialize(FireworkEffect.class, node);
            } catch (SerializationException e) {
                e.printStackTrace();
            }
            return null;
        });

        specialType(Map.class, map  -> {
            try {
                FireworkEffectSerializer.INSTANCE.deserialize(FireworkEffect.class, BasicConfigurationNode.root().set(map));
            } catch (SerializationException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public static @NotNull FireworkEffectRegistry getInstance() {
        return Preconditions.checkNotNull(registry, "FireworkEffectRegistry is not initialized yet!");
    }
}