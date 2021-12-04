package org.screamingsandals.lib.event.entity;

public interface SExpBottleEvent extends SProjectileHitEvent {

    int getExp();

    void setExp(int exp);

    boolean isShowEffect();

    void setShowEffect(boolean showEffect);
}
