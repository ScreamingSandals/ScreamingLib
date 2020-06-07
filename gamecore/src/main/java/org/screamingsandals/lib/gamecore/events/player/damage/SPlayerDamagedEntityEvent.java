package org.screamingsandals.lib.gamecore.events.player.damage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.events.BaseEvent;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerDamagedEntityEvent extends BaseEvent {
    private final GamePlayer gamePlayer;
    private final Entity damaged;
    private final GameFrame gameFrame;
    private final EntityDamageEvent.DamageCause damageCause;
}