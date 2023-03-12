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
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface DimensionType extends RegistryItem, RawValueHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.DIMENSION)
    boolean is(@Nullable Object object);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.DIMENSION)
    boolean is(@Nullable Object @NotNull... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.DIMENSION)
    static @NotNull DimensionType of(@NotNull Object dimension) {
        var result = ofNullable(dimension);
        Preconditions.checkNotNullIllegal(result, "Could not find dimension: " + dimension);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIMENSION)
    @Contract("null -> null")
    static @Nullable DimensionType ofNullable(@Nullable Object dimension) {
        if (dimension instanceof DimensionType) {
            return (DimensionType) dimension;
        }
        return DimensionRegistry.getInstance().resolveMapping(dimension);
    }

    static @NotNull RegistryItemStream<@NotNull DimensionType> all() {
        return DimensionRegistry.getInstance().getRegistryItemStream();
    }
}
