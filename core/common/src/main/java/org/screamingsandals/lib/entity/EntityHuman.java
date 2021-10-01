package org.screamingsandals.lib.entity;

import org.screamingsandals.lib.player.PlayerWrapper;

public interface EntityHuman extends EntityLiving {
    PlayerWrapper asPlayer();

    int getExpToLevel();

    float getSaturation();

    void setSaturation(float saturation);

    float getExhaustion();

    void setExhaustion(float exhaustion);

    int getFoodLevel();

    void setFoodLevel(int foodLevel);
}
