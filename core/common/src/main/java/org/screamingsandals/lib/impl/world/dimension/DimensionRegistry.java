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

package org.screamingsandals.lib.impl.world.dimension;

import org.jetbrains.annotations.*;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistry;
import org.screamingsandals.lib.world.dimension.DimensionType;

@ProvidedService
@ApiStatus.Internal
public abstract class DimensionRegistry extends SimpleRegistry<DimensionType> {
    private static @Nullable DimensionRegistry registry;

    public DimensionRegistry() {
        super(DimensionType.class);
        Preconditions.checkArgument(registry == null, "DimensionRegistry is already initialized!");
        registry = this;
    }

    public static @NotNull DimensionRegistry getInstance() {
        return Preconditions.checkNotNull(registry, "DimensionRegistry is not initialized yet!");
    }
}
