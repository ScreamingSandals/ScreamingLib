package org.screamingsandals.lib.event.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.entity.SEntityDeathEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

public interface SPlayerDeathEvent extends SEntityDeathEvent, SPlayerEvent {

    Component getDeathMessage();

    void setDeathMessage(Component deathMessage);

    void setDeathMessage(ComponentLike deathMessage);

    boolean isKeepInventory();

    void setKeepInventory(boolean keepInventory);

    boolean isShouldDropExperience();

    void setShouldDropExperience(boolean shouldDropExperience);

    boolean isKeepLevel();

    void setKeepLevel(boolean keepLevel);

    int getNewLevel();

    void setNewLevel(int newLevel);

    int getNewTotalExp();

    void setNewTotalExp(int newTotalExp);

    int getNewExp();

    void setNewExp(int newExp);

    int getDroppedExp();

    void setDroppedExp(int droppedExp);

    @Nullable
    PlayerWrapper getKiller();
}
