package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerItemDamageEvent extends CancellableAbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<Item> item;
    private final ObjectLink<Integer> damage;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public Item getItem() {
        return item.get();
    }

    public int getDamage() {
        return damage.get();
    }

    public void setDamage(int damage) {
        this.damage.set(damage);
    }
}
