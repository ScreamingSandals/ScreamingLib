package org.screamingsandals.lib.bukkit.entity.pose;

import org.bukkit.entity.Pose;
import org.screamingsandals.lib.entity.pose.EntityPoseHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class BukkitEntityPoseHolder extends BasicWrapper<Pose> implements EntityPoseHolder {

    protected BukkitEntityPoseHolder(Pose wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof Pose || object instanceof EntityPoseHolder) {
            return equals(object);
        }
        return equals(EntityPoseHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
