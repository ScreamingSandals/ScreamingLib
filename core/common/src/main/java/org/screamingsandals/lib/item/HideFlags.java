/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
public enum HideFlags {
    ENCHANTMENTS("HIDE_ENCHANTS"),
    ATTRIBUTE_MODIFIERS("HIDE_ATTRIBUTES"),
    UNBREAKABLE("HIDE_UNBREAKABLE"),
    CAN_DESTROY("HIDE_DESTROYS"),
    CAN_PLACE_ON("HIDE_PLACED_ON"),
    MISC("HIDE_POTION_EFFECTS"),
    DYED("HIDE_DYE"),
    ARMOR_TRIM("HIDE_ARMOR_TRIM");

    @Getter
    private final @NotNull String bukkitName;

    private static final @NotNull Map<@NotNull String, HideFlags> VALUES = new HashMap<>();

    static {
        for (var flag : values()) {
            VALUES.put(flag.name(), flag);
            VALUES.put(flag.bukkitName, flag);
        }
    }

    public static @NotNull HideFlags convert(@NotNull String name) {
        return VALUES.getOrDefault(name.toUpperCase(Locale.ROOT), MISC);
    }
}
