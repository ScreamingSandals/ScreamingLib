package org.screamingsandals.lib.minestom.particle;

import net.minestom.server.particle.Particle;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.particle.ParticleTypeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class MinestomParticleTypeHolder extends BasicWrapper<Particle> implements ParticleTypeHolder {
    protected MinestomParticleTypeHolder(Particle wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public @Nullable Class<? extends ParticleData> expectedDataClass() {
        return null;
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof Particle || object instanceof ParticleTypeHolder) {
            return equals(object);
        }
        return equals(ParticleTypeHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
