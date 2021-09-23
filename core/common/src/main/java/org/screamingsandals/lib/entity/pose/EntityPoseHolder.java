package org.screamingsandals.lib.entity.pose;

import lombok.Data;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@Data
@LimitedVersionSupport("Bukkit >= 1.17")
public class EntityPoseHolder implements ComparableWrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return EntityPoseMapping.convertEntityPoseHolder(this, type);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_POSE)
    public boolean is(Object object) {
        return equals(ofOptional(object).orElse(null));
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_POSE)
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_POSE)
    public static EntityPoseHolder of(Object entityPose) {
        return ofOptional(entityPose).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_POSE)
    public static Optional<EntityPoseHolder> ofOptional(Object entityPose) {
        if (entityPose instanceof EntityPoseHolder) {
            return Optional.of((EntityPoseHolder) entityPose);
        }
        return EntityPoseMapping.resolve(entityPose);
    }

    public static List<EntityPoseHolder> all() {
        return EntityPoseMapping.getValues();
    }
}
