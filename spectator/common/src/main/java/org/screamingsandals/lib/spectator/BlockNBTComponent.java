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
import org.screamingsandals.lib.impl.spectator.Spectator;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

@LimitedVersionSupport(">= 1.14")
public interface BlockNBTComponent extends NBTComponent {
    @Contract(value = "-> new", pure = true)
    static BlockNBTComponent.@NotNull Builder builder() {
        return Spectator.getBackend().blockNBT();
    }

    // TODO: real position API
    @NotNull String blockPosition();

    @Contract(pure = true)
    @NotNull BlockNBTComponent withBlockPosition(@NotNull String blockPosition);

    @Contract(value = "-> new", pure = true)
    BlockNBTComponent.@NotNull Builder toBuilder();

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    @NotNull BlockNBTComponent withInterpret(boolean interpret);

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    @NotNull BlockNBTComponent withNbtPath(@NotNull String nbtPath);

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    @NotNull BlockNBTComponent withSeparator(@Nullable Component separator);

    interface Builder extends NBTComponent.Builder<Builder, BlockNBTComponent> {
        @Contract("_ -> this")
        @NotNull Builder blockPosition(@NotNull String blockPosition);
    }
}
