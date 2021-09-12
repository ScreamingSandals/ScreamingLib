package org.screamingsandals.lib.utils.annotations.ide;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface CustomAutocompletion {
    Type value();

    enum Type {
        MATERIAL,
        ENCHANTMENT,
        POTION_EFFECT,
        POTION,
        EQUIPMENT_SLOT,
        FIREWORK_EFFECT,
        ENTITY_TYPE,
        DAMAGE_CAUSE,
        ATTRIBUTE_TYPE,
        GAME_MODE,
        INVENTORY_TYPE,
        ENTITY_POSE,
        DIFFICULTY,
        DIMENSION,
        BLOCK,
        GAME_RULE
    }
}
