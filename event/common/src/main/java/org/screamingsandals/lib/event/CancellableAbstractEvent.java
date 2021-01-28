package org.screamingsandals.lib.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract event extending {@link AbstractEvent} that can be cancelled
 */
@EqualsAndHashCode(callSuper = false)
@Data
public abstract class CancellableAbstractEvent extends AbstractEvent implements Cancellable {
    /**
     * Indicates if the event was cancelled or not
     */
    private boolean cancelled;
}
