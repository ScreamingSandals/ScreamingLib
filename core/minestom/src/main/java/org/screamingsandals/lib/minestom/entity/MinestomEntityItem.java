package org.screamingsandals.lib.minestom.entity;

import net.minestom.server.entity.ItemEntity;
import net.minestom.server.item.ItemStack;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.builder.ItemFactory;

import java.util.concurrent.TimeUnit;

public class MinestomEntityItem extends MinestomEntityBasic implements EntityItem {
    protected MinestomEntityItem(ItemEntity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Item getItem() {
        return ItemFactory.build(((ItemEntity) wrappedObject).getItemStack()).orElse(null);
    }

    @Override
    public void setItem(Item stack) {
        ((ItemEntity) wrappedObject).setItemStack(ItemFactory.convertItem(stack, ItemStack.class));
    }

    @Override
    public int getPickupDelay() {
        return (int) ((ItemEntity) wrappedObject).getPickupDelay();
    }

    @Override
    public void setPickupDelay(int delay, TimeUnit timeUnit) {
        ((ItemEntity) wrappedObject).setPickupDelay(delay, timeUnit.toChronoUnit());
    }

    @Override
    public boolean isPickable() {
        return ((ItemEntity) wrappedObject).isPickable();
    }

    @Override
    public void setPickable(boolean pickable) {
        ((ItemEntity) wrappedObject).setPickable(pickable);
    }

    @Override
    public boolean isMergeable() {
        return ((ItemEntity) wrappedObject).isMergeable();
    }

    @Override
    public void setMergeable(boolean mergeable) {
        ((ItemEntity) wrappedObject).setMergeable(mergeable);
    }

    @Override
    public float getMergeRange() {
        return ((ItemEntity) wrappedObject).getMergeRange();
    }

    @Override
    public void setMergeRange(float mergeRange) {
        ((ItemEntity) wrappedObject).setMergeRange(mergeRange);
    }

    @Override
    public long getSpawnTime() {
        return ((ItemEntity) wrappedObject).getSpawnTime();
    }
}
