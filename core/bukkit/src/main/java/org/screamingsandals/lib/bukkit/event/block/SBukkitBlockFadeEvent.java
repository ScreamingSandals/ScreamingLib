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

package org.screamingsandals.lib.bukkit.event.block;

import lombok.*;
import lombok.experimental.Accessors;

import lombok.experimental.ExtensionMethod;
import org.bukkit.event.block.BlockFadeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.block.state.BlockStateMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.block.SBlockFadeEvent;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class SBukkitBlockFadeEvent implements SBlockFadeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull BlockFadeEvent event;

    // Internal cache
    private @Nullable BlockHolder block;
    private @Nullable BlockStateHolder newBlockState;

    @Override
    public @NotNull BlockHolder block() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public @NotNull BlockStateHolder newBlockState() {
        if (newBlockState == null) {
            newBlockState = BlockStateMapper.<BlockStateHolder>wrapBlockState(event.getNewState()).orElseThrow();
        }
        return newBlockState;
    }
}
