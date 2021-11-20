package org.screamingsandals.lib.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Deprecated
public abstract class CancellableAbstractAsyncEvent extends AbstractAsyncEvent implements SCancellableAsyncEvent {
    private boolean cancelled;
}
