package org.screamingsandals.lib.utils.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@RequiredArgsConstructor
public class HandlerUnregisteredEvent extends AbstractEvent {
    private final EventManager eventManager;
    private final Class<?> eventClass;
    private final EventHandler<?> handler;
}
