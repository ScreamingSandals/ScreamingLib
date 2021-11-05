package org.screamingsandals.lib.bukkit.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.TreeSpecies;
import org.bukkit.entity.*;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.*;

// TODO: it would be better to generate this class and maybe find a better way then manually doing this shit (because of fucking modded servers)
public class BukkitEntityMetadataMapper {

    private static final Map<Class<? extends Entity>, Map<String, BukkitMetadata<?, ?>>> METADATA = new HashMap<>();

    static {
        var breedableExist = Reflect.has("org.bukkit.entity.Breedable");

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

        // TODO Enderman: carried block

        // TODO: Eye of Ender (EnderSignal)

        // TODO: Evoker spelling (on older versions)

        // TODO: Evoker fangs owner

        Builder.begin(Explosive.class)
                .map("is_incendiary", Boolean.class, Explosive::isIncendiary, Explosive::setIsIncendiary)
                .map("yield", Float.class, Explosive::getYield, Explosive::setYield);

        // TODO: Falling Block

        Builder.begin(Fireball.class)
                .map("direction", Vector.class, Fireball::getDirection, Fireball::setDirection);

        // TODO: Firework

        // TODO: Fish hook

        // TODO: Foxes

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

        // TODO: Hoglin

        // TODO: Horse

        // TODO: Husk

        Builder.begin(IronGolem.class)
                .map("is_player_created", Boolean.class, IronGolem::isPlayerCreated, IronGolem::setPlayerCreated);

        // TODO: Item Frame

        // TODO: Llama

        // TODO: Minecart and its variants

        if (Reflect.hasMethod(MushroomCow.class, "getVariant")) {
            Builder.begin(MushroomCow.class)
                    .map("variant", "data_type", MushroomCow.Variant.class, MushroomCow::getVariant, MushroomCow::setVariant);
        }

        // TODO: Ocelot + cat type for older versions

        // TODO: Painting

        // TODO: Panda

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

        // TODO: Piglin

        // TODO: Piglin Abstract

        // TODO: Pig Zombie

        if (Reflect.has("org.bukkit.entity.PufferFish")) {
            Builder.begin(PufferFish.class)
                    .map("puff_state", Integer.class, PufferFish::getPuffState, PufferFish::setPuffState);
        }

        Builder.begin(Rabbit.class)
                .map("rabbit_type", "data_type_id", Rabbit.Type.class, Rabbit::getRabbitType, Rabbit::setRabbitType);

        // TODO: Raider

        Builder.begin(Sheep.class)
                .map("color", DyeColor.class, Sheep::getColor, Sheep::setColor)
                .map("sheared", Boolean.class, Sheep::isSheared, Sheep::setSheared);

        // TODO: Shulker

        // TODO: Shulker Bullet

        // TODO: Sittable (why it doesn't extend Entity?)

        // TODO: Sized Fireball

        // TODO: Skeleton

        Builder.begin(Slime.class)
                .map("size", "id_size", Integer.class, Slime::getSize, Slime::setSize);


        if (Reflect.hasMethod(Snowman.class, "isDerp")) {
            Builder.begin(Snowman.class)
                    .map("derp", Boolean.class, Snowman::isDerp, Snowman::setDerp);
        }

        Builder.begin(SpectralArrow.class)
                .map("glowing_ticks", Integer.class, SpectralArrow::getGlowingTicks, SpectralArrow::setGlowingTicks);

        // TODO: Spellcaster

        // TODO: Steerable

        if (Reflect.has("org.bukkit.entity.Strider")) {
            Builder.begin(Strider.class)
                    .map("shivering", "data_suffocating", Boolean.class, Strider::isShivering, Strider::setShivering);
        }

        // TODO: Tameable

        // TODO: Thrown Potion

        Builder.begin(TNTPrimed.class)
                .map("fuse_ticks", "data_fuse_id", Integer.class, TNTPrimed::getFuseTicks, TNTPrimed::setFuseTicks);
                // TODO: source

        // TODO: Tropical fish

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

        // TODO: Zombie

        // TODO: Zombie Villager
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

        if (bukkitMetadata.valueClass == Location.class && value instanceof LocationHolder) {
            value = ((LocationHolder) value).as(Location.class);
        } else if (bukkitMetadata.valueClass == EulerAngle.class) {
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

        if (valueClass == LocationHolder.class) {
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
        private final Function<E, V> getter;
        private final BiConsumer<E, V> setter;

    }

    @RequiredArgsConstructor
    public static class Builder<E extends Entity> {
        private final Map<String, BukkitMetadata<E, ?>> metadataMap = new HashMap<>();

        public static <E extends Entity> Builder<E> begin(Class<E> entityClass) {
            var b = new Builder<E>();
            METADATA.put(entityClass, (Map) b.metadataMap);
            return b;
        }

        public <V> Builder<E> map(String name, Class<V> valueClass, Function<E, V> getter, BiConsumer<E, V> setter) {
            metadataMap.put(name.toLowerCase(), new BukkitMetadata<>(valueClass, getter, setter));
            return this;
        }

        public <V> Builder<E> map(String name, String mojangName, Class<V> valueClass, Function<E, V> getter, BiConsumer<E, V> setter) {
            var d = new BukkitMetadata<>(valueClass, getter, setter);
            metadataMap.put(name.toLowerCase(), d);
            metadataMap.put(mojangName.toLowerCase(), d);
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
    }
}
