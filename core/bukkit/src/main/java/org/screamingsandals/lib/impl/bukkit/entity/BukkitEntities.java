/*
 * Copyright 2023 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.impl.bukkit.entity;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Item;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.ExperienceOrb;
import org.screamingsandals.lib.entity.ItemEntity;
import org.screamingsandals.lib.entity.LightningBolt;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.entity.hanging.BukkitHangingEntity;
import org.screamingsandals.lib.impl.bukkit.entity.hanging.BukkitItemFrame;
import org.screamingsandals.lib.impl.bukkit.entity.hanging.BukkitLeashKnot;
import org.screamingsandals.lib.impl.bukkit.entity.hanging.BukkitPainting;
import org.screamingsandals.lib.impl.bukkit.entity.illager.BukkitEvokerFangs;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitAbstractArrow;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitArrow;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitDragonFireball;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitEgg;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitEnderPearl;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitExperienceBottle;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitEyeOfEnder;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitFireball;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitFireworkRocket;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitFishingBobber;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitHurtingProjectileEntity;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitLargeFireball;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitLlamaSpit;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitProjectileEntity;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitShulkerBullet;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitSmallFireball;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitSnowball;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitSpectralArrow;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitThrowableProjectileEntity;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitThrownPotion;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitTrident;
import org.screamingsandals.lib.impl.bukkit.entity.projectile.BukkitWitherSkull;
import org.screamingsandals.lib.impl.bukkit.entity.technical.BukkitBlockDisplay;
import org.screamingsandals.lib.impl.bukkit.entity.technical.BukkitDisplay;
import org.screamingsandals.lib.impl.bukkit.entity.technical.BukkitInteraction;
import org.screamingsandals.lib.impl.bukkit.entity.technical.BukkitItemDisplay;
import org.screamingsandals.lib.impl.bukkit.entity.technical.BukkitMarker;
import org.screamingsandals.lib.impl.bukkit.entity.technical.BukkitTextDisplay;
import org.screamingsandals.lib.impl.bukkit.entity.type.BukkitEntityType1_8;
import org.screamingsandals.lib.impl.bukkit.entity.vehicle.BukkitBoat;
import org.screamingsandals.lib.impl.bukkit.entity.vehicle.BukkitChestBoat;
import org.screamingsandals.lib.impl.bukkit.entity.vehicle.BukkitChestMinecart;
import org.screamingsandals.lib.impl.bukkit.entity.vehicle.BukkitCommandBlockMinecart;
import org.screamingsandals.lib.impl.bukkit.entity.vehicle.BukkitFurnaceMinecart;
import org.screamingsandals.lib.impl.bukkit.entity.vehicle.BukkitHopperMinecart;
import org.screamingsandals.lib.impl.bukkit.entity.vehicle.BukkitMinecart;
import org.screamingsandals.lib.impl.bukkit.entity.vehicle.BukkitRideableMinecart;
import org.screamingsandals.lib.impl.bukkit.entity.vehicle.BukkitSpawnerMinecart;
import org.screamingsandals.lib.impl.bukkit.entity.vehicle.BukkitTntMinecart;
import org.screamingsandals.lib.impl.bukkit.entity.vehicle.BukkitVehicle;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.impl.bukkit.entity.type.InternalEntityLegacyConstants;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.nms.accessors.EntityAccessor;
import org.screamingsandals.lib.nms.accessors.EnumZombieTypeAccessor;
import org.screamingsandals.lib.nms.accessors.ZombieAccessor;
import org.screamingsandals.lib.tasker.DefaultThreads;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.Location;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Service
public class BukkitEntities extends Entities {

    @Override
    protected @Nullable Entity wrapEntity0(@NotNull Object entity) {
        if (entity instanceof Entity) {
            return (Entity) entity;
        }

        if (!(entity instanceof org.bukkit.entity.Entity)) {
            return null;
        }

        // TODO: this operation of looking for correct implementation looks heavy to me, maybe introduce some type-based cache?

        // ORDER IS IMPORTANT HERE - KEEP IT RIGHT
        if (entity instanceof org.bukkit.entity.LivingEntity) {
            if (entity instanceof org.bukkit.entity.HumanEntity) {
                if (entity instanceof Player) {
                    return new BukkitPlayer((Player) entity);
                }

                return new BukkitHumanEntity((org.bukkit.entity.HumanEntity) entity);
            }

            if (BukkitFeature.MOB_INTERFACE.isSupported()) {
                if (entity instanceof Mob) {
                    return new BukkitPathfindingMob((org.bukkit.entity.LivingEntity) entity);
                }
            } else if (entity instanceof Slime || entity instanceof Creature) {
                return new BukkitPathfindingMob((org.bukkit.entity.LivingEntity) entity);
            }

            if (entity instanceof org.bukkit.entity.ArmorStand) {
                return new BukkitArmorStand((org.bukkit.entity.ArmorStand) entity);
            }

            return new BukkitLivingEntity((org.bukkit.entity.LivingEntity) entity);
        }

        if (entity instanceof org.bukkit.entity.Vehicle) {
            if (entity instanceof org.bukkit.entity.Boat) {
                if (BukkitFeature.ENTITY_CHEST_BOAT.isSupported()) {
                    if (entity instanceof org.bukkit.entity.ChestBoat) {
                        return new BukkitChestBoat((org.bukkit.entity.ChestBoat) entity);
                    }
                }
                return new BukkitBoat((org.bukkit.entity.Boat) entity);
            }
            if (entity instanceof org.bukkit.entity.Minecart) {
                if (entity instanceof org.bukkit.entity.minecart.StorageMinecart) {
                    return new BukkitChestMinecart((org.bukkit.entity.minecart.StorageMinecart) entity);
                }
                if (entity instanceof org.bukkit.entity.minecart.CommandMinecart) {
                    return new BukkitCommandBlockMinecart((org.bukkit.entity.minecart.CommandMinecart) entity);
                }
                if (entity instanceof org.bukkit.entity.minecart.PoweredMinecart) {
                    return new BukkitFurnaceMinecart((org.bukkit.entity.minecart.PoweredMinecart) entity);
                }
                if (entity instanceof org.bukkit.entity.minecart.HopperMinecart) {
                    return new BukkitHopperMinecart((org.bukkit.entity.minecart.HopperMinecart) entity);
                }
                if (entity instanceof org.bukkit.entity.minecart.RideableMinecart) {
                    return new BukkitRideableMinecart((org.bukkit.entity.minecart.RideableMinecart) entity);
                }
                if (entity instanceof org.bukkit.entity.minecart.SpawnerMinecart) {
                    return new BukkitSpawnerMinecart((org.bukkit.entity.minecart.SpawnerMinecart) entity);
                }
                if (entity instanceof org.bukkit.entity.minecart.ExplosiveMinecart) {
                    return new BukkitTntMinecart((org.bukkit.entity.minecart.ExplosiveMinecart) entity);
                }

                return new BukkitMinecart((org.bukkit.entity.Minecart) entity);
            }

            return new BukkitVehicle((org.bukkit.entity.Vehicle) entity);
        }

        if (entity instanceof org.bukkit.entity.ExperienceOrb) {
            return new BukkitExperienceOrb((org.bukkit.entity.ExperienceOrb) entity);
        }

        if (entity instanceof Projectile) {
            if (entity instanceof org.bukkit.entity.Fireball) {
                if (BukkitFeature.ENTITY_SIZED_FIREBALL.isSupported()) {
                    if (entity instanceof org.bukkit.entity.SizedFireball) {
                        if (entity instanceof org.bukkit.entity.LargeFireball) {
                            return new BukkitLargeFireball((org.bukkit.entity.LargeFireball) entity);
                        }
                        if (entity instanceof org.bukkit.entity.SmallFireball) {
                            return new BukkitSmallFireball((org.bukkit.entity.SmallFireball) entity);
                        }

                        return new BukkitFireball((org.bukkit.entity.SizedFireball) entity);
                    }
                } else {
                    if (entity instanceof org.bukkit.entity.LargeFireball) {
                        return new BukkitLargeFireball((org.bukkit.entity.LargeFireball) entity);
                    }
                    if (entity instanceof org.bukkit.entity.SmallFireball) {
                        return new BukkitSmallFireball((org.bukkit.entity.SmallFireball) entity);
                    }
                }

                if (entity instanceof org.bukkit.entity.WitherSkull) {
                    return new BukkitWitherSkull((org.bukkit.entity.WitherSkull) entity);
                }

                if (BukkitFeature.ENTITY_DRAGON_FIREBALL.isSupported()) {
                    if (entity instanceof org.bukkit.entity.DragonFireball) {
                        return new BukkitDragonFireball((org.bukkit.entity.DragonFireball) entity);
                    }
                }

                return new BukkitHurtingProjectileEntity((org.bukkit.entity.Fireball) entity);
            }

            if (BukkitFeature.ENTITY_ABSTRACT_ARROW.isSupported()) {
                if (entity instanceof org.bukkit.entity.AbstractArrow) {
                    // does not have to check for Bukkit features here: Trident and SpectralArrow were both implemented before AbstractArrow
                    if (entity instanceof org.bukkit.entity.Trident) {
                        return new BukkitTrident((org.bukkit.entity.Trident) entity);
                    }

                    if (entity instanceof org.bukkit.entity.SpectralArrow) {
                        return new BukkitSpectralArrow((org.bukkit.entity.SpectralArrow) entity);
                    }

                    if (entity instanceof org.bukkit.entity.Arrow) {
                        return new BukkitArrow((org.bukkit.entity.Arrow) entity);
                    }

                    return new BukkitAbstractArrow((org.bukkit.entity.AbstractArrow) entity);
                }
            } else {
                if (entity instanceof org.bukkit.entity.Arrow) {
                    if (BukkitFeature.ENTITY_TIPPED_ARROW.isSupported()) {
                        // Trident and SpectralArrow added after TippedArrow
                        if (BukkitFeature.ENTITY_TRIDENT.isSupported()) {
                            if (entity instanceof org.bukkit.entity.Trident) {
                                return new BukkitTrident((org.bukkit.entity.Trident) entity);
                            }
                        }

                        if (BukkitFeature.ENTITY_SPECTRAL_ARROW.isSupported()) {
                            if (entity instanceof org.bukkit.entity.SpectralArrow) {
                                return new BukkitSpectralArrow((org.bukkit.entity.SpectralArrow) entity);
                            }
                        }

                        if (entity instanceof org.bukkit.entity.TippedArrow) {
                            return new BukkitArrow((org.bukkit.entity.TippedArrow) entity);
                        }

                        return new BukkitAbstractArrow((org.bukkit.entity.Arrow) entity);
                    } else {
                        return new BukkitArrow((org.bukkit.entity.Arrow) entity);
                    }
                }
            }

            if (entity instanceof org.bukkit.entity.ThrowableProjectile) {
                if (entity instanceof org.bukkit.entity.Egg) {
                    return new BukkitEgg((org.bukkit.entity.Egg) entity);
                }
                if (entity instanceof org.bukkit.entity.EnderPearl) {
                    return new BukkitEnderPearl((org.bukkit.entity.EnderPearl) entity);
                }
                if (entity instanceof org.bukkit.entity.ThrownExpBottle) {
                    return new BukkitExperienceBottle((org.bukkit.entity.ThrownExpBottle) entity);
                }
                if (entity instanceof org.bukkit.entity.ThrownPotion) {
                    return new BukkitThrownPotion((org.bukkit.entity.ThrownPotion) entity);
                }
                if (entity instanceof org.bukkit.entity.Snowball) {
                    return new BukkitSnowball((org.bukkit.entity.Snowball) entity);
                }

                return new BukkitThrowableProjectileEntity((org.bukkit.entity.ThrowableProjectile) entity);
            }

            if (entity instanceof org.bukkit.entity.FishHook) {
                return new BukkitFishingBobber((org.bukkit.entity.FishHook) entity);
            }

            if (BukkitFeature.ENTITY_LLAMA_SPIT.isSupported()) {
                if (entity instanceof org.bukkit.entity.LlamaSpit) {
                    return new BukkitLlamaSpit((org.bukkit.entity.LlamaSpit) entity);
                }
            }

            if (entity instanceof org.bukkit.entity.Firework) {
                return new BukkitFireworkRocket((org.bukkit.entity.Firework) entity);
            }

            if (BukkitFeature.ENTITY_SHULKER_BULLET.isSupported()) {
                if (entity instanceof org.bukkit.entity.ShulkerBullet) {
                    return new BukkitShulkerBullet((org.bukkit.entity.ShulkerBullet) entity);
                }
            }

            return new BukkitProjectileEntity((Projectile) entity);
        }

        if (entity instanceof Item) {
            return new BukkitItemEntity((Item) entity);
        }

        if (entity instanceof org.bukkit.entity.LightningStrike) {
            return new BukkitLightningStrike((org.bukkit.entity.LightningStrike) entity);
        }

        if (entity instanceof org.bukkit.entity.Hanging) {
            if (entity instanceof org.bukkit.entity.LeashHitch) {
                return new BukkitLeashKnot((org.bukkit.entity.LeashHitch) entity);
            }
            if (entity instanceof org.bukkit.entity.Painting) {
                return new BukkitPainting((org.bukkit.entity.Painting) entity);
            }
            if (entity instanceof org.bukkit.entity.ItemFrame) {
                return new BukkitItemFrame((org.bukkit.entity.ItemFrame) entity);
            }

            return new BukkitHangingEntity((org.bukkit.entity.Hanging) entity);
        }


        if (BukkitFeature.ENTITY_AREA_EFFECT_CLOUD.isSupported()) {
            if (entity instanceof org.bukkit.entity.AreaEffectCloud) {
                return new BukkitAreaEffectCloud((org.bukkit.entity.AreaEffectCloud) entity);
            }
        }

        if (entity instanceof org.bukkit.entity.EnderCrystal) {
            return new BukkitEndCrystal((org.bukkit.entity.EnderCrystal) entity);
        }

        if (entity instanceof org.bukkit.entity.FallingBlock) {
            return new BukkitFallingBlock((org.bukkit.entity.FallingBlock) entity);
        }

        if (entity instanceof org.bukkit.entity.TNTPrimed) {
            return new BukkitPrimedTnt((org.bukkit.entity.TNTPrimed) entity);
        }

        if (entity instanceof org.bukkit.entity.EnderSignal) {
            return new BukkitEyeOfEnder((org.bukkit.entity.EnderSignal) entity);
        }

        if (BukkitFeature.ENTITY_EVOKER_FANGS.isSupported()) {
            if (entity instanceof org.bukkit.entity.EvokerFangs) {
                return new BukkitEvokerFangs((org.bukkit.entity.EvokerFangs) entity);
            }
        }

        if (BukkitFeature.ENTITY_INTERACTION.isSupported()) {
            if (entity instanceof org.bukkit.entity.Interaction) {
                return new BukkitInteraction((org.bukkit.entity.Interaction) entity);
            }
        }

        if (BukkitFeature.ENTITY_MARKER.isSupported()) {
            if (entity instanceof org.bukkit.entity.Marker) {
                return new BukkitMarker((org.bukkit.entity.Marker) entity);
            }
        }

        if (BukkitFeature.ENTITY_DISPLAY.isSupported()) {
            if (entity instanceof org.bukkit.entity.Display) {
                if (entity instanceof org.bukkit.entity.TextDisplay) {
                    return new BukkitTextDisplay((org.bukkit.entity.TextDisplay) entity);
                }
                if (entity instanceof org.bukkit.entity.BlockDisplay) {
                    return new BukkitBlockDisplay((org.bukkit.entity.BlockDisplay) entity);
                }
                if (entity instanceof org.bukkit.entity.ItemDisplay) {
                    return new BukkitItemDisplay((org.bukkit.entity.ItemDisplay) entity);
                }

                return new BukkitDisplay((org.bukkit.entity.Display) entity);
            }
        }

        return new BukkitEntity((org.bukkit.entity.Entity) entity);
    }

    @Override
    public @Nullable Entity spawn0(@NotNull EntityType entityType, @NotNull Location locationHolder, @Nullable Consumer<? super @NotNull Entity> preSpawnFunction) {
        var entityType1 = entityType.asNullable(org.bukkit.entity.EntityType.class);

        if (entityType1 == null) {
            return null;
        }

        var entityClass = entityType1.getEntityClass();
        if (entityClass == null) {
            return null; // huh?
        }

        var bukkitLoc = locationHolder.as(org.bukkit.Location.class);
        var world = bukkitLoc.getWorld();
        if (world != null) {
            // TODO: test all entity types
            if (BukkitFeature.ENTITY_PRE_SPAWN_FUNCTION.isSupported()) {
                org.bukkit.entity.Entity entity;
                if (preSpawnFunction != null) {
                    entity = world.spawn(bukkitLoc, entityClass, preSpawnBukkitEntity -> preSpawnFunction.accept(wrapEntity0(preSpawnBukkitEntity)));
                } else {
                    entity = world.spawn(bukkitLoc, entityClass);
                }
                return wrapEntity0(entity);
            } else {
                var entity = world.spawn(bukkitLoc, entityClass);

                // this is not required since 1.11 and pre spawn function has been added after the release of 1.11, therefore this is not needed in the other branch of the if statement
                if (!BukkitFeature.NORMAL_ENTITY_RESOURCE_LOCATIONS.isSupported() && entityType instanceof BukkitEntityType1_8 && ((BukkitEntityType1_8) entityType).getAdditionalLegacyData() != 0) {
                    var data = ((BukkitEntityType1_8) entityType).getAdditionalLegacyData();
                    if (entity instanceof Horse) {
                        switch (data) {
                            case InternalEntityLegacyConstants.HORSE_VARIANT_DONKEY:
                                //noinspection deprecation
                                ((Horse) entity).setVariant(Horse.Variant.DONKEY);
                                break;
                            case InternalEntityLegacyConstants.HORSE_VARIANT_MULE:
                                //noinspection deprecation
                                ((Horse) entity).setVariant(Horse.Variant.MULE);
                                break;
                            case InternalEntityLegacyConstants.HORSE_VARIANT_SKELETON:
                                //noinspection deprecation
                                ((Horse) entity).setVariant(Horse.Variant.SKELETON_HORSE);
                                break;
                            case InternalEntityLegacyConstants.HORSE_VARIANT_ZOMBIE:
                                //noinspection deprecation
                                ((Horse) entity).setVariant(Horse.Variant.UNDEAD_HORSE);
                                break;
                        }
                    } else if (entity instanceof Skeleton) {
                        switch (data) {
                            case InternalEntityLegacyConstants.SKELETON_VARIANT_WITHER:
                                //noinspection deprecation
                                ((Skeleton) entity).setSkeletonType(Skeleton.SkeletonType.WITHER);
                                break;
                            case InternalEntityLegacyConstants.SKELETON_VARIANT_STRAY:
                                if (BukkitFeature.STRAY.isSupported()) {
                                    //noinspection deprecation
                                    ((Skeleton) entity).setSkeletonType(Skeleton.SkeletonType.STRAY);
                                }
                                break;
                        }
                    } else if (entity instanceof Zombie) {
                        switch (data) {
                            case InternalEntityLegacyConstants.ZOMBIE_VARIANT_VILLAGER:
                                //noinspection deprecation
                                ((Zombie) entity).setVillager(true);
                                break;
                            case InternalEntityLegacyConstants.ZOMBIE_VARIANT_HUSK:
                                if (BukkitFeature.HUSK.isSupported()) {
                                    // we need NMS to spawn Husk on 1.10, thank you md_5 -_-
                                    Reflect.fastInvoke(ZombieAccessor.getMethodSetVillagerType1(), ClassStorage.getHandle(entity), EnumZombieTypeAccessor.getFieldHUSK());
                                }
                                break;
                        }
                    } else if (entity instanceof Guardian && data == InternalEntityLegacyConstants.ELDER_GUARDIAN) {
                        //noinspection deprecation
                        ((Guardian) entity).setElder(true);
                    }
                }

                var wrappedEntity = wrapEntity0(entity);
                if (preSpawnFunction != null) {
                    preSpawnFunction.accept(wrappedEntity);
                }
                return wrappedEntity;
            }
        }
        return null;
    }

    @Override
    public @Nullable ItemEntity dropItem0(@NotNull ItemStack item, @NotNull Location locationHolder, @Nullable Consumer<? super @NotNull ItemEntity> preSpawnFunction) {
        var bukkitLoc = locationHolder.as(org.bukkit.Location.class);
        Item itemEntity;
        if (preSpawnFunction != null && BukkitFeature.ITEM_ENTITY_PRE_SPAWN_FUNCTION.isSupported()) {
            itemEntity = bukkitLoc.getWorld().dropItem(bukkitLoc, item.as(org.bukkit.inventory.ItemStack.class), item1 -> preSpawnFunction.accept(new BukkitItemEntity(item1)));
        } else {
            itemEntity = bukkitLoc.getWorld().dropItem(bukkitLoc, item.as(org.bukkit.inventory.ItemStack.class));
        }
        return new BukkitItemEntity(itemEntity);
    }

    @Override
    public @Nullable ExperienceOrb dropExperience0(int experience, @NotNull Location locationHolder, @Nullable Consumer<? super @NotNull ExperienceOrb> preSpawnFunction) {
        var bukkitLoc = locationHolder.as(org.bukkit.Location.class);
        if (BukkitFeature.ENTITY_PRE_SPAWN_FUNCTION.isSupported()) {
            return new BukkitExperienceOrb(bukkitLoc.getWorld().spawn(bukkitLoc, org.bukkit.entity.ExperienceOrb.class, experienceOrb -> {
                experienceOrb.setExperience(experience);
                if (preSpawnFunction != null) {
                    preSpawnFunction.accept(new BukkitExperienceOrb(experienceOrb));
                }
            }));
        } else {
            var orb = bukkitLoc.getWorld().spawn(bukkitLoc, org.bukkit.entity.ExperienceOrb.class);
            orb.setExperience(experience);
            return new BukkitExperienceOrb(orb);
        }
    }

    @Override
    public @Nullable LightningBolt strikeLightning0(@NotNull Location locationHolder, @Nullable Consumer<? super @NotNull LightningBolt> preSpawnFunction) {
        var bukkitLoc = locationHolder.as(org.bukkit.Location.class);
        var lightning = bukkitLoc.getWorld().strikeLightning(bukkitLoc);
        var wrapped = new BukkitLightningStrike(lightning);
        if (preSpawnFunction != null) {
            preSpawnFunction.accept(wrapped);
        }
        return wrapped;
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
            return (int) entityCount;
        }

        final var entityCounter = Reflect.getField(EntityAccessor.getFieldENTITY_COUNTER());
        if (entityCounter instanceof AtomicInteger) {
            return ((AtomicInteger) entityCounter).incrementAndGet();
        }
        throw new UnsupportedOperationException("Can't obtain new Entity id");
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Integer> getNewEntityIdSynchronously0() {
        // TODO: check how this is supposed to work on Folia
        CompletableFuture<Integer> future = new CompletableFuture<>();
        if (Server.isServerThread()) {
            future.complete(getNewEntityId());
        } else {
            Tasker.run(DefaultThreads.GLOBAL_THREAD, () -> future.complete(getNewEntityId()));
        }
        future.exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
        return future;
    }
}
