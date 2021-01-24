package org.screamingsandals.lib.utils.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public abstract class CancellableAbstractEvent extends AbstractEvent implements Cancellable {
    private boolean cancelled;
}
