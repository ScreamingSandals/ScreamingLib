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
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

public interface SignBlockSnapshot extends BlockEntitySnapshot {
    @NotNull Component @NotNull [] frontLines();

    @NotNull Component frontLine(@Range(from = 0, to = 3) int index);

    void frontLine(@Range(from = 0, to = 3) int index, @NotNull Component component);

    default void frontLine(@Range(from = 0, to = 3) int index, @NotNull ComponentLike component) {
        frontLine(index, component.asComponent());
    }

    @LimitedVersionSupport(">= 1.20, unknown behaviour on older Bukkit versions")
    boolean waxed();

    @LimitedVersionSupport(">= 1.20, unknown behaviour on older Bukkit versions")
    void waxed(boolean waxed);

    @LimitedVersionSupport(">= 1.17")
    boolean frontSideGlowing();

    @LimitedVersionSupport(">= 1.17")
    void frontSideGlowing(boolean glowing);
}
