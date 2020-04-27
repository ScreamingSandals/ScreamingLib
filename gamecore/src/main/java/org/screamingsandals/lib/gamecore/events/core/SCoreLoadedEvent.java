package org.screamingsandals.lib.gamecore.events.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.gamecore.GameCore;

/**
 * Fired when game-core instance is loaded successfully
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SCoreLoadedEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    private final GameCore gameCore;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
