package org.screamingsandals.lib.minestom.material.container;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.minestom.material.builder.MinestomItemFactory;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.List;
import java.util.Optional;

// TODO
public class MinestomContainer extends BasicWrapper<Inventory> implements Container {

    protected MinestomContainer(Inventory wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Optional<Item> getItem(int index) {
        return MinestomItemFactory.build(wrappedObject.getItemStack(index));
    }

    @Override
    public void setItem(int index, Item item) {
        wrappedObject.setItemStack(index, item.as(ItemStack.class));
    }

    @Override
    public List<Item> addItem(Item... items) {
        return null;
    }

    @Override
    public List<Item> removeItem(Item... items) {
        return null;
    }

    @Override
    public Item[] getContents() {
        return new Item[0];
    }

    @Override
    public void setContents(Item[] items) throws IllegalArgumentException {

    }

    @Override
    public boolean contains(MaterialHolder materialHolder) {
        return false;
    }

    @Override
    public boolean contains(Item item) {
        return false;
    }

    @Override
    public boolean containsAtLeast(Item item, int amount) {
        return false;
    }

    @Override
    public int getSize() {
        return wrappedObject.getSize();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void openInventory(Wrapper wrapper) {
        wrapper.asOptional(Player.class).ifPresent(player ->
                player.openInventory(wrappedObject)
        );
    }
}
