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

package org.screamingsandals.lib.firework;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.FireworkEffectHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.*;

@SuppressWarnings("AlternativeMethodAvailable")
@AbstractService
public abstract class FireworkEffectMapping extends AbstractTypeMapper<FireworkEffectHolder> {
    private static FireworkEffectMapping fireworkEffectMapping;

    protected final BidirectionalConverter<FireworkEffectHolder> fireworkEffectConverter = BidirectionalConverter.<FireworkEffectHolder>build()
            .registerP2W(FireworkEffectHolder.class, e -> e)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return FireworkEffectHolderSerializer.INSTANCE.deserialize(FireworkEffectHolder.class, node);
                } catch (SerializationException e) {
                    e.printStackTrace();
                    return null;
                }
            })
            .registerP2W(Map.class, map -> {
                try {
                    FireworkEffectHolderSerializer.INSTANCE.deserialize(FireworkEffectHolder.class, BasicConfigurationNode.root().set(map));
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
                return null;
            });

    @ApiStatus.Internal
    public FireworkEffectMapping() {
        if (fireworkEffectMapping != null) {
            throw new UnsupportedOperationException("FireworkEffectMapping is already initialized.");
        }

        fireworkEffectMapping = this;
    }

    @OnPostConstruct
    public void postConstruct() {
        mapAlias("SMALL", "BALL");
        mapAlias("LARGE", "BALL_LARGE");
    }

    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    @OfMethodAlternative(value = FireworkEffectHolder.class, methodName = "ofNullable")
    @Contract("null -> null")
    public static @Nullable FireworkEffectHolder resolve(@Nullable Object fireworkEffectObject) {
        if (fireworkEffectMapping == null) {
            throw new UnsupportedOperationException("FireworkEffectMapping is not initialized yet.");
        }
        if (fireworkEffectObject == null) {
            return null;
        }

        return fireworkEffectMapping.fireworkEffectConverter.convertOptional(fireworkEffectObject).or(() -> fireworkEffectMapping.resolveFromMapping(fireworkEffectObject)).orElse(null);
    }

    @OfMethodAlternative(value = FireworkEffectHolder.class, methodName = "all")
    public static @NotNull List<@NotNull FireworkEffectHolder> getValues() {
        if (fireworkEffectMapping == null) {
            throw new UnsupportedOperationException("FireworkEffectMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(fireworkEffectMapping.values);
    }
}
