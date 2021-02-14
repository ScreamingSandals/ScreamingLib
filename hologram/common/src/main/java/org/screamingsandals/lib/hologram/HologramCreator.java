package org.screamingsandals.lib.hologram;

import org.screamingsandals.lib.hologram.builder.TextHologramBuilder;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.function.Supplier;

@AbstractService
public abstract class HologramCreator {
    private static HologramCreator creator;

    public static void init(Supplier<HologramCreator> supplier) {
        if (creator != null) {
            throw new UnsupportedOperationException("HologramCreator is already initialized");
        }
        creator = supplier.get();
    }

    public static TextHologram buildTextHologram(TextHologramBuilder builder) {
        if (creator == null) {
            throw new UnsupportedOperationException("HologramCreator is not initialized yet!");
        }

        return creator.buildTextHologram0(builder);
    }

    protected abstract TextHologram buildTextHologram0(TextHologramBuilder builder);

}
