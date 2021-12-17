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
    public Component getDeathMessage() {
        return ComponentObjectLink.processGetter(getEvent(), "deathMessage", getEvent()::getDeathMessage);
    }

    @Override
    public void setDeathMessage(Component deathMessage) {
        ComponentObjectLink.processSetter(getEvent(), "deathMessage", getEvent()::setDeathMessage, deathMessage);
    }

    @Override
    public void setDeathMessage(ComponentLike deathMessage) {
        if (deathMessage instanceof SenderMessage) {
            setDeathMessage(((SenderMessage) deathMessage).asComponent(getPlayer()));
        } else {
            setDeathMessage(deathMessage != null ? deathMessage.asComponent() : null);
        }
    }

    @Override
    public boolean isKeepInventory() {
        return getEvent().getKeepInventory();
    }

    @Override
    public void setKeepInventory(boolean keepInventory) {
        getEvent().setKeepInventory(keepInventory);
    }

    @Override
    public boolean isShouldDropExperience() {
        return getEvent().shouldDropExperience();
    }

    @Override
    public void setShouldDropExperience(boolean shouldDropExperience) {
        getEvent().setShouldDropExperience(shouldDropExperience);
    }

    @Override
    public boolean isKeepLevel() {
        return getEvent().getKeepLevel();
    }

    @Override
    public void setKeepLevel(boolean keepLevel) {
        getEvent().setKeepLevel(keepLevel);
    }

    @Override
    public int getNewLevel() {
        return getEvent().getNewLevel();
    }

    @Override
    public void setNewLevel(int newLevel) {
        getEvent().setNewLevel(newLevel);
    }

    @Override
    public int getNewTotalExp() {
        return getEvent().getNewTotalExp();
    }

    @Override
    public void setNewTotalExp(int newTotalExp) {
        getEvent().setNewTotalExp(newTotalExp);
    }

    @Override
    public int getNewExp() {
        return getEvent().getNewExp();
    }

    @Override
    public void setNewExp(int newExp) {
        getEvent().setNewExp(newExp);
    }

    @Override
    public int getDroppedExp() {
        return getEvent().getDroppedExp();
    }

    @Override
    public void setDroppedExp(int droppedExp) {
        getEvent().setDroppedExp(droppedExp);
    }

    @Override
    @Nullable
    public PlayerWrapper getKiller() {
        if (bukkitKiller != null && killer == null) {
            killer = new BukkitEntityPlayer(bukkitKiller);
        }
        return killer;
    }

    @Override
    public PlayerWrapper getPlayer() {
        return (PlayerWrapper) getEntity();
    }

    @Override
    public PlayerDeathEvent getEvent() {
        return (PlayerDeathEvent) super.getEvent();
    }
}
