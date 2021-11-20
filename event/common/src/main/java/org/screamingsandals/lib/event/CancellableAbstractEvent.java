package org.screamingsandals.lib.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract event extending {@link AbstractEvent} that can be cancelled
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Deprecated
public abstract class CancellableAbstractEvent extends AbstractEvent implements SCancellableEvent {
    /**
     * Indicates if the event was cancelled or not
     */
    private boolean cancelled;
}
