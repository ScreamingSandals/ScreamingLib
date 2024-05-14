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
 * A class holding a block snapshot, which can be unwrapped to a platform-specific type.
 * <p>
 * To unwrap this type to a platform-specific type, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.block.BlockState; // or other type based on the platform
 *
 *  ...
 *
 *  BlockSnapshotHolder blockSnapshotHolder = apiManager.methodReturningBlockSnapshotHolder();
 *  BlockState blockState = blockSnapshotHolder.as(BlockState.class);
 *
 *  // or shortened
 *  BlockState blockState = apiManager.methodReturningBlockSnapshotHolder().as(BlockState.class);
 * }</pre>
 * <p>
 * To create a new {@link BlockSnapshotHolder}, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.block.BlockState; // or other type based on the platform
 *
 *  ...
 *
 *  blockState blockState = ...
 *  BlockSnapshotHolder blockSnapshotHolder = BlockSnapshotHolder.of(blockState);
 * }</pre>
 * <p>
 * Currently supported platform types:
 * <ul>
 *     <li>{@code org.bukkit.block.BlockState} - This type may not necessarily represent snapshot when it comes to a tile entity,
 *     do not modify underlying wrapped block state. You can also use newer API to obtain real snapshot object.</li>
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
public interface BlockSnapshotHolder extends Wrapper {
    /**
     * Converts a platform-specific type to a new {@link BlockSnapshotHolder}. This method does not accept null.
     *
     * @param blockSnapshot a placed block represented by a platform-specific type
     * @return new {@link BlockSnapshotHolder}
     * @throws IllegalArgumentException if the platform block snapshot object is not convertable or null has been passed
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @NotNull BlockSnapshotHolder of(@NotNull Object blockSnapshot) {
        var result = ofNullable(blockSnapshot);
        if (result == null) {
            throw new IllegalArgumentException("Could not wrap block snapshot: " + blockSnapshot);
        }
        return result;
    }

    /**
     * Converts a platform-specific type to a new {@link BlockSnapshotHolder}.
     *
     * @param blockSnapshot a block snapshot represented by a platform-specific type or null
     * @return new {@link BlockSnapshotHolder} or null if the block snapshot has not been passed
     * @throws IllegalArgumentException if the platform block snapshot object is not convertable
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @Nullable BlockSnapshotHolder ofNullable(@Nullable Object blockSnapshot) {
        if (Provider.provider == null) {
            throw new UnsupportedOperationException("A provider for " + BlockSnapshotHolder.class.getSimpleName() + " has not been registered yet!");
        }
        if (blockSnapshot == null) {
            return null;
        }
        return Provider.provider.apply(blockSnapshot);
    }

    @ApiStatus.Internal
    final class Provider {
        private static @Nullable Function<@NotNull Object, @Nullable BlockSnapshotHolder> provider;

        private Provider() {
        }

        public static void registerProvider(@NotNull Function<@NotNull Object, @Nullable BlockSnapshotHolder> provider) {
            if (Provider.provider != null) {
                throw new UnsupportedOperationException("A provider for " + BlockSnapshotHolder.class.getSimpleName() + " has already been registered!");
            }
            Provider.provider = provider;
        }
    }
}
