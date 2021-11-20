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
public abstract class STimeSkipEvent extends CancellableAbstractEvent {

    public abstract WorldHolder getWorld();

    public abstract Reason getReason();

    public abstract long getSkipAmount();

    public abstract void setSkipAmount(long skipAmount);

    // TODO: holder?
    public enum Reason {
        COMMAND,
        CUSTOM,
        NIGHT_SKIP
    }
}
