/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.bukkit.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.*;

public class BukkitEntityMetadataMapper {

    private static final Map<Class<? extends Entity>, Map<String, BukkitMetadata<?, ?>>> METADATA = new HashMap<>();

    // TODO: it would be better to generate this static initializer or maybe find a better way then manually doing this shit (because of fucking modded servers)
    static {
        var breedableExist = Reflect.has("org.bukkit.entity.Breedable");
        var steerableExist = Reflect.has("org.bukkit.entity.Steerable");
        var spellcasterExist = Reflect.has("org.bukkit.entity.Spellcaster");

        if (Reflect.has("org.bukkit.entity.AbstractHorse")) {
            Builder.begin(AbstractHorse.class)
                    .map("domestication", Integer.class, AbstractHorse::getDomestication, AbstractHorse::setDomestication)
                    .map("max_domestication", Integer.class, AbstractHorse::getMaxDomestication, AbstractHorse::setMaxDomestication)
                    .map("jump_strength", Double.class, AbstractHorse::getJumpStrength, AbstractHorse::setJumpStrength);
        }

        Builder.begin(Ageable.class) // TODO: Fix for Piglin & Zoglin are not Ageable in early 1.16 spigot days
                .map("is_baby", "data_baby_id", Boolean.class, ageable -> !ageable.isAdult(), (ageable, aBoolean) -> {
                    if (aBoolean) {
                        ageable.setBaby();
                    } else {
                        ageable.setAdult();
                    }
                })
                .map("age", Integer.class, Ageable::getAge, Ageable::setAge)
                .when(!breedableExist, b -> b
                        .map("breed", Boolean.class, Ageable::canBreed, Ageable::setBreed)
                        .map("ageLock", Boolean.class, Ageable::getAgeLock, Ageable::setAgeLock)
                );

        // TODO: Animals? (not rly metadata)

        Builder.begin(AreaEffectCloud.class)
                .map("radius", "data_radius", Float.class, AreaEffectCloud::getRadius, AreaEffectCloud::setRadius)
                .map("color", "data_color", Color.class, AreaEffectCloud::getColor, AreaEffectCloud::setColor);
        // TODO: data_waiting BOOLEAN
        // TODO: data_particle PARTICLE

        Builder.begin(ArmorStand.class)
                .map("small", Boolean.class, ArmorStand::isSmall, ArmorStand::setSmall)
                .map("arms", Boolean.class, ArmorStand::hasArms, ArmorStand::setArms)
                .map("base_plate", Boolean.class, ArmorStand::hasBasePlate, ArmorStand::setBasePlate)
                .map("marker", Boolean.class, ArmorStand::isMarker, ArmorStand::setMarker)

                .map("head_pose", "data_head_pose", EulerAngle.class, ArmorStand::getHeadPose, ArmorStand::setHeadPose)
                .map("body_pose", "data_body_pose", EulerAngle.class, ArmorStand::getBodyPose, ArmorStand::setBodyPose)
                .map("left_arm_pose", "data_left_arm_pose", EulerAngle.class, ArmorStand::getLeftArmPose, ArmorStand::setLeftArmPose)
                .map("right_arm_pose", "data_right_arm_pose", EulerAngle.class, ArmorStand::getRightArmPose, ArmorStand::setRightArmPose)
                .map("left_leg_pose", "data_left_leg_pose", EulerAngle.class, ArmorStand::getLeftLegPose, ArmorStand::setLeftLegPose)
                .map("right_leg_pose", "data_right_leg_pose", EulerAngle.class, ArmorStand::getRightLegPose, ArmorStand::setRightLegPose);

        Builder.begin(Arrow.class)
                .map("effect_color", "id_effect_color", Color.class, Arrow::getColor, Arrow::setColor);

        if (Reflect.has("org.bukkit.entity.Axolotl")) {
            Builder.begin(Axolotl.class)
                    .map("variant", "data_variant", Axolotl.Variant.class, Axolotl::getVariant, Axolotl::setVariant)
                    .map("playing_dead", "data_playing_dead", Boolean.class, Axolotl::isPlayingDead, Axolotl::setPlayingDead);
            // TODO: from_bucket BOOLEAN (not present in Bukkit api)
        }

        Builder.begin(Bat.class)
                .map("awake", Boolean.class, Bat::isAwake, Bat::setAwake)

                .map("is_hanging", Boolean.class, bat -> !bat.isAwake(), (bat, aBoolean) -> bat.setAwake(!aBoolean))
                .map("flags", "data_id_flags", Byte.class, bat -> (byte) (bat.isAwake() ? 0x0 : 0x1), (bat, aByte) -> bat.setAwake((aByte & 0x1) == 0x1));

        if (Reflect.has("org.bukkit.entity.Bee")) {
            Builder.begin(Bee.class)
                    .map("hive", Location.class, Bee::getHive, Bee::setHive)
                    .map("flower", Location.class, Bee::getFlower, Bee::setFlower)
                    .map("has_nectar", Boolean.class, Bee::hasNectar, Bee::setHasNectar)
                    .when(() -> Reflect.hasMethod(Bee.class, "getCannotEnterHiveTicks"), b -> b
                            .map("remaining_anger_time", "data_remaining_anger_time", Integer.class, Bee::getAnger, Bee::setAnger)
                            .map("cannot_enter_hive_ticks", Integer.class, Bee::getCannotEnterHiveTicks, Bee::setCannotEnterHiveTicks)
                    );
        }

        Builder.begin(Boat.class)
                .map("wood_type", "data_id_type", TreeSpecies.class, Boat::getWoodType, Boat::setWoodType);

        if (breedableExist) {
            Builder.begin(Breedable.class)
                    .map("breed", Boolean.class, Breedable::canBreed, Breedable::setBreed)
                    .map("ageLock", Boolean.class, Breedable::getAgeLock, Breedable::setAgeLock);
        }

        if (Reflect.has("org.bukkit.entity.Cat")) {
            Builder.begin(Cat.class)
                    .map("cat_type", "data_type_id", Cat.Type.class, Cat::getCatType, Cat::setCatType)
                    .map("collar_color", "data_collar_color", DyeColor.class, Cat::getCollarColor, Cat::setCollarColor);
        }

        if (Reflect.has("org.bukkit.entity.ChestedHorse")) {
            Builder.begin(ChestedHorse.class)
                    .map("is_carrying_chest", "data_has_chat", Boolean.class, ChestedHorse::isCarryingChest, ChestedHorse::setCarryingChest);
        }

        Builder.begin(Creeper.class)
                .map("powered", "data_is_powered", Boolean.class, Creeper::isPowered, Creeper::setPowered)
                .when(Reflect.hasMethod(Creeper.class, "getMaxFuseTicks"), b -> b
                        .map("max_fuse_ticks", Integer.class, Creeper::getMaxFuseTicks, Creeper::setMaxFuseTicks)
                        .map("explosion_radius", Integer.class, Creeper::getExplosionRadius, Creeper::setExplosionRadius)
                        .when(Reflect.hasMethod(Creeper.class, "getFuseTicks"), b2 -> b2
                                .map("fuse_ticks", Integer.class, Creeper::getFuseTicks, Creeper::setFuseTicks)
                        )
                );

        Builder.begin(EnderCrystal.class)
                .map("showing_bottom", "data_show_bottom", Boolean.class, EnderCrystal::isShowingBottom, EnderCrystal::setShowingBottom)
                .map("beam_target", "data_beam_target", Location.class, EnderCrystal::getBeamTarget, EnderCrystal::setBeamTarget);

        Builder.begin(EnderDragon.class)
                .map("phase", "data_phase", EnderDragon.Phase.class, EnderDragon::getPhase, EnderDragon::setPhase);

        Builder.begin(Enderman.class)
                .whenNot(1, 13, b -> b
                        .map("carried_block", MaterialData.class, Enderman::getCarriedMaterial, Enderman::setCarriedMaterial)
                )
                .when(1, 13, b -> b
                        .map("carried_block", BlockData.class, Enderman::getCarriedBlock, Enderman::setCarriedBlock)
                );

        // TODO: Eye of Ender (EnderSignal)

        if (!spellcasterExist) {
            Builder.begin(Evoker.class)
                    .map("spell", Evoker.Spell.class, Evoker::getCurrentSpell, Evoker::setCurrentSpell);
        }

        if (Reflect.has("org.bukkit.entity.EvokerFangs")) {
            Builder.begin(EvokerFangs.class)
                    .map("owner", LivingEntity.class, EvokerFangs::getOwner, EvokerFangs::setOwner);
        }

        Builder.begin(Explosive.class)
                .map("is_incendiary", Boolean.class, Explosive::isIncendiary, Explosive::setIsIncendiary)
                .map("yield", Float.class, Explosive::getYield, Explosive::setYield);

        Builder.begin(FallingBlock.class)
                .map("drop_item", Boolean.class, FallingBlock::getDropItem, FallingBlock::setDropItem)
                .map("can_hurt_entities", Boolean.class, FallingBlock::canHurtEntities, FallingBlock::setHurtEntities)
                .when(1, 13, b -> b
                        .map("block_data", BlockData.class, FallingBlock::getBlockData, null) // not mutable in bukkit
                )
                .whenNot(1, 13, b -> b
                        .map("block_data", Material.class, FallingBlock::getMaterial, null) // not mutable in bukkit
                );

        Builder.begin(Fireball.class)
                .map("direction", Vector.class, Fireball::getDirection, Fireball::setDirection);

        // TODO: Fish hook

        if (Reflect.has("org.bukkit.entity.Fox")) {
            Builder.begin(Fox.class)
                    .map("fox_type", Fox.Type.class, Fox::getFoxType, Fox::setFoxType)
                    .map("is_crouching", Boolean.class, Fox::isCrouching, Fox::setCrouching)
                    .map("is_sleeping", Boolean.class, Fox::isSleeping, Fox::setSleeping)
                    .when(Reflect.hasMethod(Fox.class, "getFirstTrustedPlayer"), b -> b
                            .map("first_trusted_player", UUID.class, fox -> {
                                var trusted = fox.getFirstTrustedPlayer();
                                if (trusted != null) {
                                    return trusted.getUniqueId();
                                } else {
                                    return null;
                                }
                            }, (fox, uuid) -> fox.setFirstTrustedPlayer(uuid == null ? null : Bukkit.getOfflinePlayer(uuid)))
                            .map("second_trusted_player", UUID.class, fox -> {
                                var trusted = fox.getSecondTrustedPlayer();
                                if (trusted != null) {
                                    return trusted.getUniqueId();
                                } else {
                                    return null;
                                }
                            }, (fox, uuid) -> fox.setSecondTrustedPlayer(uuid == null ? null : Bukkit.getOfflinePlayer(uuid)))
                    );
        }

        if (Reflect.has("org.bukkit.entity.GlowSquid")) {
            Builder.begin(GlowSquid.class)
                    .map("dark_ticks_remaining", "data_dark_ticks_remaining", Integer.class, GlowSquid::getDarkTicksRemaining, GlowSquid::setDarkTicksRemaining);
        }

        if (Reflect.has("org.bukkit.entity.Goat")) {
            Builder.begin(Goat.class)
                    .map("screaming", "data_is_screaming_goat", Boolean.class, Goat::isScreaming, Goat::setScreaming);
        }

        Builder.begin(Guardian.class)
                .map("has_laser", Boolean.class, Guardian::hasLaser, Guardian::setLaser);

        if (Reflect.has("org.bukkit.entity.Hoglin")) {
            Builder.begin(Hoglin.class)
                    .map("is_immune_to_zombification", Boolean.class, Hoglin::isImmuneToZombification, Hoglin::setImmuneToZombification)
                    .map("is_able_to_be_hunted", Boolean.class, Hoglin::isAbleToBeHunted, Hoglin::setIsAbleToBeHunted)
                    .when(Reflect.hasMethod(Hoglin.class, "isConverting"), b -> b
                            .map("is_converting", Boolean.class, Hoglin::isConverting, null)
                            .map("conversion_time", Integer.class, Hoglin::getConversionTime, Hoglin::setConversionTime)
                    );
        }

        Builder.begin(Horse.class)
                .map("color", Horse.Color.class, Horse::getColor, Horse::setColor)
                .map("style", Horse.Style.class, Horse::getStyle, Horse::setStyle)
                .whenNot(1, 11, b -> b
                        .map("variant", Horse.Variant.class, Horse::getVariant, Horse::setVariant)
                        .map("is_carrying_chest", Boolean.class, Horse::isCarryingChest, Horse::setCarryingChest)
                        .map("domestication", Integer.class, Horse::getDomestication, Horse::setDomestication)
                        .map("max_domestication", Integer.class, Horse::getMaxDomestication, Horse::setMaxDomestication)
                        .map("jump_strength", Double.class, Horse::getJumpStrength, Horse::setJumpStrength)
                );

        if (Reflect.has("org.bukkit.entity.Husk")) {
            Builder.begin(Husk.class)
                    .when(Reflect.hasMethod(Husk.class, "isConverting"), b -> b
                            .map("is_converting", Boolean.class, Husk::isConverting, null)
                            .map("conversion_time", Integer.class, Husk::getConversionTime, Husk::setConversionTime)
                    );
        }

        Builder.begin(IronGolem.class)
                .map("is_player_created", Boolean.class, IronGolem::isPlayerCreated, IronGolem::setPlayerCreated);

        Builder.begin(ItemFrame.class)
                .map("item", ItemStack.class, ItemFrame::getItem, ItemFrame::setItem)
                .map("rotation", Rotation.class, ItemFrame::getRotation, ItemFrame::setRotation)
                .when(Reflect.hasMethod(ItemFrame.class, "isVisible"), b -> b
                        .map("visible", Boolean.class, ItemFrame::isVisible, ItemFrame::setVisible)
                        .map("fixed", Boolean.class, ItemFrame::isFixed, ItemFrame::setFixed)
                )
                .when(Reflect.hasMethod(ItemFrame.class, "getItemDropChance"), b -> b
                        .map("item_drop_chance", Float.class, ItemFrame::getItemDropChance, ItemFrame::setItemDropChance)
                );

        if (Reflect.has("org.bukkit.entity.Llama")) {
            Builder.begin(Llama.class)
                    .map("color", Llama.Color.class, Llama::getColor, Llama::setColor)
                    .map("strength", Integer.class, Llama::getStrength, Llama::setStrength);
        }

        Builder.begin(Minecart.class)
                .map("damage", Double.class, Minecart::getDamage, Minecart::setDamage)
                .map("max_speed", Double.class, Minecart::getMaxSpeed, Minecart::setMaxSpeed)
                .map("is_slow_when_empty", Boolean.class, Minecart::isSlowWhenEmpty, Minecart::setSlowWhenEmpty)
                .map("flying_velocity_mod", Vector.class, Minecart::getFlyingVelocityMod, Minecart::setFlyingVelocityMod)
                .map("derailed_velocity_mod", Vector.class, Minecart::getDerailedVelocityMod, Minecart::setDerailedVelocityMod)
                .map("display_block_offset", Integer.class, Minecart::getDisplayBlockOffset, Minecart::setDisplayBlockOffset)
                .whenNot(1, 13, b -> b
                        .map("display_block_data", MaterialData.class, Minecart::getDisplayBlock, Minecart::setDisplayBlock)
                )
                .when(1, 13, b -> b
                        .map("display_block_data", BlockData.class, Minecart::getDisplayBlockData, Minecart::setDisplayBlockData)
                );

        Builder.begin(CommandMinecart.class)
                .map("command", String.class, CommandMinecart::getCommand, CommandMinecart::setCommand);

        Builder.begin(HopperMinecart.class)
                .map("is_enabled", Boolean.class, HopperMinecart::isEnabled, HopperMinecart::setEnabled);

        if (Reflect.hasMethod(PoweredMinecart.class, "getFuel")) {
            Builder.begin(PoweredMinecart.class)
                    .map("fuel", Integer.class, PoweredMinecart::getFuel, PoweredMinecart::setFuel);
        }

        if (Reflect.hasMethod(MushroomCow.class, "getVariant")) {
            Builder.begin(MushroomCow.class)
                    .map("variant", "data_type", MushroomCow.Variant.class, MushroomCow::getVariant, MushroomCow::setVariant);
        }

        Builder.begin(Ocelot.class)
                .whenNot(1, 14, b -> b
                        .map("cat_type", Ocelot.Type.class, Ocelot::getCatType, Ocelot::setCatType)
                )
                .when(Reflect.hasMethod(Ocelot.class, "isTrusting"), b -> b
                        .map("is_trusting", Boolean.class, Ocelot::isTrusting, Ocelot::setTrusting)
                );

        Builder.begin(Painting.class)
                .map("art", Art.class, Painting::getArt, Painting::setArt);

        if (Reflect.has("org.bukkit.entity.Panda")) {
            Builder.begin(Panda.class)
                    .map("main_gene", "main_gene_id", Panda.Gene.class, Panda::getMainGene, Panda::setMainGene)
                    .map("hidden_gene", "hidden_gene_id", Panda.Gene.class, Panda::getHiddenGene, Panda::setHiddenGene);
        }

        if (Reflect.has("org.bukkit.entity.Parrot")) {
            Builder.begin(Parrot.class)
                    .map("variant", "data_variant_id", Parrot.Variant.class, Parrot::getVariant, Parrot::setVariant);
        }

        if (Reflect.has("org.bukkit.entity.Phantom")) {
            Builder.begin(Phantom.class)
                    .map("size", "id_size", Integer.class, Phantom::getSize, Phantom::setSize);
        }

        if (!steerableExist) {
            Builder.begin(Pig.class)
                    .map("has_saddle", Boolean.class, Pig::hasSaddle, Pig::setSaddle);
        }

        if (Reflect.has("org.bukkit.entity.Piglin")) {
            Builder.begin(Piglin.class)
                    .map("is_able_to_hunt", Boolean.class, Piglin::isAbleToHunt, Piglin::setIsAbleToHunt)
                    .whenNot(breedableExist, b -> b
                            .map("is_baby", "data_baby_id", Boolean.class, Piglin::isBaby, Piglin::setBaby)
                    )
                    .whenNot(1, 16, 2, b -> b
                        .map("is_immune_to_zombification", Boolean.class, Piglin::isImmuneToZombification, Piglin::setImmuneToZombification)
                        .when(Reflect.hasMethod(Piglin.class, "isConverting"), b2 -> b2
                                .map("is_converting", Boolean.class, Piglin::isConverting, null)
                                .map("conversion_time", Integer.class, Piglin::getConversionTime, Piglin::setConversionTime)
                        )
                    );
        }

        if (Reflect.has("org.bukkit.entity.PiglinAbstract")) {
            Builder.begin(PiglinAbstract.class)
                    .whenNot(breedableExist, b -> b
                            .map("is_baby", "data_baby_id", Boolean.class, PiglinAbstract::isBaby, PiglinAbstract::setBaby)
                    )
                    .map("is_immune_to_zombification", Boolean.class, PiglinAbstract::isImmuneToZombification, PiglinAbstract::setImmuneToZombification)
                    .when(Reflect.hasMethod(Piglin.class, "isConverting"), b2 -> b2
                            .map("is_converting", Boolean.class, PiglinAbstract::isConverting, null)
                            .map("conversion_time", Integer.class, PiglinAbstract::getConversionTime, PiglinAbstract::setConversionTime)
                    );
        }

        Builder.begin(PigZombie.class)
                .map("anger_level", Integer.class, PigZombie::getAnger, PigZombie::setAnger)
                .map("is_angry", Boolean.class, PigZombie::isAngry, PigZombie::setAngry);

        if (Reflect.has("org.bukkit.entity.PufferFish")) {
            Builder.begin(PufferFish.class)
                    .map("puff_state", Integer.class, PufferFish::getPuffState, PufferFish::setPuffState);
        }

        Builder.begin(Rabbit.class)
                .map("rabbit_type", "data_type_id", Rabbit.Type.class, Rabbit::getRabbitType, Rabbit::setRabbitType);

        if (Reflect.has("org.bukkit.entity.Raider")) {
            Builder.begin(Raider.class)
                    .map("patrol_target", Location.class, raider -> {
                        var target = raider.getPatrolTarget();
                        if (target != null) {
                            return raider.getPatrolTarget().getLocation();
                        } else {
                            return null;
                        }
                    }, (raider, location) -> raider.setPatrolTarget(location == null ? null : location.getBlock()))
                    .map("is_patrol_leader", Boolean.class, Raider::isPatrolLeader, Raider::setPatrolLeader)
                    .when(Reflect.hasMethod(Raider.class, "isCanJoinRaid"), b -> b
                            .map("can_join_raid", Boolean.class, Raider::isCanJoinRaid, Raider::setCanJoinRaid)
                    );
        }

        Builder.begin(Sheep.class)
                .map("color", DyeColor.class, Sheep::getColor, Sheep::setColor)
                .map("sheared", Boolean.class, Sheep::isSheared, Sheep::setSheared);

        // TODO: Shulker

        Builder.begin(ShulkerBullet.class)
                .map("target", Entity.class, ShulkerBullet::getTarget, ShulkerBullet::setTarget);

        // TODO: Sittable (why it doesn't extend Entity?)

        if (Reflect.has("org.bukkit.entity.SizedFireball")) {
            Builder.begin(SizedFireball.class)
                    .map("display_item", ItemStack.class, SizedFireball::getDisplayItem, SizedFireball::setDisplayItem);
        }

        Builder.begin(Skeleton.class)
                .whenNot(1, 11, b -> b
                        .map("skeleton_type", Skeleton.SkeletonType.class, Skeleton::getSkeletonType, Skeleton::setSkeletonType)
                )
                .when(Reflect.hasMethod(Skeleton.class, "isConverting"), b -> b
                        .map("is_converting", Boolean.class, Skeleton::isConverting, null)
                        .map("conversion_time", Integer.class, Skeleton::getConversionTime, Skeleton::setConversionTime)
                );

        Builder.begin(Slime.class)
                .map("size", "id_size", Integer.class, Slime::getSize, Slime::setSize);


        if (Reflect.hasMethod(Snowman.class, "isDerp")) {
            Builder.begin(Snowman.class)
                    .map("derp", Boolean.class, Snowman::isDerp, Snowman::setDerp);
        }

        Builder.begin(SpectralArrow.class)
                .map("glowing_ticks", Integer.class, SpectralArrow::getGlowingTicks, SpectralArrow::setGlowingTicks);

        if (spellcasterExist) {
            Builder.begin(Spellcaster.class)
                    .map("spell", Spellcaster.Spell.class, Spellcaster::getSpell, Spellcaster::setSpell);
        }

        if (steerableExist) {
            Builder.begin(Steerable.class)
                    .map("has_saddle", Boolean.class, Steerable::hasSaddle, Steerable::setSaddle)
                    .map("boost_ticks", Integer.class, Steerable::getBoostTicks, Steerable::setBoostTicks)
                    .map("current_boost_ticks", Integer.class, Steerable::getCurrentBoostTicks, Steerable::setCurrentBoostTicks)
                    .map("steer_material", Material.class, Steerable::getSteerMaterial, null);
        }

        if (Reflect.has("org.bukkit.entity.Strider")) {
            Builder.begin(Strider.class)
                    .map("shivering", "data_suffocating", Boolean.class, Strider::isShivering, Strider::setShivering);
        }

        Builder.begin(Tameable.class)
                .map("is_tamed", Boolean.class, Tameable::isTamed, Tameable::setTamed)
                .map("owner", UUID.class, animal -> {
                    var trusted = animal.getOwner();
                    if (trusted != null) {
                        return trusted.getUniqueId();
                    } else {
                        return null;
                    }
                }, (animal, uuid) -> animal.setOwner(uuid == null ? null : Bukkit.getOfflinePlayer(uuid)));

        // TODO: Thrown Potion

        var ss = Reflect.hasMethod(TNTPrimed.class, "setSource");
        Builder.begin(TNTPrimed.class)
                .map("fuse_ticks", "data_fuse_id", Integer.class, TNTPrimed::getFuseTicks, TNTPrimed::setFuseTicks)
                .whenNot(ss, b -> b
                        .map("source", Entity.class, TNTPrimed::getSource, null)
                )
                .when(ss, b -> b
                        .map("source", Entity.class, TNTPrimed::getSource, TNTPrimed::setSource)
                );

        if (Reflect.has("org.bukkit.entity.TropicalFish")) {
            Builder.begin(TropicalFish.class)
                    .map("pattern_color", DyeColor.class, TropicalFish::getPatternColor, TropicalFish::setPatternColor)
                    .map("body_color", DyeColor.class, TropicalFish::getBodyColor, TropicalFish::setBodyColor)
                    .map("pattern", TropicalFish.Pattern.class, TropicalFish::getPattern, TropicalFish::setPattern);
        }

        if (Reflect.has("org.bukkit.entity.Vex") && Reflect.hasMethod("org.bukkit.entity.Vex", "isCharging")) {
            Builder.begin(Vex.class)
                    .map("is_charging", "data_is_charging", Boolean.class, Vex::isCharging, Vex::setCharging);
        }

        Builder.begin(Villager.class)
                .map("villager_profession", Villager.Profession.class, Villager::getProfession, Villager::setProfession)
                .when(1, 14, b -> b
                        .map("villager_type", Villager.Type.class, Villager::getVillagerType, Villager::setVillagerType)
                        .map("villager_level", Integer.class, Villager::getVillagerLevel, Villager::setVillagerLevel)
                        .map("villager_experience", Integer.class, Villager::getVillagerExperience, Villager::setVillagerExperience)
                );

        if (Reflect.has("org.bukkit.entity.WanderingTrader") && Reflect.hasMethod("org.bukkit.entity.WanderingTrader", "isCharging")) {
            Builder.begin(WanderingTrader.class)
                    .map("despawn_delay", Integer.class, WanderingTrader::getDespawnDelay, WanderingTrader::setDespawnDelay);
        }

        Builder.begin(WitherSkull.class)
                .map("is_charged", "data_dangerous", Boolean.class, WitherSkull::isCharged, WitherSkull::setCharged);

        Builder.begin(Wolf.class)
                .map("is_angry", Boolean.class, Wolf::isAngry, Wolf::setAngry)
                .map("collar_color", "data_collar_color", DyeColor.class, Wolf::getCollarColor, Wolf::setCollarColor);

        Builder.begin(Zombie.class)
                .whenNot(breedableExist, b -> b
                        .map("is_baby", "data_baby_id", Boolean.class, Zombie::isBaby, Zombie::setBaby)
                )
                .whenNot(1, 11, b -> b
                        .map("is_villager", Boolean.class, Zombie::isVillager, Zombie::setVillager)
                        .map("villager_profession", Villager.Profession.class, Zombie::getVillagerProfession, Zombie::setVillagerProfession)
                )
                .when(Reflect.hasMethod(Zombie.class, "isConverting"), b -> b
                        .map("is_converting", Boolean.class, Zombie::isConverting, null)
                        .map("conversion_time", Integer.class, Zombie::getConversionTime, Zombie::setConversionTime)
                );

        if (Reflect.has("org.bukkit.entity.ZombieVillager")) {
            Builder.begin(ZombieVillager.class)
                    .map("villager_profession", Villager.Profession.class, ZombieVillager::getVillagerProfession, ZombieVillager::setVillagerProfession)
                    .when(Reflect.hasMethod(ZombieVillager.class, "isConverting"), b -> b
                            .map("is_converting", Boolean.class, Zombie::isConverting, null)
                            .map("conversion_time", Integer.class, Zombie::getConversionTime, Zombie::setConversionTime)
                    )
                    .when(Reflect.hasMethod(ZombieVillager.class, "getConversionPlayer"), b -> b
                            .map("conversion_player", UUID.class, zombie -> {
                                var player = zombie.getConversionPlayer();
                                if (player != null) {
                                    return player.getUniqueId();
                                } else {
                                    return null;
                                }
                            }, (zombieVillager, uuid) -> zombieVillager.setConversionPlayer(uuid == null ? null : Bukkit.getOfflinePlayer(uuid)))
                    )
                    .when(Reflect.hasMethod(ZombieVillager.class, "getVillagerType"), b -> b
                            .map("villager_type", Villager.Type.class, ZombieVillager::getVillagerType, ZombieVillager::setVillagerType)
                    );

        }
    }

    public static boolean has(Entity entity, String metadata) {
        final var finalMetadata = metadata.toLowerCase();
        return METADATA.entrySet().stream()
                .anyMatch(classMapEntry -> classMapEntry.getKey().isInstance(entity) && classMapEntry.getValue().containsKey(finalMetadata));
    }

    @SuppressWarnings("unchecked")
    public static void set(Entity entity, String metadata, Object value) {
        final var finalMetadata = metadata.toLowerCase();
        var bukkitMetadata = METADATA.entrySet().stream()
                .filter(classMapEntry -> classMapEntry.getKey().isInstance(entity) && classMapEntry.getValue().containsKey(finalMetadata))
                .map(classMapEntry -> classMapEntry.getValue().get(finalMetadata))
                .findFirst()
                .orElseThrow();

        if (bukkitMetadata.setter == null) {
            throw new UnsupportedOperationException("Can't change an immutable metadata value!");
        }

        if (value instanceof Wrapper) {
            var opt = ((Wrapper) value).asOptional(bukkitMetadata.valueClass);
            if (opt.isPresent()) {
                value = opt.get();
            }
        }

        if (bukkitMetadata.valueClass == EulerAngle.class) {
            if (value instanceof Vector3D) {
                value = new EulerAngle(((Vector3D) value).getX(), ((Vector3D) value).getY(), ((Vector3D) value).getZ());
            } else if (value instanceof Vector3Df) {
                value = new EulerAngle(((Vector3Df) value).getX(), ((Vector3Df) value).getY(), ((Vector3Df) value).getZ());
            }
        } else if (bukkitMetadata.valueClass == Vector.class) {
            if (value instanceof Vector3D) {
                value = new Vector(((Vector3D) value).getX(), ((Vector3D) value).getY(), ((Vector3D) value).getZ());
            } else if (value instanceof Vector3Df) {
                value = new Vector(((Vector3Df) value).getX(), ((Vector3Df) value).getY(), ((Vector3Df) value).getZ());
            }
        } else if (bukkitMetadata.valueClass == Color.class) {
            if (value instanceof RGBLike) {
                value = Color.fromRGB(((RGBLike) value).red(), ((RGBLike) value).green(), ((RGBLike) value).blue());
            }
        } else if (bukkitMetadata.valueClass == DyeColor.class) {
            if (value instanceof RGBLike) {
                value = DyeColor.getByColor(Color.fromRGB(((RGBLike) value).red(), ((RGBLike) value).green(), ((RGBLike) value).blue()));
            } else if (value instanceof Number) {
                value = DyeColor.getByWoolData(((Number) value).byteValue());
            }
        } else if (bukkitMetadata.valueClass.isEnum() && value instanceof String) {
            String finalValue = (String) value;
            value = Arrays.stream(bukkitMetadata.valueClass.getEnumConstants()).filter(o -> finalValue.equalsIgnoreCase((String) Reflect.fastInvoke(o, "name"))).findFirst().orElse(null);
        } else if (value instanceof Component) { // TODO: converting component to platform component, not just strings
            value = AdventureHelper.toLegacyNullableResult((Component) value);
        }

        if (value != null) {
            if (bukkitMetadata.valueClass == Boolean.class) {
                if (!(value instanceof Boolean)) {
                    value = Boolean.parseBoolean(value.toString());
                }
            } else if (bukkitMetadata.valueClass == Integer.class) {
                if (value instanceof Number) {
                    value = ((Number) value).intValue();
                } else {
                    try {
                        value = Integer.parseInt(value.toString());
                    } catch (NumberFormatException ignored) {
                    }
                }
            } else if (bukkitMetadata.valueClass == Long.class) {
                if (value instanceof Number) {
                    value = ((Number) value).longValue();
                } else {
                    try {
                        value = Long.parseLong(value.toString());
                    } catch (NumberFormatException ignored) {
                    }
                }
            } else if (bukkitMetadata.valueClass == Float.class) {
                if (value instanceof Number) {
                    value = ((Number) value).floatValue();
                } else {
                    try {
                        value = Float.parseFloat(value.toString());
                    } catch (NumberFormatException ignored) {
                    }
                }
            } else if (bukkitMetadata.valueClass == Double.class) {
                if (value instanceof Number) {
                    value = ((Number) value).doubleValue();
                } else {
                    try {
                        value = Double.parseDouble(value.toString());
                    } catch (NumberFormatException ignored) {
                    }
                }
            } else if (bukkitMetadata.valueClass == Byte.class) {
                if (value instanceof Number) {
                    value = ((Number) value).byteValue();
                } else {
                    try {
                        value = Byte.parseByte(value.toString());
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }

        if (value == null || bukkitMetadata.valueClass.isInstance(value)) {
            //noinspection RedundantCast (it's not redundant actually, but you know IDEA)
            ((BukkitMetadata) bukkitMetadata).setter.accept(entity, value);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Entity entity, String metadata, Class<T> valueClass) {
        final var finalMetadata = metadata.toLowerCase();
        var bukkitMetadata = METADATA.entrySet().stream()
                .filter(classMapEntry -> classMapEntry.getKey().isInstance(entity) && classMapEntry.getValue().containsKey(finalMetadata))
                .map(classMapEntry -> classMapEntry.getValue().get(finalMetadata))
                .findFirst()
                .orElseThrow();

        //noinspection RedundantCast (it's not redundant actually, but you know IDEA)
        var value = ((BukkitMetadata) bukkitMetadata).getter.apply(entity);

        if (valueClass.isInstance(value)) {
            return (T) value;
        }

        if (valueClass == BlockTypeHolder.class) {
            return (T) BlockTypeHolder.of(value);
        } else if (valueClass == ItemTypeHolder.class) {
            return (T) ItemTypeHolder.of(value);
        } else if (EntityBasic.class.isAssignableFrom(valueClass) && value instanceof Entity) {
            return (T) EntityMapper.wrapEntity(value).orElseThrow();
        } else if (value instanceof ItemStack && valueClass == Item.class) {
            return (T) new BukkitItem((ItemStack) value);
        } else if (valueClass == LocationHolder.class) {
            if (value instanceof Location) {
                return (T) LocationMapper.wrapLocation(value);
            }
        } else if (valueClass == Component.class) {
            return (T) AdventureHelper.toComponentNullableResult(value.toString()); // TODO: converting platform component to component, not just strings
        } else if (valueClass == String.class) {
            if (value.getClass().isEnum()) {
                return (T) Reflect.fastInvoke(value, "name");
            } else {
                return (T) value.toString();
            }
        } else if (valueClass == Vector3D.class) {
            if (value instanceof EulerAngle) {
                return (T) new Vector3D(((EulerAngle) value).getX(), ((EulerAngle) value).getY(), ((EulerAngle) value).getZ());
            } else if (value instanceof Vector) {
                return (T) new Vector3D(((Vector) value).getX(), ((Vector) value).getY(), ((Vector) value).getZ());
            }
        } else if (valueClass == Vector3Df.class) {
            if (value instanceof EulerAngle) {
                return (T) new Vector3Df((float) ((EulerAngle) value).getX(), (float) ((EulerAngle) value).getY(), (float) ((EulerAngle) value).getZ());
            } else if (value instanceof Vector) {
                return (T) new Vector3Df((float) ((Vector) value).getX(), (float) ((Vector) value).getY(), (float) ((Vector) value).getZ());
            }
        } else if (valueClass == RGBLike.class) {
            if (value instanceof Color) {
                return (T) TextColor.color(((Color) value).getRed(), ((Color) value).getGreen(), ((Color) value).getBlue());
            } else if (value instanceof DyeColor) {
                return (T) TextColor.color(((DyeColor) value).getColor().getRed(), ((DyeColor) value).getColor().getGreen(), ((DyeColor) value).getColor().getBlue());
            }
        } else if (valueClass == Boolean.class) {
            if (value instanceof Boolean) {
                return (T) value;
            } else {
                return (T) Boolean.valueOf(value.toString());
            }
        } else if (valueClass == Integer.class) {
            if (value instanceof Number) {
                return (T) (Integer) ((Number) value).intValue();
            } else {
                try {
                    return (T) Integer.valueOf(value.toString());
                } catch (NumberFormatException ignored) {
                }
            }
        } else if (valueClass == Long.class) {
            if (value instanceof Number) {
                return (T) (Long) ((Number) value).longValue();
            } else {
                try {
                    return (T) Long.valueOf(value.toString());
                } catch (NumberFormatException ignored) {
                }
            }
        } else if (valueClass == Byte.class) {
            if (value instanceof Number) {
                return (T) (Byte) ((Number) value).byteValue();
            } else if (value instanceof DyeColor) {
                return (T) (Byte) ((DyeColor) value).getWoolData();
            } else {
                try {
                    return (T) Byte.valueOf(value.toString());
                } catch (NumberFormatException ignored) {
                }
            }
        } else if (valueClass == Double.class) {
            if (value instanceof Number) {
                return (T) (Double) ((Number) value).doubleValue();
            } else {
                try {
                    return (T) Double.valueOf(value.toString());
                } catch (NumberFormatException ignored) {
                }
            }
        } else if (valueClass == Float.class) {
            if (value instanceof Number) {
                return (T) (Float) ((Number) value).floatValue();
            } else {
                try {
                    return (T) Float.valueOf(value.toString());
                } catch (NumberFormatException ignored) {
                }
            }
        }

        throw new UnsupportedOperationException("Can't convert metadata " + metadata + " of " + entity + " to " + valueClass.getName());
    }

    @Data
    public static class BukkitMetadata<E extends Entity, V> {
        private final Class<V> valueClass;
        @NotNull
        private final Function<E, V> getter;
        @Nullable
        private final BiConsumer<E, V> setter;

    }

    @RequiredArgsConstructor
    public static class Builder<E extends Entity> {
        private final Class<E> clazz;
        private boolean registered = false;
        private final Map<String, BukkitMetadata<E, ?>> metadataMap = new HashMap<>();

        public static <E extends Entity> Builder<E> begin(Class<E> entityClass) {
            return new Builder<>(entityClass);
        }

        public <V> Builder<E> map(@NotNull String name, @NotNull Class<V> valueClass, @NotNull Function<E, V> getter, @Nullable BiConsumer<E, V> setter) {
            metadataMap.put(name.toLowerCase(), new BukkitMetadata<>(valueClass, getter, setter));
            if (!registered) {
                METADATA.put(clazz, (Map) metadataMap);
                registered = true;
            }
            return this;
        }

        public <V> Builder<E> map(@NotNull String name, @Nullable String alternativeName, @NotNull Class<V> valueClass, @NotNull Function<E, V> getter, @Nullable BiConsumer<E, V> setter) {
            var d = new BukkitMetadata<>(valueClass, getter, setter);
            metadataMap.put(name.toLowerCase(), d);
            if (alternativeName != null) {
                metadataMap.put(alternativeName.toLowerCase(), d);
            }
            if (!registered) {
                METADATA.put(clazz, (Map) metadataMap);
                registered = true;
            }
            return this;
        }

        public Builder<E> when(boolean condition, Consumer<Builder<E>> consumer) {
            if (condition) {
                consumer.accept(this);
            }
            return this;
        }

        public Builder<E> when(Supplier<Boolean> condition, Consumer<Builder<E>> consumer) {
            if (condition.get()) {
                consumer.accept(this);
            }
            return this;
        }

        public Builder<E> when(int major, int minor, Consumer<Builder<E>> consumer) {
            if (Version.isVersion(major, minor)) {
                consumer.accept(this);
            }
            return this;
        }

        public Builder<E> when(int major, int minor, int patch, Consumer<Builder<E>> consumer) {
            if (Version.isVersion(major, minor, patch)) {
                consumer.accept(this);
            }
            return this;
        }

        public Builder<E> whenNot(boolean condition, Consumer<Builder<E>> consumer) {
            if (!condition) {
                consumer.accept(this);
            }
            return this;
        }

        public Builder<E> whenNot(Supplier<Boolean> condition, Consumer<Builder<E>> consumer) {
            if (!condition.get()) {
                consumer.accept(this);
            }
            return this;
        }

        public Builder<E> whenNot(int major, int minor, Consumer<Builder<E>> consumer) {
            if (!Version.isVersion(major, minor)) {
                consumer.accept(this);
            }
            return this;
        }

        public Builder<E> whenNot(int major, int minor, int patch, Consumer<Builder<E>> consumer) {
            if (!Version.isVersion(major, minor, patch)) {
                consumer.accept(this);
            }
            return this;
        }
    }
}
