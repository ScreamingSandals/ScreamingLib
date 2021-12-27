package org.screamingsandals.lib.bukkit.particle;

import org.bukkit.Particle;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.particle.*;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class BukkitParticleTypeHolder extends BasicWrapper<Particle> implements ParticleTypeHolder {
    protected BukkitParticleTypeHolder(Particle wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public Class<? extends ParticleData> expectedDataClass() {
        var dataType = wrappedObject.getDataType();
        if (dataType != Void.class) {
            switch (dataType.getSimpleName()) {
                case "MaterialData":
                case "BlockData":
                    return BlockTypeHolder.class;
                case "ItemStack":
                    return Item.class;
                case "DustOptions":
                    return DustOptions.class;
                case "DustTransition":
                    return DustTransition.class;
            }
        }
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
