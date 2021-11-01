package org.screamingsandals.lib.bukkit.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
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

                .map("client_flags", "data_client_flags", byte.class, armorStand ->
                                (byte) ((armorStand.isSmall() ? 0x01 : 0x00)
                                        | (armorStand.hasArms() ? 0x04 : 0x00)
                                        | (armorStand.hasBasePlate() ? 0x08 : 0x00)
                                        | (armorStand.isMarker() ? 0x10 : 0x00)),
                        (armorStand, aByte) -> {
                            armorStand.setSmall((aByte & 0x01) == 0x01);
                            armorStand.setArms((aByte & 0x04) == 0x04);
                            armorStand.setBasePlate((aByte & 0x08) == 0x08);
                            armorStand.setMarker((aByte & 0x10) == 0x10);
                        })
                .map("head_pose", "data_head_pose", EulerAngle.class, ArmorStand::getHeadPose, ArmorStand::setHeadPose)
                .map("body_pose", "data_body_pose", EulerAngle.class, ArmorStand::getBodyPose, ArmorStand::setBodyPose)
                .map("left_arm_pose", "data_left_arm_pose", EulerAngle.class, ArmorStand::getLeftArmPose, ArmorStand::setLeftArmPose)
                .map("right_arm_pose", "data_right_arm_pose", EulerAngle.class, ArmorStand::getRightArmPose, ArmorStand::setRightArmPose)
                .map("left_leg_pose", "data_left_leg_pose", EulerAngle.class, ArmorStand::getLeftLegPose, ArmorStand::setLeftLegPose)
                .map("right_leg_pose", "data_right_leg_pose", EulerAngle.class, ArmorStand::getRightLegPose, ArmorStand::setRightLegPose);

        Builder.begin(Arrow.class)
                .map("effect_color", "id_effect_color", Color.class, Arrow::getColor, Arrow::setColor);

        // TODO: Axolotl
                // TODO: data_variant INT
                // TODO: data_playing_dead BOOLEAN
                // TODO: from_bucket BOOLEAN

        Builder.begin(Bat.class)
                .map("awake", Boolean.class, Bat::isAwake, Bat::setAwake)

                .map("is_hanging", Boolean.class, bat -> !bat.isAwake(), (bat, aBoolean) -> bat.setAwake(!aBoolean))
                .map("flags", "data_id_flags", Byte.class, bat -> (byte) (bat.isAwake() ? 0x0 : 0x1), (bat, aByte) -> bat.setAwake((aByte & 0x1) == 0x1));

        if (Reflect.has("org.bukkit.entity.Bee")) {
            Builder.begin(Bee.class) // TODO: add check if the mob exists
                    // TODO: data_flags_id BYTE
                    .map("remaining_anger_time", "data_remaining_anger_time", Integer.class, Bee::getAnger, Bee::setAnger);
        }

        // TODO: the rest https://github.com/Articdive/ArticData/blob/1.17.1/1_17_1_entities.json

        // required by bedwars

        Builder.begin(Ageable.class) // TODO: Fix for Piglin & Zoglin are not Ageable in early 1.16 spigot days
                .map("is_baby", "data_baby_id", Boolean.class, ageable -> !ageable.isAdult(), (ageable, aBoolean) -> {
                    if (aBoolean) {
                        ageable.setBaby();
                    } else {
                        ageable.setAdult();
                    }
                });

        Builder.begin(Villager.class)
                .map("villager_profession", Villager.Profession.class, Villager::getProfession, Villager::setProfession)
                .when(1, 14, b -> b
                        .map("villager_type", Villager.Type.class, Villager::getVillagerType, Villager::setVillagerType)
                        .map("villager_level", Integer.class, Villager::getVillagerLevel, Villager::setVillagerLevel)
                        .map("villager_experience", Integer.class, Villager::getVillagerExperience, Villager::setVillagerExperience)
                );

        Builder.begin(Sheep.class)
                .map("color", DyeColor.class, Sheep::getColor, Sheep::setColor)
                .map("sheared", Boolean.class, Sheep::isSheared, Sheep::setSheared);

        Builder.begin(Explosive.class)
                .map("is_incendiary", Boolean.class, Explosive::isIncendiary, Explosive::setIsIncendiary)
                .map("yield", Float.class, Explosive::getYield, Explosive::setYield);;

        Builder.begin(TNTPrimed.class)
                .map("fuse_ticks", "data_fuse_id", Integer.class, TNTPrimed::getFuseTicks, TNTPrimed::setFuseTicks);

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
                } catch (NumberFormatException ignored) {}
            }
        } else if (valueClass == Long.class) {
            if (value instanceof Number) {
                return (T) (Long) ((Number) value).longValue();
            } else {
                try {
                    return (T) Long.valueOf(value.toString());
                } catch (NumberFormatException ignored) {}
            }
        } else if (valueClass == Byte.class) {
            if (value instanceof Number) {
                return (T) (Byte) ((Number) value).byteValue();
            } else if (value instanceof DyeColor) {
                return (T) (Byte) ((DyeColor) value).getWoolData();
            } else {
                try {
                    return (T) Byte.valueOf(value.toString());
                } catch (NumberFormatException ignored) {}
            }
        } else if (valueClass == Double.class) {
            if (value instanceof Number) {
                return (T) (Double) ((Number) value).doubleValue();
            } else {
                try {
                    return (T) Double.valueOf(value.toString());
                } catch (NumberFormatException ignored) {}
            }
        } else if (valueClass == Float.class) {
            if (value instanceof Number) {
                return (T) (Float) ((Number) value).floatValue();
            } else {
                try {
                    return (T) Float.valueOf(value.toString());
                } catch (NumberFormatException ignored) {}
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
