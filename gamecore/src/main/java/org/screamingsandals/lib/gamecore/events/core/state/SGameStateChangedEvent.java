package org.screamingsandals.lib.gamecore.events.core.state;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameState;

@EqualsAndHashCode(callSuper = false)
@Data
public class SGameStateChangedEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    private final GameFrame gameFrame;
    private final GameState activeState;
    private final GameState previousState;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}