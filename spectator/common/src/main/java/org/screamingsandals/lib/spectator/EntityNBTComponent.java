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

@LimitedVersionSupport(">= 1.14")
public interface EntityNBTComponent extends NBTComponent {
    @Contract(value = "-> new", pure = true)
    @NotNull
    static EntityNBTComponent.Builder builder() {
        return Spectator.getBackend().entityNBT();
    }

    @NotNull
    String selector();

    @Contract(pure = true)
    @NotNull
    EntityNBTComponent withSelector(@NotNull String selector);

    @Contract(value = "-> new", pure = true)
    @NotNull
    EntityNBTComponent.Builder toBuilder();

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    @NotNull
    EntityNBTComponent withNbtPath(@NotNull String nbtPath);

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    @NotNull
    EntityNBTComponent withSeparator(@Nullable Component separator);

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    @NotNull
    EntityNBTComponent withInterpret(boolean interpret);

    interface Builder extends NBTComponent.Builder<Builder, EntityNBTComponent> {
        @Contract("_ -> this")
        @NotNull
        Builder selector(@NotNull String selector);
    }
}
