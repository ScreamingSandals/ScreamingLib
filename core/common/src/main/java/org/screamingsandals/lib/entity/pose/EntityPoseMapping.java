package org.screamingsandals.lib.entity.pose;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class EntityPoseMapping extends AbstractTypeMapper<EntityPoseHolder> {
    private static EntityPoseMapping entityPoseMapping;

    protected final BidirectionalConverter<EntityPoseHolder> entityPoseConverter = BidirectionalConverter.<EntityPoseHolder>build()
            .registerP2W(EntityPoseHolder.class, d -> d);

    @ApiStatus.Internal
    public EntityPoseMapping() {
        if (entityPoseMapping != null) {
            throw new UnsupportedOperationException("EntityPoseMapping is already initialized!");
        }
        entityPoseMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_POSE)
    @OfMethodAlternative(value = EntityPoseHolder.class, methodName = "ofOptional")
    public static Optional<EntityPoseHolder> resolve(Object entityPose) {
        if (entityPoseMapping == null) {
            throw new UnsupportedOperationException("EntityPoseMapping is not initialized yet.");
        }

        if (entityPose == null) {
            return Optional.empty();
        }

        return entityPoseMapping.entityPoseConverter.convertOptional(entityPose).or(() -> entityPoseMapping.resolveFromMapping(entityPose));
    }

    @OfMethodAlternative(value = EntityPoseHolder.class, methodName = "all")
    public static List<EntityPoseHolder> getValues() {
        if (entityPoseMapping == null) {
            throw new UnsupportedOperationException("EntityPoseMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(entityPoseMapping.values);
    }
}
