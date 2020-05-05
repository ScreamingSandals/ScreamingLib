package org.screamingsandals.lib.gamecore.events.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerTeleportEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    private final GamePlayer gamePlayer;
    private final Location destination;
    private final Location current;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
