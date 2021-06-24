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

import java.util.*;

public class MinestomContainer extends BasicWrapper<Inventory> implements Container {

    public MinestomContainer(Inventory wrappedObject) {
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
        var list = new ArrayList<Item>();
        Arrays.stream(items).forEach(item -> {
            if (!wrappedObject.addItemStack(item.as(ItemStack.class))) {
                list.add(item);
            }
        });
        return list;
    }

    private int first(ItemStack item) {
        if (item == null) {
            return -1;
        }
        ItemStack[] inventory = wrappedObject.getItemStacks();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) continue;

            if (item.isSimilar(inventory[i])) {
                return i;
            }
        }
        return -1;

    }

    @Override
    public List<Item> removeItem(Item... items) {
        var list = new ArrayList<Item>();
        Arrays.stream(items).forEach(item -> {
            var stack = item.as(ItemStack.class);
            var toDelete = stack.getAmount();

            while (toDelete > 0) {
                var first = first(stack);

                if (first == -1) {
                    item.setAmount(toDelete);
                    list.add(item);
                    break;
                } else {
                    var itemStack = wrappedObject.getItemStack(first);
                    int amount = itemStack.getAmount();

                    if (amount <= toDelete) {
                        toDelete -= amount;
                        wrappedObject.setItemStack(first, ItemStack.getAirItem());
                    } else {
                        itemStack.setAmount((byte) (amount - toDelete));
                        wrappedObject.setItemStack(first, itemStack);
                        toDelete = 0;
                    }
                }
            }
        });
        return list;
    }

    @Override
    public Item[] getContents() {
        return Arrays.stream(wrappedObject.getItemStacks()).map(MinestomItemFactory::build).map(item -> item.orElse(null)).toArray(Item[]::new);
    }

    @Override
    public void setContents(Item[] items) throws IllegalArgumentException {
        if (items.length != getSize()) {
            throw new IllegalArgumentException("Wrong size of items array. Must be " + getSize());
        }
        for (var i = 0; i < items.length; i++) {
            setItem(i, items[i]);
        }
    }

    @Override
    public boolean contains(MaterialHolder materialHolder) {
        return Arrays.stream(getContents()).filter(Objects::nonNull).anyMatch(item -> item.getMaterial().equals(materialHolder));
    }

    @Override
    public boolean contains(Item item) {
        return Arrays.asList(getContents()).contains(item);
    }

    @Override
    public boolean containsAtLeast(Item item, int amount) {
        var amount2 = Arrays.stream(getContents()).filter(item::isSimilar).mapToInt(Item::getAmount).sum();
        return amount2 >= amount;
    }

    @Override
    public int getSize() {
        return wrappedObject.getSize();
    }

    @Override
    public boolean isEmpty() {
        return Arrays.stream(getContents()).allMatch(Objects::isNull);
    }

    @Override
    public void openInventory(Wrapper wrapper) {
        wrapper.asOptional(Player.class).ifPresent(player ->
                player.openInventory(wrappedObject)
        );
    }
}
