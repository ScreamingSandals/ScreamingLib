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

package org.screamingsandals.lib.utils.annotations.ide;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.PARAMETER)
@Documented
public @interface MinecraftType {
    @NotNull Type value();

    enum Type {
        ATTRIBUTE_TYPE,
        BLOCK,
        BLOCK_TYPE,
        BLOCK_OR_TAG,
        BLOCK_TYPE_TAG,
        DAMAGE_TYPE,
        DIFFICULTY_TYPE,
        DIMENSION_TYPE,
        DYE_COLOR,
        ENCHANTMENT,
        ENCHANTMENT_TYPE,
        ENTITY_POSE,
        ENTITY_TYPE,
        ENTITY_TYPE_OR_TAG,
        ENTITY_TYPE_TAG,
        EQUIPMENT_SLOT,
        FIREWORK_EFFECT,
        FIREWORK_EFFECT_OR_FIREWORK_EFFECT_TYPE,
        FIREWORK_EFFECT_TYPE,
        GAME_EVENT,
        GAME_MODE,
        GAME_RULE_TYPE,
        GOAL_TYPE,
        INVENTORY_TYPE,
        ITEM_TYPE,
        ITEM_TYPE_OR_TAG,
        ITEM_TYPE_TAG,
        PARTICLE_TYPE,
        POTION,
        POTION_EFFECT,
        POTION_EFFECT_TYPE,
        PROFESSION,
        SOUND,
        SOUND_SOURCE,
        VILLAGER_TYPE,
        WEATHER;
    }
}
