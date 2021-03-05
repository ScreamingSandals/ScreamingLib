package org.screamingsandals.lib.player.event;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = false)
public class SPlayerDamageByEntityEvent extends SPlayerDamageEvent {
    private final EntityBasic damager;

    public SPlayerDamageByEntityEvent(@NotNull final EntityBasic damager,
                                      PlayerWrapper player, DamageCause cause,
                                      final double damage) {
        super(player, cause, damage);
        this.damager = damager;
    }
}
