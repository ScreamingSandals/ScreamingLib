package org.screamingsandals.lib.minestom.entity;

import net.minestom.server.entity.Player;
import org.screamingsandals.lib.entity.EntityHuman;

public class MinestomEntityHuman extends MinestomEntityLiving implements EntityHuman {
    protected MinestomEntityHuman(Player wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public int getExpToLevel() {
        return ((Player) wrappedObject).getLevel();
    }

    @Override
    public float getSaturation() {
        return ((Player) wrappedObject).getFoodSaturation();
    }

    @Override
    public void setSaturation(float saturation) {
        ((Player) wrappedObject).setFoodSaturation(saturation);
    }

    @Override
    public float getExhaustion() {
        return 0;
    }

    @Override
    public void setExhaustion(float exhaustion) {

    }

    @Override
    public int getFoodLevel() {
        return ((Player) wrappedObject).getFood();
    }

    @Override
    public void setFoodLevel(int foodLevel) {
        ((Player) wrappedObject).setFood(foodLevel);
    }
}
