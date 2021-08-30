package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerLevelChangeEvent extends AbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<Integer> oldLevel;
    private final ImmutableObjectLink<Integer> newLevel;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public int getOldLevel() {
        return oldLevel.get();
    }

    public int getNewLevel() {
        return newLevel.get();
    }
}
