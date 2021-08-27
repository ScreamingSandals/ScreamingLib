package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerSwapHandItemsEvent extends SPlayerCancellableEvent {
    private final ObjectLink<Item> mainHandItem;
    private final ObjectLink<Item> offHandItem;

    public SPlayerSwapHandItemsEvent(ImmutableObjectLink<PlayerWrapper> player,
                                     ObjectLink<Item> mainHandItem,
                                     ObjectLink<Item> offHandItem) {
        super(player);
        this.mainHandItem = mainHandItem;
        this.offHandItem = offHandItem;
    }

    public Item getMainHandItem() {
        return mainHandItem.get();
    }

    public void setMainHandItem(Item mainHandItem) {
        this.mainHandItem.set(mainHandItem);
    }

    public Item getOffHandItem() {
        return offHandItem.get();
    }

    public void setOffHandItem(Item offHandItem) {
        this.offHandItem.set(offHandItem);
    }
}
