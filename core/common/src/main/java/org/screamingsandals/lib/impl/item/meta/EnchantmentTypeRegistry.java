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
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistry;
import org.screamingsandals.lib.item.meta.EnchantmentType;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ProvidedService;

@ProvidedService
@ApiStatus.Internal
public abstract class EnchantmentTypeRegistry extends SimpleRegistry<EnchantmentType> {

    private static @Nullable EnchantmentTypeRegistry registry;

    protected EnchantmentTypeRegistry() {
        super(EnchantmentType.class);
        Preconditions.checkArgument(registry == null, "EnchantmentTypeRegistry is already initialized!");
        registry = this;
    }

    public static @NotNull EnchantmentTypeRegistry getInstance() {
        return Preconditions.checkNotNull(registry, "EnchantmentTypeRegistry is not initialized yet!");
    }
}
