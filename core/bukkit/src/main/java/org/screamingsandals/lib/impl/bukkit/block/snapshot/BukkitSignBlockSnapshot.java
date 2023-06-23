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

package org.screamingsandals.lib.impl.bukkit.block.snapshot;

import org.bukkit.block.Sign;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.impl.bukkit.BukkitCore;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.block.snapshot.SignBlockSnapshot;

import java.util.Arrays;

public class BukkitSignBlockSnapshot extends BukkitBlockEntitySnapshot implements SignBlockSnapshot {
    protected BukkitSignBlockSnapshot(@NotNull Sign wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull Component @NotNull [] frontLines() {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return ((Sign) wrappedObject).lines().stream().map(AdventureBackend::wrapComponent).toArray(Component[]::new);
        } else {
            return Arrays.stream(((Sign) wrappedObject).getLines()).map(Component::fromLegacy).toArray(Component[]::new);
        }
    }

    @Override
    public @NotNull Component frontLine(@Range(from = 0, to = 3) int index) {
        return frontLines()[index];
    }

    @Override
    public void frontLine(@Range(from = 0, to = 3) int index, Component component) {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            ((Sign) wrappedObject).line(index, component.as(net.kyori.adventure.text.Component.class));
        } else {
            ((Sign) wrappedObject).setLine(index, component.toLegacy());
        }
    }

    @Override
    public boolean waxed() {
        if (BukkitFeature.SIGN_IS_WAXED.isSupported()) {
            return ((Sign) wrappedObject).isWaxed();
        } else {
            return !((Sign) wrappedObject).isEditable();
        }
    }

    @Override
    public void waxed(boolean waxed) {
        if (BukkitFeature.SIGN_IS_WAXED.isSupported()) {
            ((Sign) wrappedObject).setWaxed(waxed);
        } else {
            ((Sign) wrappedObject).setEditable(!waxed);
        }
    }

    @Override
    public boolean frontSideGlowing() {
        if (BukkitFeature.SIGN_IS_GLOWING_TEXT.isSupported()) {
            return ((Sign) wrappedObject).isGlowingText();
        } else {
            return false; // else: not supported on < 1.17
        }
    }

    @Override
    public void frontSideGlowing(boolean glowing) {
        if (BukkitFeature.SIGN_IS_GLOWING_TEXT.isSupported()) {
            ((Sign) wrappedObject).setGlowingText(glowing);
        } // else: not supported on < 1.17
    }
}
