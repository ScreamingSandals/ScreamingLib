package org.screamingsandals.lib.minestom.item;

import net.minestom.server.item.ItemStack;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemView;

public class MinestomItemView extends MinestomItem implements ItemView {
    public MinestomItemView(ItemStack wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public void changeAmount(int amount) {
        // empty stub
    }

    @Override
    public void replace(Item replaceable) {
        // empty stub
    }
}
