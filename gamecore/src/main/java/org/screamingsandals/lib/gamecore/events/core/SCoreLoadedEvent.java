package org.screamingsandals.lib.gamecore.events.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.events.BaseEvent;

/**
 * Fired when game-core instance is loaded successfully
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SCoreLoadedEvent extends BaseEvent {
    private final GameCore gameCore;
}
