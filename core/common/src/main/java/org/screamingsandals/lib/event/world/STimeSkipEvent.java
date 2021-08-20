package org.screamingsandals.lib.event.world;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.world.WorldHolder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@LimitedVersionSupport("Bukkit >= 1.15.1")
public class STimeSkipEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<WorldHolder> world;
    private final ImmutableObjectLink<Reason> reason;
    private final ObjectLink<Long> skipAmount;

    public WorldHolder getWorld() {
        return world.get();
    }

    public Reason getReason() {
        return reason.get();
    }

    public long getSkipAmount() {
        return skipAmount.get();
    }

    public void setSkipAmount(long skipAmount) {
        this.skipAmount.set(skipAmount);
    }

    // TODO: holder?
    public enum Reason {
        COMMAND,
        CUSTOM,
        NIGHT_SKIP
    }
}
