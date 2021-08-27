package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = false)
public class SPlayerBedLeaveEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<BlockHolder> bed;
    private final ObjectLink<Boolean> bedSpawn;

    public SPlayerBedLeaveEvent(ImmutableObjectLink<PlayerWrapper> player,
                               ImmutableObjectLink<BlockHolder> bed,
                               ObjectLink<Boolean> bedSpawn) {
        super(player);
        this.bed = bed;
        this.bedSpawn = bedSpawn;
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
