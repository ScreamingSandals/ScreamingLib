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

package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.ResourceLocation;

@LimitedVersionSupport(">= 1.15")
public interface StorageNBTComponent extends NBTComponent {
    @Contract(value = "-> new", pure = true)
    static StorageNBTComponent.@NotNull Builder builder() {
        return Spectator.getBackend().storageNBT();
    }

    @NotNull ResourceLocation storageKey();

    @Contract(pure = true)
    @NotNull StorageNBTComponent withStorageKey(@NotNull ResourceLocation storageKey);

    @Contract(value = "-> new", pure = true)
    StorageNBTComponent.@NotNull Builder toBuilder();

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    @NotNull StorageNBTComponent withInterpret(boolean interpret);

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    @NotNull StorageNBTComponent withNbtPath(@NotNull String nbtPath);

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    @NotNull StorageNBTComponent withSeparator(@Nullable Component separator);

    interface Builder extends NBTComponent.Builder<Builder, StorageNBTComponent> {
        @Contract("_ -> this")
        @NotNull Builder storageKey(@NotNull ResourceLocation storageKey);
    }
}
