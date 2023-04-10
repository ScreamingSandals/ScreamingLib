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

package org.screamingsandals.lib.item.meta;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.PotionEffectHolderSerializer;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RomanToDecimal;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.Registry;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Map;
import java.util.regex.Pattern;

@ProvidedService
@ApiStatus.Internal
public abstract class PotionEffectRegistry extends Registry<PotionEffect> {

    private static final @NotNull Pattern RESOLUTION_PATTERN = Pattern.compile("^(?<namespaced>[A-Za-z][A-Za-z0-9_.\\-/:]*)(\\s+(?<level>(\\d+|(?=[MDCLXVI])M*(C[MD]|D?C*)(X[CL]|L?X*)(I[XV]|V?I*)))?)?$");
    private static @Nullable PotionEffectRegistry registry;

    protected PotionEffectRegistry() {
        super(PotionEffect.class);
        Preconditions.checkArgument(registry == null, "PotionEffectRegistry is already initialized!");
        registry = this;
    }

    public static @NotNull PotionEffectRegistry getInstance() {
        return Preconditions.checkNotNull(registry, "PotionEffectRegistry is not initialized yet!");
    }

    @Override
    protected final @Nullable PotionEffect resolveMapping0(@NotNull Object object) {
        if (object instanceof ConfigurationNode) {
            try {
                return PotionEffectHolderSerializer.INSTANCE.deserialize(Enchantment.class, (ConfigurationNode) object);
            } catch (SerializationException e) {
                e.printStackTrace();
                return null;
            }
        } else if (object instanceof Map) {
            try {
                return PotionEffectHolderSerializer.INSTANCE.deserialize(PotionEffect.class, BasicConfigurationNode.root().set(object));
            } catch (SerializationException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Nullable ResourceLocation location = null;
        @Nullable String levelStr = null;
        if (object instanceof ResourceLocation) {
            location = (ResourceLocation) object;
        } else {
            var enchantment = object.toString().trim();

            var matcher = RESOLUTION_PATTERN.matcher(enchantment);

            if (matcher.matches() && matcher.group("namespaced") != null) {
                location = ResourceLocation.of(matcher.group("namespaced"));

                levelStr = matcher.group("level");

            }
        }

        if (location == null) {
            return null;
        }

        var result = this.resolveMappingPlatform(location);
        if (result != null) {
            if (levelStr != null && !levelStr.isEmpty()) {
                int level;
                try {
                    level = Integer.parseInt(levelStr);
                } catch (Throwable t) {
                    level = RomanToDecimal.romanToDecimal(levelStr);
                }
                return result.withDuration(level);
            }
            return result;
        }

        return null;
    }

    protected abstract @Nullable PotionEffect resolveMappingPlatform(@NotNull ResourceLocation location);
}
