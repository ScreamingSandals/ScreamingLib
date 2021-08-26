package org.screamingsandals.lib.minestom.entity;

import net.minestom.server.entity.ItemEntity;
import net.minestom.server.item.ItemStack;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.item.builder.ItemFactory;

import java.util.concurrent.TimeUnit;

public class MinestomEntityItem extends MinestomEntityBasic implements EntityItem {

    protected MinestomEntityItem(ItemEntity wrappedObject) {
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
        return (int) getWrappedObject().getPickupDelay();
    }

    @Override
    public void setPickupDelay(int delay, TimeUnit timeUnit) {
        getWrappedObject().setPickupDelay(delay, net.minestom.server.utils.time.TimeUnit.valueOf(timeUnit.name()));
    }

    @Override
    public boolean isPickable() {
        return getWrappedObject().isPickable();
    }

    @Override
    public void setPickable(boolean pickable) {
        getWrappedObject().setPickable(pickable);
    }

    @Override
    public boolean isMergeable() {
        return getWrappedObject().isMergeable();
    }

    @Override
    public void setMergeable(boolean mergeable) {
        getWrappedObject().setMergeable(mergeable);
    }

    @Override
    public float getMergeRange() {
        return getWrappedObject().getMergeRange();
    }

    @Override
    public void setMergeRange(float mergeRange) {
        getWrappedObject().setMergeRange(mergeRange);
    }

    @Override
    public long getSpawnTime() {
        return getWrappedObject().getSpawnTime();
    }

    private ItemEntity getWrappedObject() {
        return (ItemEntity) wrappedObject;
    }
}
