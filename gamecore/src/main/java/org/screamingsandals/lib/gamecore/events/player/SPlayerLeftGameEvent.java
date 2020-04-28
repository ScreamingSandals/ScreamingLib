package org.screamingsandals.lib.gamecore.events.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerLeftGameEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    private final GameFrame gameFrame;
    private final GamePlayer gamePlayer;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
