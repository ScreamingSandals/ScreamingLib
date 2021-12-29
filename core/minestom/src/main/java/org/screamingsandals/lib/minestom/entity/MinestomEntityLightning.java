package org.screamingsandals.lib.minestom.entity;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import org.screamingsandals.lib.entity.EntityLightning;

public class MinestomEntityLightning extends MinestomEntityBasic implements EntityLightning {
    protected MinestomEntityLightning(Entity wrappedObject) {
        super(wrappedObject);
        if (wrappedObject.getEntityType() != EntityType.LIGHTNING_BOLT) {
            throw new UnsupportedOperationException("Passed a non-lightning bolt entity to MinestomEntityLightning!");
        }
    }

    @Override
    public boolean isEffect() {
        return true;
    }
}
