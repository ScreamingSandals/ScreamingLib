package org.screamingsandals.lib.sponge.material.container;

import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Wrapper;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.Inventory;

import java.util.List;
import java.util.Optional;

// TODO
public class SpongeContainer extends BasicWrapper<Inventory> implements Container {
    public SpongeContainer(Inventory wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Optional<Item> getItem(int index) {
        return Optional.empty();
    }

    @Override
    public void setItem(int index, Item item) {

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
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void openInventory(Wrapper wrapper) {
        wrapper.asOptional(ServerPlayer.class).ifPresent(player ->
            player.openInventory(wrappedObject)
        );
    }
}
