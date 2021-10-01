package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.entity.EntityHuman;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;

public class BukkitEntityHuman extends BukkitEntityLiving implements EntityHuman {
    protected BukkitEntityHuman(HumanEntity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public PlayerWrapper asPlayer() {
        if (wrappedObject instanceof Player) {
            return PlayerMapper.wrapPlayer(wrappedObject);
        }
        return null;
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

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (PlayerWrapper.class.isAssignableFrom(type)) {
            return (T) asPlayer();
        }
        return super.as(type);
    }
}
