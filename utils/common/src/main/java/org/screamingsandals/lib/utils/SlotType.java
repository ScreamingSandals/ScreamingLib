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

package org.screamingsandals.lib.utils;

import java.util.Arrays;
import java.util.List;

// TODO: Replace with holder
public enum SlotType {
    /**
     * A result slot in a furnace or crafting inventory.
     */
    RESULT,
    /**
     * A slot in the crafting matrix, or an 'input' slot.
     */
    CRAFTING,
    /**
     * An armour slot in the player's inventory.
     */
    ARMOR,
    /**
     * A regular slot in the container or the player's inventory; anything
     * not covered by the other enum values.
     */
    CONTAINER,
    /**
     * A slot in the bottom row or quickbar.
     */
    QUICKBAR,
    /**
     * A pseudo-slot representing the area outside the inventory window.
     */
    OUTSIDE,
    /**
     * The fuel slot in a furnace inventory, or the ingredient slot in a
     * brewing stand inventory.
     */
    FUEL;

    public static final List<SlotType> VALUES = Arrays.asList(values());

    public static SlotType convert(String input) {
        return VALUES.stream()
                .filter(next -> input.equalsIgnoreCase(next.name()))
                .findFirst()
                .orElse(SlotType.RESULT);
    }
}
