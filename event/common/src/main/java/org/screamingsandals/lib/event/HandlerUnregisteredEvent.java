package org.screamingsandals.lib.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * This event is called when any {@link EventHandler} is unregistered.
 */
@EqualsAndHashCode(callSuper = false)
@Data
@RequiredArgsConstructor
public class HandlerUnregisteredEvent extends AbstractEvent {
    private final EventManager eventManager;
    private final Class<?> eventClass;
    private final EventHandler<?> handler;
}
