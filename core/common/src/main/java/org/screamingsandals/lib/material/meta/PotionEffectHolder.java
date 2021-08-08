package org.screamingsandals.lib.material.meta;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.Wrapper;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Optional;

@Data
@RequiredArgsConstructor
@ConfigSerializable
public class PotionEffectHolder implements Wrapper {
    private final String platformName;
    private final int duration;
    private final int amplifier;
    private final boolean ambient;
    private final boolean particles;
    private final boolean icon;

    public PotionEffectHolder(String name) {
        this(name, 1, 1, true, true, true);
    }

    public PotionEffectHolder duration(int duration) {
        return new PotionEffectHolder(this.platformName, duration, this.amplifier, this.ambient, this.particles, this.icon);
    }

    public PotionEffectHolder amplifier(int amplifier) {
        return new PotionEffectHolder(this.platformName, this.duration, amplifier, this.ambient, this.particles, this.icon);
    }

    public PotionEffectHolder ambient(boolean ambient) {
        return new PotionEffectHolder(this.platformName, this.duration, this.amplifier, ambient, this.particles, this.icon);
    }

    public PotionEffectHolder particles(boolean particles) {
        return new PotionEffectHolder(this.platformName, this.duration, this.amplifier, this.ambient, particles, this.icon);
    }

    public PotionEffectHolder icon(boolean icon) {
        return new PotionEffectHolder(this.platformName, this.duration, this.amplifier, this.ambient, this.particles, icon);
    }

    @Override
    public <T> T as(Class<T> type) {
        return PotionEffectMapping.convertPotionEffectHolder(this, type);
    }

    public static PotionEffectHolder of(Object effect) {
        return ofOptional(effect).orElseThrow();
    }

    public static Optional<PotionEffectHolder> ofOptional(Object effect) {
        if (effect instanceof PotionEffectHolder) {
            return Optional.of((PotionEffectHolder) effect);
        }
        return PotionEffectMapping.resolve(effect);
    }
}
