package org.screamingsandals.lib.sponge.material.container;

import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Wrapper;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpongeContainer extends BasicWrapper<Inventory> implements Container {
    public SpongeContainer(Inventory wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Optional<Item> getItem(int index) {
        return wrappedObject.peekAt(index).flatMap(ItemFactory::build);
    }

    @Override
    public void setItem(int index, Item item) {
        wrappedObject.set(index, item.as(ItemStack.class));
    }

    @Override
    public List<Item> addItem(Item... items) {
        return wrappedObject.offer(Arrays.stream(items).map(item -> item.as(ItemStack.class)).toArray(ItemStack[]::new))
                .getRejectedItems()
                .stream()
                .map(ItemStackSnapshot::createStack)
                .map(ItemFactory::build)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private int first(ItemStack item) {
        if (item == null) {
            return -1;
        }
        item = item.copy();
        item.setQuantity(1);
        var itemArray = new ItemStack[wrappedObject.capacity()];
        for (var i = 0; i < itemArray.length; i++) {
            itemArray[i] = wrappedObject.peekAt(i).map(itemStack -> {
                var nstack = itemStack.copy();
                nstack.setQuantity(1);
                return nstack;
            }).orElse(null);
        }
        for (int i = 0; i < itemArray.length; i++) {
            if (itemArray[i] == null) continue;

            if (item.equalTo(itemArray[i])) {
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
            var toDelete = stack.getQuantity();

            while (toDelete > 0) {
                var first = first(stack);

                if (first == -1) {
                    item.setAmount(toDelete);
                    list.add(item);
                    break;
                } else {
                    var itemStack = wrappedObject.peekAt(first).get();
                    int amount = itemStack.getQuantity();

                    if (amount <= toDelete) {
                        toDelete -= amount;
                        wrappedObject.set(first, ItemStack.empty());
                    } else {
                        itemStack.setQuantity((byte) (amount - toDelete));
                        wrappedObject.set(first, itemStack);
                        toDelete = 0;
                    }
                }
            }
        });
        return list;
    }

    @Override
    public Item[] getContents() {
        var itemArray = new Item[wrappedObject.capacity()];
        for (var i = 0; i < itemArray.length; i++) {
            itemArray[i] = wrappedObject.peekAt(i).flatMap(ItemFactory::build).orElse(null);
        }
        return itemArray;
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
        return wrappedObject.contains(materialHolder.as(ItemType.class));
    }

    @Override
    public boolean contains(Item item) {
        return wrappedObject.contains(item.as(ItemStack.class));
    }

    @Override
    public boolean containsAtLeast(Item item, int amount) {
        return wrappedObject.contains(item.as(ItemStack.class));
    }

    @Override
    public int getSize() {
        return wrappedObject.capacity();
    }

    @Override
    public boolean isEmpty() {
        return wrappedObject.freeCapacity() == wrappedObject.capacity();
    }

    @Override
    public void openInventory(Wrapper wrapper) {
        wrapper.asOptional(ServerPlayer.class).ifPresent(player ->
            player.openInventory(wrappedObject)
        );
    }
}
