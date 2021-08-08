package org.screamingsandals.lib.entity;

import org.screamingsandals.lib.world.LocationHolder;

import java.util.Optional;

public interface EntityLightning extends EntityBasic {
    boolean isEffect();

    static Optional<EntityLightning> strike(LocationHolder locationHolder) {
        return EntityMapper.strikeLightning(locationHolder);
    }
}
