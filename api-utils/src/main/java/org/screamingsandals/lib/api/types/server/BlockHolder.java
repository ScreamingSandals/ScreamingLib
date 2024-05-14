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

package org.screamingsandals.lib.api.types.server;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.api.Wrapper;

import java.util.function.Function;

/**
 * A class holding a block state, which can be unwrapped to a platform-specific type.
 * <p>
 * To unwrap this type to a platform-specific type, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.block.data.BlockData; // or other type based on the platform
 *
 *  ...
 *
 *  BlockHolder blockHolder = apiManager.methodReturningBlockHolder();
 *  BlockData blockData = blockHolder.as(BlockData.class);
 *
 *  // or shortened
 *  BlockData blockData = apiManager.methodReturningBlockHolder().as(BlockData.class);
 * }</pre>
 * <p>
 * To create a new {@link BlockHolder}, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.block.data.BlockData; // or other type based on the platform
 *
 *  ...
 *
 *  BlockData blockData = ...
 *  BlockHolder blockHolder = BlockHolder.of(blockData);
 * }</pre>
 * <p>
 * Currently supported platform types:
 * <ul>
 *     <li>{@code org.bukkit.block.data.BlockData} (>= 1.13 only)</li>
 *     <li>{@code org.bukkit.material.MaterialData} (<= 1.12.2 only)</li>
 *     <li>{@code org.bukkit.Material} - converting to this type causes a loss of state on all versions (only type is preserved);
 *     for legacy versions (<= 1.12.2), unwrapping to this class can be inaccurate and may cause unexpected type conversion (e.g. diorite -> stone)</li>
 * </ul>
 * <p>
 * NOTE: The type should be directly used only when interacting with an API of a ScreamingLib-based plugin.
 * The ScreamingLib-based plugin itself should use the actual Component type provided by the library.
 * <p>
 * The holder itself lacks identity and should not be compared using {@code ==}, use {@link Object#equals(Object)} instead.
 *
 * @since 2.0.3
 */
@ApiStatus.NonExtendable
public interface BlockHolder extends Wrapper {
    /**
     * Converts a platform-specific type to a new {@link BlockHolder}. This method does not accept null.
     *
     * @param block a block represented by a platform-specific type
     * @return new {@link BlockHolder}
     * @throws IllegalArgumentException if the platform block object is not convertable or null has been passed
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @NotNull BlockHolder of(@NotNull Object block) {
        var result = ofNullable(block);
        if (result == null) {
            throw new IllegalArgumentException("Could not wrap block: " + block);
        }
        return result;
    }

    /**
     * Converts a platform-specific type to a new {@link BlockHolder}.
     *
     * @param block a block represented by a platform-specific type or null
     * @return new {@link BlockHolder} or null if the block has not been passed
     * @throws IllegalArgumentException if the platform block object is not convertable
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @Nullable BlockHolder ofNullable(@Nullable Object block) {
        if (Provider.provider == null) {
            throw new UnsupportedOperationException("A provider for " + BlockHolder.class.getSimpleName() + " has not been registered yet!");
        }
        if (block == null) {
            return null;
        }
        return Provider.provider.apply(block);
    }

    @ApiStatus.Internal
    final class Provider {
        private static @Nullable Function<@NotNull Object, @Nullable BlockHolder> provider;

        private Provider() {
        }

        public static void registerProvider(@NotNull Function<@NotNull Object, @Nullable BlockHolder> provider) {
            if (Provider.provider != null) {
                throw new UnsupportedOperationException("A provider for " + BlockHolder.class.getSimpleName() + " has already been registered!");
            }
            Provider.provider = provider;
        }
    }
}
