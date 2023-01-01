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

package org.screamingsandals.lib.world.dimension;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.DimensionHolderSerializer;
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
public abstract class DimensionMapping extends AbstractTypeMapper<DimensionHolder> {
    private static DimensionMapping dimensionMapping;

    protected final BidirectionalConverter<DimensionHolder> dimensionConverter = BidirectionalConverter.<DimensionHolder>build()
            .registerP2W(DimensionHolder.class, d -> d)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return DimensionHolderSerializer.INSTANCE.deserialize(DimensionHolder.class, node);
                } catch (SerializationException ex) {
                    ex.printStackTrace();
                    return null;
                }
            });

    @ApiStatus.Internal
    public DimensionMapping() {
        if (dimensionMapping != null) {
            throw new UnsupportedOperationException("DimensionMapping is already initialized!");
        }
        dimensionMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIMENSION)
    @OfMethodAlternative(value = DimensionHolder.class, methodName = "ofNullable")
    @Contract("null -> null")
    public static @Nullable DimensionHolder resolve(@Nullable Object dimension) {
        if (dimensionMapping == null) {
            throw new UnsupportedOperationException("DimensionMapping is not initialized yet.");
        }

        if (dimension == null) {
            return null;
        }

        return dimensionMapping.dimensionConverter.convertOptional(dimension).or(() -> dimensionMapping.resolveFromMapping(dimension)).orElse(null);
    }

    @OfMethodAlternative(value = DimensionHolder.class, methodName = "all")
    public static @NotNull List<@NotNull DimensionHolder> getValues() {
        if (dimensionMapping == null) {
            throw new UnsupportedOperationException("DimensionMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(dimensionMapping.values);
    }
}
