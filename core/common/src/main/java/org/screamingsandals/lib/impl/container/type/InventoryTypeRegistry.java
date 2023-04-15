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

package org.screamingsandals.lib.impl.container.type;

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.container.type.InventoryType;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistry;

@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
@ProvidedService
@ApiStatus.Internal
public abstract class InventoryTypeRegistry extends SimpleRegistry<InventoryType> {
    private static @Nullable InventoryTypeRegistry registry;

    public InventoryTypeRegistry() {
        super(InventoryType.class);
        Preconditions.checkArgument(registry == null, "InventoryTypeRegistry is already initialized!");
        registry = this;
    }

    public static @NotNull InventoryTypeRegistry getInstance() {
        return Preconditions.checkNotNull(registry, "InventoryTypeRegistry is not initialized yet!");
    }
}
