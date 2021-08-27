package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerExpChangeEvent extends SPlayerEvent {
    private final ObjectLink<Integer> exp;

    public SPlayerExpChangeEvent(ImmutableObjectLink<PlayerWrapper> player,
                                 ObjectLink<Integer> exp) {
        super(player);
        this.exp = exp;
    }

    public int getExp() {
        return exp.get();
    }

    public void setExp(int exp) {
        this.exp.set(exp);
    }
}
