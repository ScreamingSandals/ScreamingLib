package org.screamingsandals.lib.bukkit.item;

import java.util.Map;

public class ItemMetaHelper {
    // TODO: map the rest
    private static final Map<String, String> NBT_TO_BUKKIT = Map.ofEntries(
            Map.entry("Potion", "potion-type"),
            Map.entry("CustomPotionColor", "custom-color"),
            Map.entry("CustomPotionEffects", "custom-effects"),
            Map.entry("Recipes", "Recipes"),
            Map.entry("Fireworks.Explosions", "firework-effects"),
            Map.entry("Fireworks.Flight", "power"),
            Map.entry("Explosion", "firework-effect"),
            Map.entry("display.color", "color"),
            Map.entry("SkullOwner", "skull-owner")
    );


}
