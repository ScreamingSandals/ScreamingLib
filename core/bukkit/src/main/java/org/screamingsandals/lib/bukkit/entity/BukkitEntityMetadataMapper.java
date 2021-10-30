package org.screamingsandals.lib.bukkit.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.entity.*;
import org.bukkit.util.EulerAngle;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

// TODO: it would be better to generate this class and maybe find a better way then manually doing this shit (because of fucking modded servers)
public class BukkitEntityMetadataMapper {

    private static final Map<Class<? extends Entity>, Map<String, BukkitMetadata<?, ?>>> METADATA = new HashMap<>();

    static {
        Builder.begin(AreaEffectCloud.class)
                .map("radius", "data_radius", float.class, AreaEffectCloud::getRadius, AreaEffectCloud::setRadius)
                .map("color", "data_color", Color.class, AreaEffectCloud::getColor, AreaEffectCloud::setColor);
                // TODO: data_waiting BOOLEAN
                // TODO: data_particle PARTICLE

        Builder.begin(ArmorStand.class)
                .map("small", boolean.class, ArmorStand::isSmall, ArmorStand::setSmall)
                .map("arms", boolean.class, ArmorStand::hasArms, ArmorStand::setArms)
                .map("base_plate", boolean.class, ArmorStand::hasBasePlate, ArmorStand::setBasePlate)
                .map("marker", boolean.class, ArmorStand::isMarker, ArmorStand::setMarker)

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
                .map("awake", boolean.class, Bat::isAwake, Bat::setAwake)

                .map("is_hanging", boolean.class, bat -> !bat.isAwake(), (bat, aBoolean) -> bat.setAwake(!aBoolean))
                .map("flags", "data_id_flags", byte.class, bat -> (byte) (bat.isAwake() ? 0x0 : 0x1), (bat, aByte) -> bat.setAwake((aByte & 0x1) == 0x1));

        Builder.begin(Bee.class) // TODO: add check if the mob exists
                // TODO: data_flags_id BYTE
                .map("remaining_anger_time", "data_remaining_anger_time", int.class, Bee::getAnger, Bee::setAnger);

        // TODO: the rest https://github.com/Articdive/ArticData/blob/1.17.1/1_17_1_entities.json

    }

    public static void set(Entity entity, String metadata, Object value) {

    }

    public static <T> T get(Entity entity, String metadata, Class<T> valueClass) {
        return null;
    }

    @Data
    public static class BukkitMetadata<E extends Entity, V> {
        private final Class<V> valueClass;
        private final Function<E, V> getter;
        private final BiConsumer<E, V> setter;

    }

    @RequiredArgsConstructor
    public static class Builder<E extends Entity> {
        private final Class<E> entity;
        private final Map<String, BukkitMetadata<E, ?>> metadataMap = new HashMap<>();

        public static <E extends Entity> Builder<E> begin(Class<E> entityClass) {
            var b = new Builder<>(entityClass);
            METADATA.put(entityClass, (Map) b.metadataMap);
            return b;
        }

        public <V> Builder<E> map(String name, Class<V> valueClass, Function<E, V> getter, BiConsumer<E, V> setter) {
            metadataMap.put(name, new BukkitMetadata<>(valueClass, getter, setter));
            return this;
        }

        public <V> Builder<E> map(String name, String mojangName, Class<V> valueClass, Function<E, V> getter, BiConsumer<E, V> setter) {
            var d = new BukkitMetadata<>(valueClass, getter, setter);
            metadataMap.put(name, d);
            metadataMap.put(mojangName, d);
            return this;
        }
    }
}
