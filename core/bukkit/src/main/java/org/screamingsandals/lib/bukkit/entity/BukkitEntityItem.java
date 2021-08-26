package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.builder.ItemFactory;

import java.util.concurrent.TimeUnit;

public class BukkitEntityItem extends BukkitEntityBasic implements EntityItem {

    protected BukkitEntityItem(org.bukkit.entity.Item wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Item getItem() {
        return ItemFactory.build(getWrappedObject().getItemStack()).orElse(null);
    }

    @Override
    public void setItem(Item stack) {
        getWrappedObject().setItemStack(ItemFactory.convertItem(stack, ItemStack.class));
    }

    @Override
    public int getPickupDelay() {
        return getWrappedObject().getPickupDelay();
    }

    @Override
    public void setPickupDelay(int delay, TimeUnit timeUnit) {
        switch (timeUnit) {
            case SECONDS:
                getWrappedObject().setPickupDelay(delay * 20);
                break;
            case MINUTES:
                getWrappedObject().setPickupDelay(delay * 1200);
                break;
            case HOURS:
                getWrappedObject().setPickupDelay(delay * 72000);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported TimeUnit " + timeUnit.name() + "!");
        }
    }

    @Override
    public boolean isPickable() {
        //TODO: think about this
        return getWrappedObject().canPlayerPickup();
    }

    @Override
    public void setPickable(boolean pickable) {
        //TODO: think about this
        getWrappedObject().setCanPlayerPickup(false);
    }

    @Override
    public boolean isMergeable() {
        //TODO
        return true;
    }

    @Override
    public void setMergeable(boolean mergeable) {
        //TODO
    }

    @Override
    public float getMergeRange() {
        //TODO
        return 0;
    }

    @Override
    public void setMergeRange(float mergeRange) {
        //TODO
    }

    @Override
    public long getSpawnTime() {
        return getWrappedObject().getTicksLived();
    }

    public org.bukkit.entity.Item getWrappedObject() {
        return (org.bukkit.entity.Item) wrappedObject;
    }
}
