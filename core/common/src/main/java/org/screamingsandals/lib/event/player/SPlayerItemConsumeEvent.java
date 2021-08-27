package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerItemConsumeEvent extends SPlayerCancellableEvent {
    private final ObjectLink<@Nullable Item> item;

    public SPlayerItemConsumeEvent(ImmutableObjectLink<PlayerWrapper> player,
                                   ObjectLink<@Nullable Item> item) {
        super(player);
        this.item = item;
    }

    @Nullable
    public Item getItem() {
        return item.get();
    }

    public void setItem(@Nullable Item item) {
        this.item.set(item);
    }
}
