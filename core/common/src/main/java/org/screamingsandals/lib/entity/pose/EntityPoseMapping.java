package org.screamingsandals.lib.entity.pose;

import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.Optional;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class EntityPoseMapping extends AbstractTypeMapper<EntityPoseHolder> {
    private static EntityPoseMapping entityPoseMapping;

    protected final BidirectionalConverter<EntityPoseHolder> entityPoseConverter = BidirectionalConverter.<EntityPoseHolder>build()
            .registerP2W(EntityPoseHolder.class, d -> d);

    protected EntityPoseMapping() {
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

    public static <T> T convertEntityPoseHolder(EntityPoseHolder holder, Class<T> newType) {
        if (entityPoseMapping == null) {
            throw new UnsupportedOperationException("EntityPoseMapping is not initialized yet.");
        }
        return entityPoseMapping.entityPoseConverter.convert(holder, newType);
    }
}
