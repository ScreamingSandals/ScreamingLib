package org.screamingsandals.lib.minestom.item.builder;

import net.minestom.server.item.ItemStack;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemView;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.minestom.item.MinestomItem;
import org.screamingsandals.lib.minestom.item.MinestomItemView;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class MinestomItemFactory extends ItemFactory {
    public MinestomItemFactory() {
        itemConverter
                .registerW2P(ItemStack.class, item -> (ItemStack) item.raw())
                .registerP2W(ItemStack.class, MinestomItem::new);
    }

    @Override
    protected ItemBuilder builder0() {
        return new MinestomItemBuilder();
    }

    @Override
    protected ItemView asView0(Item item) {
        return new MinestomItemView(item.as(ItemStack.class));
    }
}
