package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.screamingsandals.lib.entity.EntityFirework;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.utils.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class BukkitEntityFirework extends BukkitEntityProjectile implements EntityFirework {
    protected BukkitEntityFirework(Firework wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public void setEffect(List<FireworkEffectHolder> fireworkEffect, int power) {
        var meta = ((Firework) wrappedObject).getFireworkMeta();
        meta.setPower(power);
        meta.addEffects(fireworkEffect.stream().map(p -> p.as(FireworkEffect.class)).collect(Collectors.toList()));
        ((Firework) wrappedObject).setFireworkMeta(meta);
    }

    @Override
    public Pair<List<FireworkEffectHolder>, Integer> getEffect() {
        var meta = ((Firework) wrappedObject).getFireworkMeta();
        return Pair.of(meta.getEffects().stream().map(FireworkEffectHolder::of).collect(Collectors.toList()), meta.getPower());
    }

    @Override
    public void detonate() {
        ((Firework) wrappedObject).detonate();
    }

    @Override
    public boolean isShotAtAngle() {
        return ((Firework) wrappedObject).isShotAtAngle();
    }

    @Override
    public void setShotAtAngle(boolean shotAtAngle) {
        ((Firework) wrappedObject).setShotAtAngle(shotAtAngle);
    }
}
