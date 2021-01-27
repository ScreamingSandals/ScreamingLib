package org.screamingsandals.lib.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public abstract class CancellableAbstractAsyncEvent extends AbstractAsyncEvent implements Cancellable {
    private boolean cancelled;
}
