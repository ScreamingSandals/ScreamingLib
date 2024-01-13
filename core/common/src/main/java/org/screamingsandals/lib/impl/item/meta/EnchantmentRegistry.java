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

package org.screamingsandals.lib.impl.item.meta;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.EnchantmentSerializer;
import org.screamingsandals.lib.item.meta.Enchantment;
import org.screamingsandals.lib.item.meta.EnchantmentType;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RomanToDecimal;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

@ProvidedService
@ApiStatus.Internal
public abstract class EnchantmentRegistry {

    private static final @NotNull Pattern RESOLUTION_PATTERN = Pattern.compile("^(?<namespaced>[A-Za-z][A-Za-z0-9_.\\-/:]*)(\\s+(?<level>(\\d+|(?=[MDCLXVI])M*(C[MD]|D?C*)(X[CL]|L?X*)(I[XV]|V?I*)))?)?$");
    private static @Nullable EnchantmentRegistry registry;
    private final @NotNull Map<@NotNull Class<?>, Function<@NotNull Object, @Nullable Enchantment>> specialMapping = new HashMap<>();

    protected EnchantmentRegistry() {
        Preconditions.checkArgument(registry == null, "EnchantmentRegistry is already initialized!");
        registry = this;
    }

    public static @Nullable Enchantment resolve(@Nullable Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof Enchantment) {
            return (Enchantment) object;
        }

        Preconditions.checkNotNull(registry, "EnchantmentRegistry is not initialized yet!");

        if (!registry.specialMapping.isEmpty()) {
            for (var sm : registry.specialMapping.entrySet()) {
                if (sm.getKey().isInstance(object)) {
                    return sm.getValue().apply(object);
                }
            }
        }

        if (object instanceof ConfigurationNode) {
            try {
                return EnchantmentSerializer.INSTANCE.deserialize(Enchantment.class, (ConfigurationNode) object);
            } catch (SerializationException e) {
                e.printStackTrace();
                return null;
            }
        } else if (object instanceof Map.Entry) {
            var entry = (Map.Entry<?, ?>) object;
            EnchantmentType holder = EnchantmentType.ofNullable(entry.getKey());
            if (holder != null) {
                int level;
                if (entry.getValue() instanceof Number) {
                    level = ((Number) entry.getValue()).intValue();
                } else if (entry.getValue() instanceof ConfigurationNode) {
                    try {
                        level = Integer.parseInt(((ConfigurationNode) entry.getValue()).getString("1"));
                    } catch (Throwable t) {
                        level = RomanToDecimal.romanToDecimal(((ConfigurationNode) entry.getValue()).getString("I"));
                    }
                } else {
                    try {
                        level = Integer.parseInt(entry.getValue().toString());
                    } catch (Throwable t) {
                        level = RomanToDecimal.romanToDecimal(entry.getValue().toString());
                    }
                }
                return holder.asEnchantment(level);
            }
            return null;
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

        var result = EnchantmentType.ofNullable(location);
        if (result != null) {
            if (levelStr != null && !levelStr.isEmpty()) {
                int level;
                try {
                    level = Integer.parseInt(levelStr);
                } catch (Throwable t) {
                    level = RomanToDecimal.romanToDecimal(levelStr);
                }
                return result.asEnchantment(level);
            }
            return result.asEnchantment();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    @ApiStatus.Internal
    protected <E> void specialType(@NotNull Class<E> eClass, @NotNull Function<@NotNull E, @Nullable Enchantment> function) {
        specialMapping.put(eClass, (Function<Object, Enchantment>) function);
    }
}
