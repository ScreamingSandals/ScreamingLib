package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerShearEntityEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<EntityBasic> what;
    private final ImmutableObjectLink<Item> item;
    private final ImmutableObjectLink<EquipmentSlotHolder> hand;

    SPlayerShearEntityEvent(ImmutableObjectLink<PlayerWrapper> player,
                            ImmutableObjectLink<EntityBasic> what,
                            ImmutableObjectLink<Item> item,
                            ImmutableObjectLink<EquipmentSlotHolder> hand) {
        super(player);
        this.what = what;
        this.item = item;
        this.hand = hand;
    }

    public EntityBasic getWhat() {
        return what.get();
    }

    public Item getItem() {
        return item.get();
    }

    public EquipmentSlotHolder getHand() {
        return hand.get();
    }
}
