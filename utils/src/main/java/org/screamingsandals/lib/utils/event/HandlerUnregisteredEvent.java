package org.screamingsandals.lib.utils.event;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class HandlerUnregisteredEvent {
    private final EventManager eventManager;
    private final Class<?> eventClass;
    private final EventHandler<?> handler;
}
