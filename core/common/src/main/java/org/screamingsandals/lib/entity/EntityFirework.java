package org.screamingsandals.lib.entity;

import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.utils.Pair;

import java.util.List;

public interface EntityFirework extends EntityProjectile {

    void setEffect(List<FireworkEffectHolder> fireworkEffect, int power);

    Pair<List<FireworkEffectHolder>, Integer> getEffect();

    void detonate();

    boolean isShotAtAngle();

    void setShotAtAngle(boolean shotAtAngle);
}
