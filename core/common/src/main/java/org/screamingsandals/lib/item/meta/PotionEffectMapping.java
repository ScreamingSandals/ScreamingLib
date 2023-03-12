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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.PotionEffectHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.RomanToDecimal;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.key.MappingKey;
import org.screamingsandals.lib.utils.key.ResourceLocation;
import org.screamingsandals.lib.utils.key.NumericMappingKey;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@SuppressWarnings("AlternativeMethodAvailable")
@AbstractService(pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$")
public abstract class PotionEffectMapping extends AbstractTypeMapper<PotionEffectHolder> {

    private static final @NotNull Pattern RESOLUTION_PATTERN = Pattern.compile("^(?<namespaced>[A-Za-z0-9_.\\-/:]+)(\\s+(?<duration>(\\d+|(?=[MDCLXVI])M*(C[MD]|D?C*)(X[CL]|L?X*)(I[XV]|V?I*)))?)?$");
    private static @Nullable PotionEffectMapping potionEffectMapping;

    protected final @NotNull BidirectionalConverter<PotionEffectHolder> potionEffectConverter = BidirectionalConverter.<PotionEffectHolder>build()
            .registerP2W(PotionEffectHolder.class, e -> e)
            .registerP2W(Map.class, map -> {
                try {
                    return PotionEffectHolderSerializer.INSTANCE.deserialize(PotionEffectHolder.class, BasicConfigurationNode.root().set(map));
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
                return null;
            })
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return PotionEffectHolderSerializer.INSTANCE.deserialize(PotionEffectHolder.class, node);
                } catch (SerializationException e) {
                    e.printStackTrace();
                    return null;
                }
            });

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    @OfMethodAlternative(value = PotionEffectHolder.class, methodName = "ofNullable")
    @Contract("null -> null")
    public static @Nullable PotionEffectHolder resolve(@Nullable Object potionEffectObject) {
        if (potionEffectMapping == null) {
            throw new UnsupportedOperationException("PotionEffect mapping is not initialized yet.");
        }
        if (potionEffectObject == null) {
            return null;
        }

        return potionEffectMapping.potionEffectConverter.convertOptional(potionEffectObject).or(() -> {
            var potionEffect = potionEffectObject.toString().trim();

            var matcher = RESOLUTION_PATTERN.matcher(potionEffect);

            if (matcher.matches() && matcher.group("namespaced") != null) {

                MappingKey mappingKey;
                try {
                    var id = Integer.parseInt(matcher.group("namespaced"));
                    mappingKey = NumericMappingKey.of(id);
                } catch (Throwable ignored) {
                    mappingKey = ResourceLocation.of(matcher.group("namespaced"));
                }

                var duration_str = matcher.group("duration");

                if (potionEffectMapping.mapping.containsKey(mappingKey)) {
                    if (duration_str != null && !duration_str.isEmpty()) {
                        int duration;
                        try {
                            duration = Integer.parseInt(duration_str);
                        } catch (Throwable t) {
                            duration = RomanToDecimal.romanToDecimal(duration_str);
                        }
                        return Optional.of(potionEffectMapping.mapping.get(mappingKey).withDuration(duration));
                    } else {
                        return Optional.of(potionEffectMapping.mapping.get(mappingKey));
                    }
                }
            }

            return Optional.empty();
        }).orElse(null);
    }

    @OfMethodAlternative(value = PotionEffectHolder.class, methodName = "all")
    public static @NotNull List<@NotNull PotionEffectHolder> getValues() {
        if (potionEffectMapping == null) {
            throw new UnsupportedOperationException("PotionEffectMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(potionEffectMapping.values);
    }

    protected PotionEffectMapping() {
        if (potionEffectMapping != null) {
            throw new UnsupportedOperationException("PotionEffect mapping is already initialized.");
        }

        potionEffectMapping = this;
    }

    @OnPostConstruct
    public void legacyMapping() {
        mapAlias("SLOWNESS", "SLOW");
        mapAlias("HASTE", "FAST_DIGGING");
        mapAlias("MINING_FATIGUE", "SLOW_DIGGING");
        mapAlias("STRENGTH", "INCREASE_DAMAGE");
        mapAlias("INSTANT_HEALTH", "HEAL");
        mapAlias("INSTANT_DAMAGE", "HARM");
        mapAlias("JUMP_BOOST", "JUMP");
        mapAlias("NAUSEA", "CONFUSION");
        mapAlias("RESISTANCE", "DAMAGE_RESISTANCE");
    }
}
