package org.screamingsandals.lib.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum HideFlags {
    ENCHANTMENTS("HIDE_ENCHANTS"),
    ATTRIBUTE_MODIFIERS("HIDE_ATTRIBUTES"),
    UNBREAKABLE("HIDE_UNBREAKABLE"),
    CAN_DESTROY("HIDE_DESTROYS"),
    CAN_PLACE_ON("HIDE_PLACED_ON"),
    MISC("HIDE_POTION_EFFECTS"),
    DYED("HIDE_DYE");

    @Getter
    private final String bukkitName;

    private static final Map<String, HideFlags> VALUES = new HashMap<>();

    static {
        for (var flag : values()) {
            VALUES.put(flag.name(), flag);
            VALUES.put(flag.bukkitName, flag);
        }
    }

    public static HideFlags convert(String name) {
        return VALUES.getOrDefault(name.toUpperCase(), MISC);
    }
}
