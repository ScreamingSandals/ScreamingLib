/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.bukkit.event.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.entity.SBukkitEntityDeathEvent;
import org.screamingsandals.lib.event.player.SPlayerDeathEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.SenderMessage;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;

public class SBukkitPlayerDeathEvent extends SBukkitEntityDeathEvent implements SPlayerDeathEvent {
    public SBukkitPlayerDeathEvent(PlayerDeathEvent event) {
        super(event);
        bukkitKiller = event.getEntity().getKiller();
    }

    // Internal cache
    @Nullable
    private final Player bukkitKiller;
    @Nullable
    private PlayerWrapper killer;

    @Override
    public Component deathMessage() {
        return ComponentObjectLink.processGetter(event(), "deathMessage", event()::getDeathMessage);
    }

    @Override
    public void deathMessage(Component deathMessage) {
        ComponentObjectLink.processSetter(event(), "deathMessage", event()::setDeathMessage, deathMessage);
    }

    @Override
    public void deathMessage(ComponentLike deathMessage) {
        if (deathMessage instanceof SenderMessage) {
            deathMessage(((SenderMessage) deathMessage).asComponent(player()));
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
    @Nullable
    public PlayerWrapper killer() {
        if (bukkitKiller != null && killer == null) {
            killer = new BukkitEntityPlayer(bukkitKiller);
        }
        return killer;
    }

    @Override
    public PlayerWrapper player() {
        return (PlayerWrapper) entity();
    }

    @Override
    public PlayerDeathEvent event() {
        return (PlayerDeathEvent) super.event();
    }
}
