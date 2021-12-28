package org.screamingsandals.lib.minestom.entity;

import net.minestom.server.entity.ExperienceOrb;
import org.screamingsandals.lib.entity.EntityExperience;

public class MinestomEntityExperience extends MinestomEntityBasic implements EntityExperience {
    protected MinestomEntityExperience(ExperienceOrb wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public int getExperience() {
        return ((ExperienceOrb) wrappedObject).getExperienceCount();
    }

    @Override
    public void setExperience(int experience) {
        ((ExperienceOrb) wrappedObject).setExperienceCount((short) experience);
    }
}
