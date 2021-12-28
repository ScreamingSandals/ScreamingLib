package org.screamingsandals.lib.entity.pose;

import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@LimitedVersionSupport("Bukkit >= 1.17")
public interface EntityPoseHolder extends ComparableWrapper, RawValueHolder {

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default String getPlatformName() {
        return platformName();
    }

    String platformName();

    /**
     * {@inheritDoc}
     */
    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_POSE)
    boolean is(Object object);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_POSE)
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_POSE)
    static EntityPoseHolder of(Object entityPose) {
        return ofOptional(entityPose).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_POSE)
    static Optional<EntityPoseHolder> ofOptional(Object entityPose) {
        if (entityPose instanceof EntityPoseHolder) {
            return Optional.of((EntityPoseHolder) entityPose);
        }
        return EntityPoseMapping.resolve(entityPose);
    }

    static List<EntityPoseHolder> all() {
        return EntityPoseMapping.getValues();
    }
}
