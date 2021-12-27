package org.screamingsandals.lib.bukkit.entity.type;

import org.bukkit.entity.EntityType;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Arrays;
import java.util.Optional;

public class BukkitEntityTypeHolder extends BasicWrapper<EntityType> implements EntityTypeHolder {

    public BukkitEntityTypeHolder(EntityType wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean isAlive() {
        return wrappedObject.isAlive();
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

    @Override
    public <T extends EntityBasic> Optional<T> spawn(LocationHolder location) {
        return EntityMapper.spawn(this, location);
    }
}
