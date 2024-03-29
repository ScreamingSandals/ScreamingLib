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

package org.screamingsandals.lib.impl.block.snapshot;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.api.types.server.BlockSnapshotHolder;
import org.screamingsandals.lib.block.snapshot.BlockSnapshot;
import org.screamingsandals.lib.utils.annotations.ProvidedService;

import java.util.Objects;

@ProvidedService
public abstract class BlockSnapshots {

    static {
        BlockSnapshotHolder.Provider.registerProvider(o ->
                Objects.requireNonNull(wrapBlockSnapshot(o), "Cannot wrap " + o + " to BlockSnapshot")
        );
    }

    private static @Nullable BlockSnapshots blockSnapshots;

    @ApiStatus.Internal
    public BlockSnapshots() {
        if (blockSnapshots != null) {
            throw new UnsupportedOperationException("BlockSnapshots is already initialized.");
        }

        blockSnapshots = this;
    }

    @SuppressWarnings("unchecked")
    @Contract("null -> null")
    public static <T extends BlockSnapshot> @Nullable T wrapBlockSnapshot(@Nullable Object blockState) {
        if (blockSnapshots == null) {
            throw new UnsupportedOperationException("BlockSnapshots is not initialized yet.");
        }
        if (blockState == null) {
            return null;
        }
        if (blockState instanceof BlockSnapshot) {
            return (T) blockState;
        }
        return blockSnapshots.wrapBlockSnapshot0(blockState);
    }

    protected abstract <T extends BlockSnapshot> @Nullable T wrapBlockSnapshot0(@Nullable Object blockState);
}
