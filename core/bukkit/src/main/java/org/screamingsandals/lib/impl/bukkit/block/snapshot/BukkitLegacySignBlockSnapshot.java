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

package org.screamingsandals.lib.impl.bukkit.block.snapshot;

import org.bukkit.block.Sign;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.block.snapshot.SignBlockSnapshot;
import org.screamingsandals.lib.spectator.Component;

import java.util.Arrays;

public class BukkitLegacySignBlockSnapshot extends BukkitBlockSnapshot implements SignBlockSnapshot {
    protected BukkitLegacySignBlockSnapshot(@NotNull Sign wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull Component @NotNull [] frontLines() {
        return Arrays.stream(((Sign) wrappedObject).getLines()).map(Component::fromLegacy).toArray(Component[]::new);
    }

    @Override
    public @NotNull Component frontLine(@Range(from = 0, to = 3) int index) {
        return frontLines()[index];
    }

    @Override
    public void frontLine(@Range(from = 0, to = 3) int index, Component component) {
        ((Sign) wrappedObject).setLine(index, component.toLegacy());
    }

    @Override
    public boolean waxed() {
        return false;
    }

    @Override
    public void waxed(boolean waxed) {
    }

    @Override
    public boolean frontSideGlowing() {
        return false;
    }

    @Override
    public void frontSideGlowing(boolean glowing) {
    }
}
