package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerLevelChangeEvent extends SPlayerEvent {
    private final ImmutableObjectLink<Integer> oldLevel;
    private final ImmutableObjectLink<Integer> newLevel;

    public SPlayerLevelChangeEvent(ImmutableObjectLink<PlayerWrapper> player,
                                   ImmutableObjectLink<Integer> oldLevel,
                                   ImmutableObjectLink<Integer> newLevel) {
        super(player);
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public int getOldLevel() {
        return oldLevel.get();
    }

    public int getNewLevel() {
        return newLevel.get();
    }
}
