package org.screamingsandals.lib.minestom.entity;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import org.screamingsandals.lib.entity.*;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class MinestomEntityMapper extends EntityMapper {
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

    @Override
    public Optional<EntityItem> dropItem0(Item item, LocationHolder locationHolder) {
        return Optional.empty();
    }

    @Override
    public Optional<EntityExperience> dropExperience0(int experience, LocationHolder locationHolder) {
        return Optional.empty();
    }

    @Override
    public Optional<EntityLightning> strikeLightning0(LocationHolder locationHolder) {
        return Optional.empty();
    }

    @Override
    public int getNewEntityId0() {
        return 0;
    }

    @Override
    public CompletableFuture<Integer> getNewEntityIdSynchronously0() {
        return null;
    }
}
