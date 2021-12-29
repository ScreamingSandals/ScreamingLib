package org.screamingsandals.lib.minestom.entity.type;

import net.minestom.server.entity.EntitySpawnType;
import net.minestom.server.entity.EntityType;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class MinestomEntityTypeHolder extends BasicWrapper<EntityType> implements EntityTypeHolder {
    protected MinestomEntityTypeHolder(EntityType wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean isAlive() {
        final var spawnType = wrappedObject.registry().spawnType();
        return spawnType == EntitySpawnType.LIVING || spawnType == EntitySpawnType.PLAYER;
    }

    @Override
    public boolean is(Object entityType) {
        if (entityType instanceof EntityType || entityType instanceof EntityTypeHolder) {
            return equals(entityType);
        }
        return equals(EntityTypeHolder.ofOptional(entityType).orElse(null));
    }

    @Override
    public boolean is(Object... entityTypes) {
        return Arrays.stream(entityTypes).anyMatch(this::is);
    }
}
