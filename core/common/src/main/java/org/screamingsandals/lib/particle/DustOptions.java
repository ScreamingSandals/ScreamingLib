package org.screamingsandals.lib.particle;

import lombok.Data;
import lombok.experimental.Accessors;
import net.kyori.adventure.util.RGBLike;

@Data
@Accessors(chain = true, fluent = true)
public class DustOptions implements ParticleData {
    private final RGBLike color;
    private final float size;

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T as(Class<T> type) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
