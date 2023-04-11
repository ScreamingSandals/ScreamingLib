/*
 * Copyright 2023 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.bukkit.entity;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.entity.ItemEntity;
import org.screamingsandals.lib.item.ItemStack;

import java.util.concurrent.TimeUnit;

public class BukkitItemEntity extends BukkitEntity implements ItemEntity {

    public BukkitItemEntity(org.bukkit.entity.@NotNull Item wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull ItemStack getItem() {
        return new BukkitItem(getWrappedObject().getItemStack());
    }

    @Override
    public void setItem(@NotNull ItemStack stack) {
        getWrappedObject().setItemStack(stack.as(org.bukkit.inventory.ItemStack.class));
    }

    @Override
    public int getPickupDelay() {
        return getWrappedObject().getPickupDelay();
    }

    @Override
    public void setPickupDelay(int delay, @NotNull TimeUnit timeUnit) {
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

    public org.bukkit.entity.@NotNull Item getWrappedObject() {
        return (org.bukkit.entity.Item) wrappedObject;
    }
}
