package org.screamingsandals.lib.particle;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface ParticleTypeHolder extends ComparableWrapper {
    String platformName();

    @Nullable
    Class<? extends ParticleData> expectedDataClass();

    /**
     * {@inheritDoc}
     */
    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.PARTICLE_TYPE)
    boolean is(Object object);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.PARTICLE_TYPE)
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.PARTICLE_TYPE)
    static ParticleTypeHolder of(Object particle) {
        return ofOptional(particle).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.PARTICLE_TYPE)
    static Optional<ParticleTypeHolder> ofOptional(Object particle) {
        if (particle instanceof ParticleTypeHolder) {
            return Optional.of((ParticleTypeHolder) particle);
        }
        return ParticleTypeMapping.resolve(particle);
    }

    static List<ParticleTypeHolder> all() {
        return ParticleTypeMapping.getValues();
    }
}
