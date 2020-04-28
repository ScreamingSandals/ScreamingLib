package org.screamingsandals.lib.gamecore.events.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerUnregisteredEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    private final UUID uuid;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}