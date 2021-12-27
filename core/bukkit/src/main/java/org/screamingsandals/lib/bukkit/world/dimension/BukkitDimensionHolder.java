package org.screamingsandals.lib.bukkit.world.dimension;

import org.bukkit.World;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.dimension.DimensionHolder;

import java.util.Arrays;

public class BukkitDimensionHolder extends BasicWrapper<World.Environment> implements DimensionHolder {

    protected BukkitDimensionHolder(World.Environment wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof World.Environment || object instanceof DimensionHolder) {
            return equals(object);
        }
        return equals(DimensionHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
