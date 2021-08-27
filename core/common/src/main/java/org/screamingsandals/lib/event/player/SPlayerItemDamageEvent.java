package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerItemDamageEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<Item> item;
    private final ObjectLink<Integer> damage;

    public SPlayerItemDamageEvent(ImmutableObjectLink<PlayerWrapper> player,
                                  ImmutableObjectLink<Item> item,
                                  ObjectLink<Integer> damage) {
        super(player);
        this.item = item;
        this.damage = damage;
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
