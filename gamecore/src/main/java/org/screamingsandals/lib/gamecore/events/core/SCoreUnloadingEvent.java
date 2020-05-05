package org.screamingsandals.lib.gamecore.events.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.events.BaseEvent;

@EqualsAndHashCode(callSuper = false)
@Data
public class SCoreUnloadingEvent extends BaseEvent {
    private final GameCore gameCore;
}
