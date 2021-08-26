package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class SPlayerShearEntityEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<EntityBasic> what;
    private final ImmutableObjectLink<Item> item;
    private final ImmutableObjectLink<EquipmentSlotHolder> hand;

    public PlayerWrapper getPlayer() {
        return player.get();
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
