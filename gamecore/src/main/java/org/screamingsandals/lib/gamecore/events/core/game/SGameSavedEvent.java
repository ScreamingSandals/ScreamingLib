package org.screamingsandals.lib.gamecore.events.core.game;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.events.BaseEvent;

@EqualsAndHashCode(callSuper = false)
@Data
public class SGameSavedEvent extends BaseEvent {
    private final GameFrame gameFrame;
}