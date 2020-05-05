package org.screamingsandals.lib.gamecore.events.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.screamingsandals.lib.gamecore.events.BaseEvent;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerPreRegisterEvent extends BaseEvent implements Cancellable {
    private final Player player;
    private boolean cancelled;
}
