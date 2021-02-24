package org.screamingsandals.lib.minestom.entity;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.*;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.minestom.entity.type.MinestomEntityTypeMapping;
import org.screamingsandals.lib.minestom.world.InstancedPosition;
import org.screamingsandals.lib.utils.InitUtils;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;

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
    @SuppressWarnings("unchecked")
    protected <T extends EntityBasic> Optional<T> wrapEntity0(Object entity) {
        if (!(entity instanceof Entity)) {
            return Optional.empty();
        }

        // order is important here
        if (entity instanceof Player) {
            return Optional.of((T) new MinestomEntityHuman((Player) entity));
        }

        if (entity instanceof LivingEntity) {
            return Optional.of((T) new MinestomEntityLiving((LivingEntity) entity));
        }

        if (entity instanceof ItemEntity) {
            return Optional.of((T) new MinestomEntityItem((ItemEntity) entity));
        }

        return Optional.of((T) new MinestomEntityBasic((Entity) entity));
    }

    @Override
    public <T extends EntityBasic> Optional<T> spawn0(EntityTypeHolder entityType, LocationHolder locationHolder) {
        return Optional.empty(); // TODO
    }
}
