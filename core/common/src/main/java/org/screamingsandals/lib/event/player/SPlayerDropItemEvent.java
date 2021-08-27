package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerDropItemEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<EntityItem> itemDrop;

    public SPlayerDropItemEvent(ImmutableObjectLink<PlayerWrapper> player,
                                ImmutableObjectLink<EntityItem> itemDrop) {
        super(player);
        this.itemDrop = itemDrop;
    }

    public EntityItem getItemDrop() {
        return itemDrop.get();
    }
}
