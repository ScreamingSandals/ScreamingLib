package org.screamingsandals.lib.hologram;

import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.world.LocationHolder;

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

    public static TextHologram textHologram(LocationHolder holder) {
        return textHologram(holder, false);
    }

    public static TextHologram textHologram(LocationHolder holder, boolean touchable) {
        if (creator == null) {
            throw new UnsupportedOperationException("HologramCreator is not initialized yet!");
        }

        return creator.textHologram0(holder, touchable);
    }

    protected abstract TextHologram textHologram0(LocationHolder holder, boolean touchable);

}
