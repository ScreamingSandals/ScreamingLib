package org.screamingsandals.lib.visuals.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.event.SCancellableAsyncEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.InteractType;
import org.screamingsandals.lib.visuals.Visual;

@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@Data
public class VisualsTouchEvent<T extends Visual<T>> implements SCancellableAsyncEvent {
    private final PlayerWrapper player;
    private final T visual;
    private final InteractType interactType;
    private boolean cancelled;
}

