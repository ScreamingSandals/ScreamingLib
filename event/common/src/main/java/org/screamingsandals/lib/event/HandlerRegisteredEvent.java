package org.screamingsandals.lib.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * This event is called when new {@link EventHandler} is registered.
 *
 * This event is asynchronous!
 */
@EqualsAndHashCode(callSuper = false)
@Data
@RequiredArgsConstructor
public class HandlerRegisteredEvent extends AbstractAsyncEvent {
    private final EventManager eventManager;
    private final Class<?> eventClass;
    private final EventHandler<?> handler;
}
