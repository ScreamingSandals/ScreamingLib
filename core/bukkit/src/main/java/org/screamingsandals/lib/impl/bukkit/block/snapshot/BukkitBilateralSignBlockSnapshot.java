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
import org.bukkit.block.sign.Side;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.block.snapshot.BilateralSignBlockSnapshot;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.impl.bukkit.BukkitCore;
import org.screamingsandals.lib.spectator.Component;

import java.util.Arrays;

public class BukkitBilateralSignBlockSnapshot extends BukkitSignBlockSnapshot implements BilateralSignBlockSnapshot {
    protected BukkitBilateralSignBlockSnapshot(@NotNull Sign wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull Component @NotNull [] frontLines() {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return ((Sign) wrappedObject).getSide(Side.FRONT).lines().stream().map(AdventureBackend::wrapComponent).toArray(Component[]::new);
        } else {
            return Arrays.stream(((Sign) wrappedObject).getSide(Side.FRONT).getLines()).map(Component::fromLegacy).toArray(Component[]::new);
        }
    }

    @Override
    public void frontLine(@Range(from = 0, to = 3) int index, Component component) {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            ((Sign) wrappedObject).getSide(Side.FRONT).line(index, component.as(net.kyori.adventure.text.Component.class));
        } else {
            ((Sign) wrappedObject).getSide(Side.FRONT).setLine(index, component.toLegacy());
        }
    }

    @Override
    public boolean frontSideGlowing() {
        return ((Sign) wrappedObject).getSide(Side.FRONT).isGlowingText();
    }

    @Override
    public void frontSideGlowing(boolean glowing) {
        ((Sign) wrappedObject).getSide(Side.FRONT).setGlowingText(glowing);
    }

    @Override
    public @NotNull Component @NotNull [] backLines() {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return ((Sign) wrappedObject).getSide(Side.BACK).lines().stream().map(AdventureBackend::wrapComponent).toArray(Component[]::new);
        } else {
            return Arrays.stream(((Sign) wrappedObject).getSide(Side.BACK).getLines()).map(Component::fromLegacy).toArray(Component[]::new);
        }
    }

    @Override
    public @NotNull Component backLine(@Range(from = 0, to = 3) int index) {
        return backLines()[index];
    }

    @Override
    public void backLine(@Range(from = 0, to = 3) int index, @NotNull Component component) {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            ((Sign) wrappedObject).getSide(Side.BACK).line(index, component.as(net.kyori.adventure.text.Component.class));
        } else {
            ((Sign) wrappedObject).getSide(Side.BACK).setLine(index, component.toLegacy());
        }
    }

    @Override
    public boolean backSideGlowing() {
        return ((Sign) wrappedObject).getSide(Side.BACK).isGlowingText();
    }

    @Override
    public void backSideGlowing(boolean glowing) {
        ((Sign) wrappedObject).getSide(Side.BACK).setGlowingText(glowing);
    }
}
