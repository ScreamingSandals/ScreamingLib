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

package org.screamingsandals.lib.impl.bukkit.event.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.impl.bukkit.BukkitCore;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityDeathEvent;
import org.screamingsandals.lib.event.player.PlayerDeathEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

public class BukkitPlayerDeathEvent extends BukkitEntityDeathEvent implements PlayerDeathEvent {
    public BukkitPlayerDeathEvent(@NotNull org.bukkit.event.entity.PlayerDeathEvent event) {
        super(event);
        bukkitKiller = event.getEntity().getKiller();
    }

    // Internal cache
    private final @Nullable org.bukkit.entity.Player bukkitKiller;
    private @Nullable Player killer;

    @Override
    public @Nullable Component deathMessage() {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            var dm = event().deathMessage();
            return dm != null ? AdventureBackend.wrapComponent(dm) : null;
        } else {
            var m = event().getDeathMessage();
            return m != null ? Component.fromLegacy(m) : null;
        }
    }

    @Override
    public void deathMessage(@Nullable Component deathMessage) {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            event().deathMessage(deathMessage != null ? deathMessage.as(net.kyori.adventure.text.Component.class) : null);
        } else {
            event().setDeathMessage(deathMessage != null ? deathMessage.toLegacy() : null);
        }
    }

    @Override
    public void deathMessage(@Nullable ComponentLike deathMessage) {
        if (deathMessage instanceof AudienceComponentLike) {
            // TODO: there should be another logic, because this message can be seen by more players
            deathMessage(((AudienceComponentLike) deathMessage).asComponent(player()));
        } else {
            deathMessage(deathMessage != null ? deathMessage.asComponent() : null);
        }
    }

    @Override
    public boolean keepInventory() {
        return event().getKeepInventory();
    }

    @Override
    public void keepInventory(boolean keepInventory) {
        event().setKeepInventory(keepInventory);
    }

    @Override
    public boolean shouldDropExperience() {
        return event().shouldDropExperience();
    }

    @Override
    public void shouldDropExperience(boolean shouldDropExperience) {
        event().setShouldDropExperience(shouldDropExperience);
    }

    @Override
    public boolean keepLevel() {
        return event().getKeepLevel();
    }

    @Override
    public void keepLevel(boolean keepLevel) {
        event().setKeepLevel(keepLevel);
    }

    @Override
    public int newLevel() {
        return event().getNewLevel();
    }

    @Override
    public void newLevel(int newLevel) {
        event().setNewLevel(newLevel);
    }

    @Override
    public int newTotalExp() {
        return event().getNewTotalExp();
    }

    @Override
    public void newTotalExp(int newTotalExp) {
        event().setNewTotalExp(newTotalExp);
    }

    @Override
    public int getNewExp() {
        return event().getNewExp();
    }

    @Override
    public void newExp(int newExp) {
        event().setNewExp(newExp);
    }

    @Override
    public int droppedExp() {
        return event().getDroppedExp();
    }

    @Override
    public void droppedExp(int droppedExp) {
        event().setDroppedExp(droppedExp);
    }

    @Override
    public @Nullable Player killer() {
        if (bukkitKiller != null && killer == null) {
            killer = new BukkitPlayer(bukkitKiller);
        }
        return killer;
    }

    @Override
    public @NotNull Player player() {
        return (Player) entity();
    }

    @Override
    public @NotNull org.bukkit.event.entity.PlayerDeathEvent event() {
        return (org.bukkit.event.entity.PlayerDeathEvent) super.event();
    }
}
