package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerExpChangeEvent extends AbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ObjectLink<Integer> exp;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public int getExp() {
        return exp.get();
    }

    public void setExp(int exp) {
        this.exp.set(exp);
    }
}
