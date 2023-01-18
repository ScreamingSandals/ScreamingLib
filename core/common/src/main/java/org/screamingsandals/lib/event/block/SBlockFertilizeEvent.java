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

package org.screamingsandals.lib.event.block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.event.world.SPlantGrowEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.block.BlockHolder;

import java.util.Collection;

/**
 * @see SPlantGrowEvent
 */

@LimitedVersionSupport("Bukkit >= 1.13")
public interface SBlockFertilizeEvent extends SCancellableEvent, PlatformEventWrapper {

    @Nullable PlayerWrapper player();

    @NotNull BlockHolder block();

    @NotNull Collection<@NotNull BlockStateHolder> changedBlockStates();
}
