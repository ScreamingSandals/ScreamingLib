package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.block.BlockHolder;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerBedLeaveEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<BlockHolder> bed;
    private final ObjectLink<Boolean> bedSpawn;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public BlockHolder getBed() {
        return bed.get();
    }

    public boolean isBedSpawn() {
        return bedSpawn.get();
    }

    public void setBedSpawn(boolean bedSpawn) {
        this.bedSpawn.set(bedSpawn);
    }
}
