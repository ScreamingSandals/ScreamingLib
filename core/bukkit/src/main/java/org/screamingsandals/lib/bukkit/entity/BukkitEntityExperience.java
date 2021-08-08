package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.entity.ExperienceOrb;
import org.screamingsandals.lib.entity.EntityExperience;

public class BukkitEntityExperience extends BukkitEntityBasic implements EntityExperience {
    protected BukkitEntityExperience(ExperienceOrb wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public int getExperience() {
        return ((ExperienceOrb) wrappedObject).getExperience();
    }

    @Override
    public void setExperience(int experience) {
        ((ExperienceOrb) wrappedObject).setExperience(experience);
    }
}
