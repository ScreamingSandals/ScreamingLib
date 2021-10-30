package org.screamingsandals.lib.entity;

import org.screamingsandals.lib.player.PlayerWrapper;

public interface EntityHuman extends EntityLiving {
    @Deprecated
    default PlayerWrapper asPlayer() {
        return this instanceof PlayerWrapper ? (PlayerWrapper) this : null;
    }

    int getExpToLevel();

    float getSaturation();

    void setSaturation(float saturation);

    float getExhaustion();

    void setExhaustion(float exhaustion);

    int getFoodLevel();

    void setFoodLevel(int foodLevel);
}
