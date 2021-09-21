package org.screamingsandals.lib.particle;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.With;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.math.Vector3D;

@Data
@Accessors(fluent = true, chain = true)
@With
@RequiredArgsConstructor
public class ParticleHolder {
    private final ParticleTypeHolder particleType;
    private final int count;
    private final Vector3D offset;
    private final double particleData; // ?
    private final boolean longDistance;
    @Nullable
    private final ParticleData specialData;

    public ParticleHolder(ParticleTypeHolder particleType) {
        this(particleType, 1, Vector3D.ZERO, 1, false, null);
    }

    public ParticleHolder(ParticleTypeHolder particleType, int count) {
        this(particleType, count, Vector3D.ZERO, 1, false, null);
    }

    public ParticleHolder(ParticleTypeHolder particleType, int count, Vector3D offset) {
        this(particleType, count, offset, 1, false, null);
    }

    public ParticleHolder(ParticleTypeHolder particleType, int count, Vector3D offset, double particleData) {
        this(particleType, count, offset, particleData, false, null);
    }

    public ParticleHolder(ParticleTypeHolder particleType, int count, Vector3D offset, double particleData, boolean longDistance) {
        this(particleType, count, offset, particleData, longDistance, null);
    }
}
