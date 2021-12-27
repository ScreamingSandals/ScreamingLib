package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.entity.*;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.nms.accessors.*;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BukkitEntityMapper extends EntityMapper {

    public static boolean HAS_MOB_INTERFACE = Reflect.has("org.bukkit.entity.Mob");

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends EntityBasic> Optional<T> wrapEntity0(Object entity) {
        if (!(entity instanceof Entity)) {
            return Optional.empty();
        }

        // order is important here
        if (entity instanceof Player) {
            return Optional.of((T) new BukkitEntityPlayer((Player) entity));
        }

        if (entity instanceof HumanEntity) {
            return Optional.of((T) new BukkitEntityHuman((HumanEntity) entity));
        }

        if (HAS_MOB_INTERFACE) {
            if (entity instanceof Mob) {
                return Optional.of((T) new BukkitEntityPathfindingMob((LivingEntity) entity));
            }
        } else if (entity instanceof Slime || entity instanceof Creature) {
            return Optional.of((T) new BukkitEntityPathfindingMob((LivingEntity) entity));
        }

        if (entity instanceof LivingEntity) {
            return Optional.of((T) new BukkitEntityLiving((LivingEntity) entity));
        }

        if (entity instanceof Firework) {
            return Optional.of((T) new BukkitEntityFirework((Firework) entity));
        }

        if (entity instanceof Projectile) {
            return Optional.of((T) new BukkitEntityProjectile((Projectile) entity));
        }

        if (entity instanceof Item) {
            return Optional.of((T) new BukkitEntityItem((Item) entity));
        }

        if (entity instanceof LightningStrike) {
            return Optional.of((T) new BukkitEntityLightning((LightningStrike) entity));
        }

        if (entity instanceof ExperienceOrb) {
            return Optional.of((T) new BukkitEntityExperience((ExperienceOrb) entity));
        }

        return Optional.of((T) new BukkitEntityBasic((Entity) entity));
    }

    @Override
    public <T extends EntityBasic> Optional<T> spawn0(EntityTypeHolder entityType, LocationHolder locationHolder) {
        return entityType.asOptional(EntityType.class).flatMap(entityType1 -> {
            var bukkitLoc = locationHolder.as(Location.class);
            var world = bukkitLoc.getWorld();
            if (world != null) {
                // TODO: test all entity types
                var entity = world.spawnEntity(bukkitLoc, entityType1);
                return wrapEntity0(entity);
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<EntityItem> dropItem0(org.screamingsandals.lib.item.Item item, LocationHolder locationHolder) {
        var bukkitLoc = locationHolder.as(Location.class);
        var itemEntity = bukkitLoc.getWorld().dropItem(bukkitLoc, item.as(ItemStack.class));
        return Optional.of(new BukkitEntityItem(itemEntity));
    }

    @Override
    public Optional<EntityExperience> dropExperience0(int experience, LocationHolder locationHolder) {
        var bukkitLoc = locationHolder.as(Location.class);
        var orb = (ExperienceOrb) bukkitLoc.getWorld().spawnEntity(bukkitLoc, EntityType.EXPERIENCE_ORB);
        orb.setExperience(experience);
        return Optional.of(new BukkitEntityExperience(orb));
    }

    @Override
    public Optional<EntityLightning> strikeLightning0(LocationHolder locationHolder) {
        var bukkitLoc = locationHolder.as(Location.class);
        var lightning = bukkitLoc.getWorld().strikeLightning(bukkitLoc);
        return Optional.of(new BukkitEntityLightning(lightning));
    }

    @Override
    public int getNewEntityId0() {
        final var entityCount = Reflect.getField(EntityAccessor.getFieldField_70152_a());
        if (entityCount != null) {
            if (entityCount instanceof AtomicInteger) {
                return ((AtomicInteger) entityCount).incrementAndGet();
            }
            final var newCount = ((int) entityCount) + 1;
            Reflect.setField(EntityAccessor.getFieldField_70152_a(), newCount);
            return newCount;
        }

        final var entityCounter = Reflect.getField(EntityAccessor.getFieldENTITY_COUNTER());
        if (entityCounter instanceof AtomicInteger) {
            return ((AtomicInteger) entityCounter).incrementAndGet();
        }
        throw new UnsupportedOperationException("Can't obtain new Entity id");
    }

    @Override
    public CompletableFuture<Integer> getNewEntityIdSynchronously0() {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        if (Server.isServerThread()) {
            future.complete(getNewEntityId());
        } else {
            Server.runSynchronously(() -> future.complete(getNewEntityId()));
        }
        future.exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
        return future;
    }
}
