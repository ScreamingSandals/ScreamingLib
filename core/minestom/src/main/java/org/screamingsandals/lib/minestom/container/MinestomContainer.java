package org.screamingsandals.lib.minestom.container;

import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.type.InventoryTypeHolder;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.minestom.item.builder.MinestomItemFactory;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.BasicWrapper;

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
                    list.add(item.withAmount(toDelete));
                    break;
                } else {
                    var itemStack = wrappedObject.getItemStack(first);
                    int amount = itemStack.getAmount();

                    if (amount <= toDelete) {
                        toDelete -= amount;
                        wrappedObject.setItemStack(first, ItemStack.AIR);
                    } else {
                        toDelete = 0;
                        wrappedObject.setItemStack(first, itemStack.withAmount(amount - toDelete));
                    }
                }
            }
        });
        return list;
    }

    @Override
    public @Nullable Item @NotNull [] getContents() {
        return Arrays.stream(wrappedObject.getItemStacks()).map(MinestomItemFactory::build).map(item -> item.orElse(null)).toArray(Item[]::new);
    }

    @Override
    public @Nullable Item @NotNull [] getStorageContents() {
        return getContents();
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
    public void setStorageContents(@Nullable Item @NotNull [] items) throws IllegalArgumentException {
        setContents(items);
    }

    @Override
    public boolean contains(ItemTypeHolder materialHolder) {
        return Arrays.stream(getContents()).filter(Objects::nonNull).anyMatch(e -> e.getType() == materialHolder);
    }

    @Override
    public boolean contains(Item item) {
        return Arrays.asList(getContents()).contains(item);
    }

    @Override
    public boolean containsAtLeast(Item item, int amount) {
        var amount2 = Arrays.stream(getContents()).filter(item::isSimilar).filter(Objects::nonNull).mapToInt(Item::getAmount).sum();
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
    public InventoryTypeHolder getType() {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public int firstEmptySlot() {
        return 0;
    }

    @Override
    public void openInventory(PlayerWrapper wrapper) {

    }
}
