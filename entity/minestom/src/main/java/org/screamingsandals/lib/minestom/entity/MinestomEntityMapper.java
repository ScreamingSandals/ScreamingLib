package org.screamingsandals.lib.minestom.entity;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.LivingEntity;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.minestom.entity.type.MinestomEntityTypeMapping;
import org.screamingsandals.lib.utils.InitUtils;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Optional;

@Service(dependsOn = {
        MinestomEntityTypeMapping.class
})
public class MinestomEntityMapper extends EntityMapper {

    public static void init() {
        EntityMapper.init(MinestomEntityMapper::new);
    }

    public MinestomEntityMapper() {
        InitUtils.doIfNot(MinestomEntityTypeMapping::isInitialized, MinestomEntityTypeMapping::init);
    }

    @Override
    protected Optional<EntityBasic> wrapEntity0(Object entity) {
        if (!(entity instanceof Entity)) {
            return Optional.empty();
        }

        // order is important here
        if (entity instanceof LivingEntity) {
            return Optional.of(new MinestomEntityLiving((LivingEntity) entity));
        }

        if (entity instanceof ItemEntity) {
            return Optional.of(new MinestomEntityItem((ItemEntity) entity));
        }

        return Optional.of(new MinestomEntityBasic((Entity) entity));
    }
}
