package org.screamingsandals.lib.visuals.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.event.CancellableAbstractAsyncEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.InteractType;
import org.screamingsandals.lib.visuals.Visual;

@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@Data
public class VisualsTouchEvent<T extends Visual<T>> extends CancellableAbstractAsyncEvent {
    private final PlayerWrapper player;
    private final T visual;
    private final InteractType interactType;
}

