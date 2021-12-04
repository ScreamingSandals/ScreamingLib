package org.screamingsandals.lib.bukkit.event.entity;

import org.bukkit.event.entity.ExpBottleEvent;
import org.screamingsandals.lib.event.entity.SExpBottleEvent;

public class SBukkitExpBottleEvent extends SBukkitProjectileHitEvent implements SExpBottleEvent {
    public SBukkitExpBottleEvent(ExpBottleEvent event) {
        super(event);
    }

    @Override
    public int getExp() {
        return getEvent().getExperience();
    }

    @Override
    public void setExp(int exp) {
        getEvent().setExperience(exp);
    }

    @Override
    public boolean isShowEffect() {
        return getEvent().getShowEffect();
    }

    @Override
    public void setShowEffect(boolean showEffect) {
        getEvent().setShowEffect(showEffect);
    }

    @Override
    public ExpBottleEvent getEvent() {
        return (ExpBottleEvent) super.getEvent();
    }
}
