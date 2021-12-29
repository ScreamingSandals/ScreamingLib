package org.screamingsandals.lib.minestom.entity;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityProjectile;
import net.minestom.server.entity.*;
import net.minestom.server.entity.pathfinding.NavigableEntity;
import net.minestom.server.instance.Instance;
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

        if (entity instanceof NavigableEntity) {
            return Optional.of((T) new MinestomEntityPathfindingMob((LivingEntity & NavigableEntity) entity));
        }

        if (entity instanceof LivingEntity) {
            return Optional.of((T) new MinestomEntityLiving((LivingEntity) entity));
        }

        if (((Entity) entity).getEntityType() == EntityType.FIREWORK_ROCKET) {
            return Optional.of((T) new MinestomEntityFirework((EntityProjectile) entity));
        }

        if (entity instanceof EntityProjectile) {
            return Optional.of((T) new MinestomEntityProjectile((EntityProjectile) entity));
        }

        if (entity instanceof ItemEntity) {
            return Optional.of((T) new MinestomEntityItem((ItemEntity) entity));
        }

        if (((Entity) entity).getEntityType() == EntityType.LIGHTNING_BOLT) {
            return Optional.of((T) new MinestomEntityLightning((Entity) entity));
        }

        if (entity instanceof ExperienceOrb) {
            return Optional.of((T) new MinestomEntityExperience((ExperienceOrb) entity));
        }

        return Optional.of((T) new MinestomEntityBasic((Entity) entity));
    }

    private Entity createEntity(EntityType entityType) {
        if (entityType == EntityType.ARROW || entityType == EntityType.EXPERIENCE_BOTTLE
                || entityType == EntityType.EGG || entityType == EntityType.ENDER_PEARL
                || entityType == EntityType.EYE_OF_ENDER || entityType == EntityType.FIREBALL
                || entityType == EntityType.POTION || entityType == EntityType.SNOWBALL
                || entityType == EntityType.TRIDENT || entityType == EntityType.FIREWORK_ROCKET) {
            return new EntityProjectile(null, entityType);
        }
        if (entityType.registry().spawnType() == EntitySpawnType.LIVING) {
            return new LivingEntity(entityType);
        }
        return new Entity(entityType);
    }

    @Override
    public <T extends EntityBasic> Optional<T> spawn0(EntityTypeHolder entityType, LocationHolder locationHolder) {
        return entityType.asOptional(EntityType.class).flatMap(entityType1 -> {
            final var entity = createEntity(entityType1);
            if (locationHolder.getWorld() != null) {
                entity.setInstance(locationHolder.getWorld().as(Instance.class), locationHolder.as(Pos.class)).thenRun(entity::spawn);
            }
            return wrapEntity0(entity);
        });
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
        return Entity.generateId();
    }

    @Override
    public CompletableFuture<Integer> getNewEntityIdSynchronously0() {
        return CompletableFuture.completedFuture(Entity.generateId());
    }
}
