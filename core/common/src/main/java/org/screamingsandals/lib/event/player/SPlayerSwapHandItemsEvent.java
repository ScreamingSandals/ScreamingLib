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
public class SPlayerSwapHandItemsEvent extends CancellableAbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ObjectLink<Item> mainHandItem;
    private final ObjectLink<Item> offHandItem;

    public PlayerWrapper getPlayer() {
        return player.get();
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
