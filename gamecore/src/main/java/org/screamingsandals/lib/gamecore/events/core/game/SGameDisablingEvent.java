package org.screamingsandals.lib.gamecore.events.core.game;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.gamecore.core.GameFrame;

@EqualsAndHashCode(callSuper = false)
@Data
public class SGameDisablingEvent extends Event implements Cancellable {
    private static final HandlerList handlerList = new HandlerList();
    private final GameFrame gameFrame;
    private boolean cancelled;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}