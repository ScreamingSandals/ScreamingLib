package org.screamingsandals.lib.gamecore.events.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Location;
import org.screamingsandals.lib.gamecore.events.BaseEvent;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerTeleportEvent extends BaseEvent {
    private final GamePlayer gamePlayer;
    private final Location destination;
    private final Location current;
}
