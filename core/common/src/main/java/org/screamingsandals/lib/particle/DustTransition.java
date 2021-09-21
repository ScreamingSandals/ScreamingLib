package org.screamingsandals.lib.particle;

import lombok.Data;
import lombok.experimental.Accessors;
import net.kyori.adventure.util.RGBLike;

@Data
@Accessors(chain = true, fluent = true)
public class DustTransition implements ParticleData {
    private final RGBLike fromColor;
    private final RGBLike toColor;
    private final float size;

    @Override
    public <T> T as(Class<T> type) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
