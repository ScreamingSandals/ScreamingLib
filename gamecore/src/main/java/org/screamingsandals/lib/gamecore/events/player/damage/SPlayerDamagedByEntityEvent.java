package org.screamingsandals.lib.gamecore.events.player.damage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Cancellable;
import org.screamingsandals.lib.gamecore.events.BaseEvent;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerDamagedByEntityEvent extends BaseEvent implements Cancellable {
    private boolean cancelled;
}
