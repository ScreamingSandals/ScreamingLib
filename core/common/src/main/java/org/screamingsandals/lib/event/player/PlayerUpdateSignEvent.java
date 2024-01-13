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

package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.event.PlatformEvent;
import org.screamingsandals.lib.event.CancellableEvent;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

public interface PlayerUpdateSignEvent extends CancellableEvent, PlayerEvent, PlatformEvent {

    @NotNull BlockPlacement block();

    @NotNull Component @NotNull [] lines();

    @NotNull Component line(@Range(from = 0, to = 3) int index);

    void line(@Range(from = 0, to = 3) int index, @NotNull Component component);

    default void line(@Range(from = 0, to = 3) int index, @NotNull ComponentLike component) {
        line(index, component.asComponent());
    }
}
