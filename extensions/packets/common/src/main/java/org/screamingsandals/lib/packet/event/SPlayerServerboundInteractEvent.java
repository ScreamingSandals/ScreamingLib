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

package org.screamingsandals.lib.packet.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.event.CancellableAsyncEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.utils.InteractType;

@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@Data
public class SPlayerServerboundInteractEvent implements CancellableAsyncEvent {
    private final @NotNull Player player;
    private final int entityId;
    private final @NotNull InteractType interactType;
    private boolean cancelled;

    @Override
    public boolean cancelled() {
        return cancelled;
    }

    @Override
    public void cancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
