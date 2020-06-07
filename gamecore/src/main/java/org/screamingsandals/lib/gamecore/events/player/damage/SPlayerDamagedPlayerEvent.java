package org.screamingsandals.lib.gamecore.events.player.damage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageEvent;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.events.BaseEvent;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerDamagedPlayerEvent extends BaseEvent implements Cancellable {
    private final GamePlayer attacker;
    private final GamePlayer damaged;
    private final GameFrame gameFrame;
    private final EntityDamageEvent.DamageCause damageCause;
    private boolean cancelled;
}
