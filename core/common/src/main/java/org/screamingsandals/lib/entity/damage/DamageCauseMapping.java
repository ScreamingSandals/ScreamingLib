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

package org.screamingsandals.lib.entity.damage;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.DamageCauseHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.List;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class DamageCauseMapping extends AbstractTypeMapper<DamageCauseHolder> {
    private static DamageCauseMapping damageCauseMapping;

    protected final BidirectionalConverter<DamageCauseHolder> damageCauseConverter = BidirectionalConverter.<DamageCauseHolder>build()
            .registerP2W(DamageCauseHolder.class, d -> d)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return DamageCauseHolderSerializer.INSTANCE.deserialize(DamageCauseHolder.class, node);
                } catch (SerializationException ex) {
                    ex.printStackTrace();
                    return null;
                }
            });

    @ApiStatus.Internal
    public DamageCauseMapping() {
        if (damageCauseMapping != null) {
            throw new UnsupportedOperationException("DamageCauseMapping is already initialized!");
        }
        damageCauseMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DAMAGE_CAUSE)
    @OfMethodAlternative(value = DamageCauseHolder.class, methodName = "ofNullable")
    @Contract("null -> null")
    public static @Nullable DamageCauseHolder resolve(@Nullable Object damageCause) {
        if (damageCauseMapping == null) {
            throw new UnsupportedOperationException("DamageCauseMapping is not initialized yet.");
        }

        if (damageCause == null) {
            return null;
        }

        return damageCauseMapping.damageCauseConverter.convertOptional(damageCause).or(() -> damageCauseMapping.resolveFromMapping(damageCause)).orElse(null);
    }

    @OfMethodAlternative(value = DamageCauseHolder.class, methodName = "all")
    public static @NotNull List<@NotNull DamageCauseHolder> getValues() {
        if (damageCauseMapping == null) {
            throw new UnsupportedOperationException("DamageCauseMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(damageCauseMapping.values);
    }
}
