package org.screamingsandals.lib.entity;

import org.screamingsandals.lib.world.LocationHolder;

import java.util.Optional;

public interface EntityExperience extends EntityBasic {
    int getExperience();

    void setExperience(int experience);

    static Optional<EntityExperience> dropExperience(int experience, LocationHolder location) {
        return EntityMapper.dropExperience(experience, location);
    }
}
