package org.screamingsandals.lib.minestom.entity.pose;

import net.minestom.server.entity.Entity;
import org.screamingsandals.lib.entity.pose.EntityPoseHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class MinestomEntityPoseHolder extends BasicWrapper<Entity.Pose> implements EntityPoseHolder {
    protected MinestomEntityPoseHolder(Entity.Pose wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof Entity.Pose || object instanceof EntityPoseHolder) {
            return equals(object);
        }
        return equals(EntityPoseHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
