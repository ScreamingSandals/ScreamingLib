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

package org.screamingsandals.lib.impl.bukkit.event.block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlockPlacement;
import org.screamingsandals.lib.event.block.BlockSpreadEvent;

public class BukkitBlockSpreadEvent extends BukkitBlockFormEvent implements BlockSpreadEvent {
    // Internal cache
    private @Nullable BlockPlacement source;

    public BukkitBlockSpreadEvent(@NotNull org.bukkit.event.block.BlockSpreadEvent event) {
        super(event);
    }

    @Override
    public @NotNull BlockPlacement source() {
        if (source == null) {
            source = new BukkitBlockPlacement(((org.bukkit.event.block.BlockSpreadEvent) event()).getSource());
        }
        return source;
    }
}
