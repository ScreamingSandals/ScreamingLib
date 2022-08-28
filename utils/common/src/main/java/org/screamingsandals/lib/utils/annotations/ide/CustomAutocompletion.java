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
        GAME_RULE,
        WEATHER,
        PARTICLE_TYPE,
        SOUND,
        SOUND_SOURCE,
        BLOCK_TYPE_TAG,
        ITEM_TYPE_TAG,
        ENTITY_TYPE_TAG
    }
}
