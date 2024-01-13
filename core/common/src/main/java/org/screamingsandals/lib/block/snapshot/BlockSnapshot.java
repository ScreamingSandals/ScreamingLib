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

package org.screamingsandals.lib.block.snapshot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.container.ContainerHolder;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.world.Location;

// TODO: Metadata
public interface BlockSnapshot extends Wrapper, ContainerHolder {

    @NotNull Block block();

    void block(@NotNull Block type);

    @Nullable Location location();

    @Nullable BlockPlacement blockPlacement();

    default boolean updateBlock() {
        return updateBlock(false);
    }

    default boolean updateBlock(boolean force) {
        return updateBlock(force, true);
    }

    boolean updateBlock(boolean force, boolean applyPhysics);
}
