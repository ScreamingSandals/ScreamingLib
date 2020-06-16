package org.screamingsandals.lib.gamecore.events.player.move;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.events.BaseEvent;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Fired only when player is in game.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerMovedEvent extends BaseEvent implements Cancellable {
    private final GamePlayer gamePlayer;
    private final GameFrame gameFrame;
    private final Location from;
    private final Location to;
    private boolean cancelled;
}
