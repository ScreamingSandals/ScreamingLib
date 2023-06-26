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

import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Item;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
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
import org.screamingsandals.lib.impl.bukkit.entity.ambient.BukkitAmbientCreature;
import org.screamingsandals.lib.impl.bukkit.entity.ambient.BukkitBat;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitAnimal;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitAxolotl;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitBee;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitCat;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitChicken;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitCow;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitFox;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitFrog;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitGoat;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitHoglin;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitMooshroom;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitOcelot1_14;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitOcelot1_8;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitPanda;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitParrot;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitPig;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitPolarBear;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitRabbit;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitSheep;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitSniffer;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitStrider;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitTamable;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitTurtle;
import org.screamingsandals.lib.impl.bukkit.entity.animal.BukkitWolf;
import org.screamingsandals.lib.impl.bukkit.entity.animal.horse.BukkitCamel;
import org.screamingsandals.lib.impl.bukkit.entity.animal.horse.BukkitChestedHorseEntity1_11;
import org.screamingsandals.lib.impl.bukkit.entity.animal.horse.BukkitDonkey1_11;
import org.screamingsandals.lib.impl.bukkit.entity.animal.horse.BukkitDonkey1_8;
import org.screamingsandals.lib.impl.bukkit.entity.animal.horse.BukkitHorse1_11;
import org.screamingsandals.lib.impl.bukkit.entity.animal.horse.BukkitHorse1_8;
import org.screamingsandals.lib.impl.bukkit.entity.animal.horse.BukkitHorseEntity1_11;
import org.screamingsandals.lib.impl.bukkit.entity.animal.horse.BukkitHorseEntity1_8;
import org.screamingsandals.lib.impl.bukkit.entity.animal.horse.BukkitLlama;
import org.screamingsandals.lib.impl.bukkit.entity.animal.horse.BukkitMule1_11;
import org.screamingsandals.lib.impl.bukkit.entity.animal.horse.BukkitMule1_8;
import org.screamingsandals.lib.impl.bukkit.entity.animal.horse.BukkitSkeletonHorse1_11;
import org.screamingsandals.lib.impl.bukkit.entity.animal.horse.BukkitSkeletonHorse1_8;
import org.screamingsandals.lib.impl.bukkit.entity.animal.horse.BukkitTraderLlama;
import org.screamingsandals.lib.impl.bukkit.entity.animal.horse.BukkitZombieHorse1_11;
import org.screamingsandals.lib.impl.bukkit.entity.animal.horse.BukkitZombieHorse1_8;
import org.screamingsandals.lib.impl.bukkit.entity.flying.BukkitFlyingMob;
import org.screamingsandals.lib.impl.bukkit.entity.flying.BukkitGhast;
import org.screamingsandals.lib.impl.bukkit.entity.flying.BukkitPhantom;
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
import org.screamingsandals.lib.impl.bukkit.entity.slime.BukkitMagmaCube;
import org.screamingsandals.lib.impl.bukkit.entity.slime.BukkitSlime;
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
import org.screamingsandals.lib.impl.bukkit.entity.water.BukkitCod;
import org.screamingsandals.lib.impl.bukkit.entity.water.BukkitDolphin;
import org.screamingsandals.lib.impl.bukkit.entity.water.BukkitFish;
import org.screamingsandals.lib.impl.bukkit.entity.water.BukkitGlowSquid;
import org.screamingsandals.lib.impl.bukkit.entity.water.BukkitPufferFish;
import org.screamingsandals.lib.impl.bukkit.entity.water.BukkitSalmon;
import org.screamingsandals.lib.impl.bukkit.entity.water.BukkitSquid;
import org.screamingsandals.lib.impl.bukkit.entity.water.BukkitTadpole;
import org.screamingsandals.lib.impl.bukkit.entity.water.BukkitTropicalFish;
import org.screamingsandals.lib.impl.bukkit.entity.water.BukkitWaterAnimal;
import org.screamingsandals.lib.impl.bukkit.entity.water.BukkitWaterAnimal1_13;
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

            if (entity instanceof org.bukkit.entity.WaterMob) { // must be before Creature
                if (entity instanceof org.bukkit.entity.Squid) {
                    if (BukkitFeature.ENTITY_GLOW_SQUID.isSupported()) {
                        if (entity instanceof org.bukkit.entity.GlowSquid) {
                            return new BukkitGlowSquid((org.bukkit.entity.GlowSquid) entity);
                        }
                    }

                    return new BukkitSquid((org.bukkit.entity.Squid) entity);
                }

                if (BukkitFeature.ENTITY_FISH.isSupported()) {
                    if (entity instanceof org.bukkit.entity.Fish) {
                        if (entity instanceof org.bukkit.entity.Cod) {
                            return new BukkitCod((org.bukkit.entity.Cod) entity);
                        }
                        if (entity instanceof org.bukkit.entity.PufferFish) {
                            return new BukkitPufferFish((org.bukkit.entity.PufferFish) entity);
                        }
                        if (entity instanceof org.bukkit.entity.Salmon) {
                            return new BukkitSalmon((org.bukkit.entity.Salmon) entity);
                        }
                        if (entity instanceof org.bukkit.entity.TropicalFish) {
                            return new BukkitTropicalFish((org.bukkit.entity.TropicalFish) entity);
                        }

                        if (BukkitFeature.ENTITY_TADPOLE.isSupported()) {
                            if (entity instanceof org.bukkit.entity.Tadpole) {
                                return new BukkitTadpole((org.bukkit.entity.Tadpole) entity);
                            }
                        }

                        return new BukkitFish((org.bukkit.entity.Fish) entity);
                    }

                    if (entity instanceof org.bukkit.entity.Dolphin) {
                        return new BukkitDolphin((org.bukkit.entity.Dolphin) entity);
                    }

                    return new BukkitWaterAnimal1_13((org.bukkit.entity.WaterMob) entity);
                }

                return new BukkitWaterAnimal((org.bukkit.entity.WaterMob) entity);
            }

            if (entity instanceof org.bukkit.entity.Creature) {
                if (entity instanceof org.bukkit.entity.Animals) {
                    if (entity instanceof org.bukkit.entity.Chicken) {
                        return new BukkitChicken((org.bukkit.entity.Chicken) entity);
                    }

                    if (entity instanceof org.bukkit.entity.Cow) {
                        if (entity instanceof org.bukkit.entity.MushroomCow) {
                            return new BukkitMooshroom((org.bukkit.entity.MushroomCow) entity);
                        }
                        return new BukkitCow((org.bukkit.entity.Cow) entity);
                    }

                    if (entity instanceof org.bukkit.entity.Pig) {
                        return new BukkitPig((org.bukkit.entity.Pig) entity);
                    }

                    if (BukkitFeature.ENTITY_ABSTRACT_HORSE.isSupported()) {
                        if (entity instanceof org.bukkit.entity.AbstractHorse) {
                            if (entity instanceof org.bukkit.entity.ChestedHorse) {
                                if (entity instanceof org.bukkit.entity.Donkey) {
                                    return new BukkitDonkey1_11((org.bukkit.entity.Donkey) entity);
                                }
                                if (entity instanceof org.bukkit.entity.Mule) {
                                    return new BukkitMule1_11((org.bukkit.entity.Mule) entity);
                                }
                                if (entity instanceof org.bukkit.entity.Llama) {
                                    if (BukkitFeature.ENTITY_TRADER_LLAMA.isSupported()) {
                                        if (entity instanceof org.bukkit.entity.TraderLlama) {
                                            return new BukkitTraderLlama((org.bukkit.entity.TraderLlama) entity);
                                        }
                                    }
                                    return new BukkitLlama((org.bukkit.entity.Llama) entity);
                                }

                                return new BukkitChestedHorseEntity1_11((org.bukkit.entity.ChestedHorse) entity);
                            }
                            if (entity instanceof org.bukkit.entity.Horse) {
                                return new BukkitHorse1_11((org.bukkit.entity.Horse) entity);
                            }
                            if (entity instanceof org.bukkit.entity.SkeletonHorse) {
                                return new BukkitSkeletonHorse1_11((org.bukkit.entity.SkeletonHorse) entity);
                            }
                            if (entity instanceof org.bukkit.entity.ZombieHorse) {
                                return new BukkitZombieHorse1_11((org.bukkit.entity.ZombieHorse) entity);
                            }
                            if (BukkitFeature.ENTITY_CAMEL.isSupported()) {
                                if (entity instanceof org.bukkit.entity.Camel) {
                                    return new BukkitCamel((org.bukkit.entity.Camel) entity);
                                }
                            }

                            return new BukkitHorseEntity1_11((org.bukkit.entity.AbstractHorse) entity);
                        }
                    } else {
                        if (entity instanceof org.bukkit.entity.Horse) {
                            switch (((org.bukkit.entity.Horse) entity).getVariant()) {
                                case DONKEY:
                                    return new BukkitDonkey1_8((org.bukkit.entity.Horse) entity);
                                case MULE:
                                    return new BukkitMule1_8((org.bukkit.entity.Horse) entity);
                                case HORSE:
                                    return new BukkitHorse1_8((org.bukkit.entity.Horse) entity);
                                case SKELETON_HORSE:
                                    return new BukkitSkeletonHorse1_8((org.bukkit.entity.Horse) entity);
                                case UNDEAD_HORSE:
                                    return new BukkitZombieHorse1_8((org.bukkit.entity.Horse) entity);
                            }
                            return new BukkitHorseEntity1_8((org.bukkit.entity.Horse) entity);
                        }
                    }

                    if (entity instanceof org.bukkit.entity.Tameable) {
                        if (entity instanceof org.bukkit.entity.Wolf) {
                            return new BukkitWolf((org.bukkit.entity.Wolf) entity);
                        }
                        if (BukkitFeature.ENTITY_PARROT.isSupported()) {
                            if (entity instanceof org.bukkit.entity.Parrot) {
                                return new BukkitParrot((org.bukkit.entity.Parrot) entity);
                            }
                        }
                        if (BukkitFeature.ENTITY_CAT.isSupported()) {
                            if (entity instanceof org.bukkit.entity.Cat) {
                                return new BukkitCat((org.bukkit.entity.Cat) entity);
                            }
                        } else {
                            if (entity instanceof org.bukkit.entity.Ocelot) {
                                return new BukkitOcelot1_8((org.bukkit.entity.Tameable) entity);
                            }
                        }

                        return new BukkitTamable((org.bukkit.entity.Tameable) entity);
                    }

                    if (entity instanceof org.bukkit.entity.Sheep) {
                        return new BukkitSheep((org.bukkit.entity.Sheep) entity);
                    }

                    if (entity instanceof org.bukkit.entity.Rabbit) {
                        return new BukkitRabbit((org.bukkit.entity.Rabbit) entity);
                    }

                    if (BukkitFeature.ENTITY_POLAR_BEAR.isSupported()) {
                        if (entity instanceof org.bukkit.entity.PolarBear) {
                            return new BukkitPolarBear((org.bukkit.entity.PolarBear) entity);
                        }
                    }

                    if (BukkitFeature.ENTITY_TURTLE.isSupported()) {
                        if (entity instanceof org.bukkit.entity.Turtle) {
                            return new BukkitTurtle((org.bukkit.entity.Turtle) entity);
                        }
                    }

                    if (entity instanceof org.bukkit.entity.Ocelot) {
                        return new BukkitOcelot1_14((org.bukkit.entity.Ocelot) entity);
                    }

                    if (BukkitFeature.ENTITY_PANDA.isSupported()) {
                        if (entity instanceof org.bukkit.entity.Panda) {
                            return new BukkitPanda((org.bukkit.entity.Panda) entity);
                        }
                    }

                    if (BukkitFeature.ENTITY_FOX.isSupported()) {
                        if (entity instanceof org.bukkit.entity.Fox) {
                            return new BukkitFox((org.bukkit.entity.Fox) entity);
                        }
                    }

                    if (BukkitFeature.ENTITY_BEE.isSupported()) {
                        if (entity instanceof org.bukkit.entity.Bee) {
                            return new BukkitBee((org.bukkit.entity.Bee) entity);
                        }
                    }

                    if (BukkitFeature.ENTITY_HOGLIN.isSupported()) {
                        if (entity instanceof org.bukkit.entity.Hoglin) {
                            return new BukkitHoglin((org.bukkit.entity.Hoglin) entity);
                        }
                    }

                    if (BukkitFeature.ENTITY_STRIDER.isSupported()) {
                        if (entity instanceof org.bukkit.entity.Strider) {
                            return new BukkitStrider((org.bukkit.entity.Strider) entity);
                        }
                    }

                    if (BukkitFeature.ENTITY_AXOLOTL.isSupported()) {
                        if (entity instanceof org.bukkit.entity.Axolotl) {
                            return new BukkitAxolotl((org.bukkit.entity.Axolotl) entity);
                        }
                    }

                    if (BukkitFeature.ENTITY_GOAT.isSupported()) {
                        if (entity instanceof org.bukkit.entity.Goat) {
                            return new BukkitGoat((org.bukkit.entity.Goat) entity);
                        }
                    }

                    if (BukkitFeature.ENTITY_FROG.isSupported()) {
                        if (entity instanceof org.bukkit.entity.Frog) {
                            return new BukkitFrog((org.bukkit.entity.Frog) entity);
                        }
                    }

                    if (BukkitFeature.ENTITY_SNIFFER.isSupported()) {
                        if (entity instanceof org.bukkit.entity.Sniffer) {
                            return new BukkitSniffer((org.bukkit.entity.Sniffer) entity);
                        }
                    }

                    return new BukkitAnimal((org.bukkit.entity.Animals) entity);
                }

                // TODO

                return new BukkitPathfinderMob((org.bukkit.entity.Creature) entity);
            }

            if (entity instanceof org.bukkit.entity.Slime) {
                if (entity instanceof org.bukkit.entity.MagmaCube) {
                    return new BukkitMagmaCube((org.bukkit.entity.MagmaCube) entity);
                }

                return new BukkitSlime((org.bukkit.entity.Slime) entity);
            }

            if (entity instanceof org.bukkit.entity.Flying) {
                if (entity instanceof org.bukkit.entity.Ghast) {
                    return new BukkitGhast((org.bukkit.entity.Ghast) entity);
                }

                if (BukkitFeature.ENTITY_PHANTOM.isSupported()) {
                    if (entity instanceof org.bukkit.entity.Phantom) {
                        return new BukkitPhantom((org.bukkit.entity.Phantom) entity);
                    }
                }

                return new BukkitFlyingMob((org.bukkit.entity.Flying) entity);
            }

            if (entity instanceof org.bukkit.entity.Ambient) {
                if (entity instanceof org.bukkit.entity.Bat) {
                    return new BukkitBat((org.bukkit.entity.Bat) entity);
                }

                return new BukkitAmbientCreature((org.bukkit.entity.Ambient) entity);
            }

            if (BukkitFeature.MOB_INTERFACE.isSupported()) {
                if (entity instanceof Mob) {
                    return new BukkitMob((org.bukkit.entity.LivingEntity) entity);
                }
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
