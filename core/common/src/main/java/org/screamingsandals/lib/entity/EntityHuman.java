package org.screamingsandals.lib.entity;

public interface EntityHuman extends EntityLiving {
    int getExpToLevel();

    float getSaturation();

    void setSaturation(float saturation);

    float getExhaustion();

    void setExhaustion(float exhaustion);

    int getFoodLevel();

    void setFoodLevel(int foodLevel);
}
