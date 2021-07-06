package org.screamingsandals.lib.event.world;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.annotations.LimitedVersionSupport;
import org.screamingsandals.lib.world.WorldHolder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@LimitedVersionSupport("Bukkit >= 1.15.1")
public class STimeSkipEvent extends CancellableAbstractEvent {
    private final WorldHolder world;
    private final Reason reason;
    private long skipAmount;

    public enum Reason {
        COMMAND,
        CUSTOM,
        NIGHT_SKIP
    }
}
