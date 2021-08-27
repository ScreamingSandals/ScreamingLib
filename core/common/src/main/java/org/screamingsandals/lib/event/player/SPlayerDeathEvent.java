package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerDeathEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ObjectLink<Component> deathMessage;
    private final Collection<Item> drops;
    private final ObjectLink<Boolean> keepInventory;
    private final ObjectLink<Boolean> shouldDropExperience;
    private final ObjectLink<Boolean> keepLevel;
    private final ObjectLink<Integer> newLevel;
    private final ObjectLink<Integer> newTotalExp;
    private final ObjectLink<Integer> newExp;
    private final ObjectLink<Integer> droppedExp;
    private final ImmutableObjectLink<@Nullable PlayerWrapper> killer;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public Component getDeathMessage() {
        return deathMessage.get();
    }

    public void setDeathMessage(Component deathMessage) {
        this.deathMessage.set(deathMessage);
    }

    public boolean isKeepInventory() {
        return keepInventory.get();
    }

    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory.set(keepInventory);
    }

    public boolean isShouldDropExperience() {
        return shouldDropExperience.get();
    }

    public void setShouldDropExperience(boolean shouldDropExperience) {
        this.shouldDropExperience.set(shouldDropExperience);
    }

    public boolean isKeepLevel() {
        return keepLevel.get();
    }

    public void setKeepLevel(boolean keepLevel) {
        this.keepLevel.set(keepLevel);
    }

    public int getNewLevel() {
        return newLevel.get();
    }

    public void setNewLevel(int newLevel) {
        this.newLevel.set(newLevel);
    }

    public int getNewTotalExp() {
        return newTotalExp.get();
    }

    public void setNewTotalExp(int newTotalExp) {
        this.newTotalExp.set(newTotalExp);
    }

    public int getNewExp() {
        return newExp.get();
    }

    public void setNewExp(int newExp) {
        this.newExp.set(newExp);
    }

    public int getDroppedExp() {
        return droppedExp.get();
    }

    public void setDroppedExp(int droppedExp) {
        this.droppedExp.set(droppedExp);
    }

    @Nullable
    public PlayerWrapper getKiller() {
        return killer.get();
    }
}
