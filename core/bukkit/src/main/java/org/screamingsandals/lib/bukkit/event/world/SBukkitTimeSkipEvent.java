package org.screamingsandals.lib.bukkit.event.world;

import lombok.Data;
import org.bukkit.event.world.TimeSkipEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.world.STimeSkipEvent;
import org.screamingsandals.lib.world.WorldHolder;

@Data
public class SBukkitTimeSkipEvent implements STimeSkipEvent, BukkitCancellable {
    private final TimeSkipEvent event;

    // Internal cache
    private WorldHolder world;
    private Reason reason;

    @Override
    public WorldHolder getWorld() {
        if (world == null) {
            world = new BukkitWorldHolder(event.getWorld());
        }
        return world;
    }

    @Override
    public Reason getReason() {
        if (reason == null) {
            reason = Reason.valueOf(event.getSkipReason().name());
        }
        return reason;
    }

    @Override
    public long getSkipAmount() {
        return event.getSkipAmount();
    }

    @Override
    public void setSkipAmount(long skipAmount) {
        event.setSkipAmount(skipAmount);
    }
}
