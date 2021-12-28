package org.screamingsandals.lib.bukkit.item.meta;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class BukkitPotionEffectHolder extends BasicWrapper<PotionEffect> implements PotionEffectHolder {

    public BukkitPotionEffectHolder(PotionEffectType type) {
        this(new PotionEffect(type, 0,0));
    }

    public BukkitPotionEffectHolder(PotionEffect wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.getType().getName();
    }

    @Override
    public int duration() {
        return wrappedObject.getDuration();
    }

    @Override
    public int amplifier() {
        return wrappedObject.getAmplifier();
    }

    @Override
    public boolean ambient() {
        return wrappedObject.isAmbient();
    }

    @Override
    public boolean particles() {
        return wrappedObject.hasParticles();
    }

    @Override
    public boolean icon() {
        return wrappedObject.hasIcon();
    }

    @Override
    public PotionEffectHolder withDuration(int duration) {
        return new BukkitPotionEffectHolder(wrappedObject.withDuration(duration));
    }

    @Override
    public PotionEffectHolder withAmplifier(int amplifier) {
        return new BukkitPotionEffectHolder(wrappedObject.withAmplifier(amplifier));
    }

    @Override
    public PotionEffectHolder withAmbient(boolean ambient) {
        return new BukkitPotionEffectHolder(wrappedObject.withAmbient(ambient));
    }

    @Override
    public PotionEffectHolder withParticles(boolean particles) {
        return new BukkitPotionEffectHolder(wrappedObject.withParticles(particles));
    }

    @Override
    public PotionEffectHolder withIcon(boolean icon) {
        return new BukkitPotionEffectHolder(wrappedObject.withIcon(icon));
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof PotionEffect || object instanceof PotionEffectHolder) {
            return equals(object);
        }
        return equals(PotionEffectHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public boolean isSameType(Object object) {
        if (object instanceof PotionEffect) {
            return ((PotionEffect) object).getType().equals(wrappedObject.getType());
        } else if (object instanceof BukkitPotionEffectHolder) {
            return ((BukkitPotionEffectHolder) object).wrappedObject.getType().equals(wrappedObject.getType());
        } else if (object instanceof PotionEffectType) {
            return object.equals(wrappedObject.getType());
        }
        return platformName().equals(PotionEffectHolder.ofOptional(object).map(PotionEffectHolder::platformName).orElse(null));
    }

    @Override
    public boolean isSameType(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::isSameType);
    }
}
