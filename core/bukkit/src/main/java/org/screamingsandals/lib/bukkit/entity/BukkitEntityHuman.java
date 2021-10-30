package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.entity.HumanEntity;
import org.screamingsandals.lib.entity.EntityHuman;

public class BukkitEntityHuman extends BukkitEntityLiving implements EntityHuman {
    public BukkitEntityHuman(HumanEntity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public int getExpToLevel() {
        return ((HumanEntity) wrappedObject).getExpToLevel();
    }

    @Override
    public float getSaturation() {
        return ((HumanEntity) wrappedObject).getSaturation();
    }

    @Override
    public void setSaturation(float saturation) {
        ((HumanEntity) wrappedObject).setSaturation(saturation);
    }

    @Override
    public float getExhaustion() {
        return ((HumanEntity) wrappedObject).getExhaustion();
    }

    @Override
    public void setExhaustion(float exhaustion) {
        ((HumanEntity) wrappedObject).setExhaustion(exhaustion);
    }

    @Override
    public int getFoodLevel() {
        return ((HumanEntity) wrappedObject).getFoodLevel();
    }

    @Override
    public void setFoodLevel(int foodLevel) {
        ((HumanEntity) wrappedObject).setFoodLevel(foodLevel);
    }

    @Override
    public <T> T as(Class<T> type) {
        return super.as(type);
    }
}
