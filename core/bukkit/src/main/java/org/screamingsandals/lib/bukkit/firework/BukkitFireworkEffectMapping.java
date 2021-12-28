package org.screamingsandals.lib.bukkit.firework;

import org.bukkit.FireworkEffect;
import org.screamingsandals.lib.firework.FireworkEffectMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitFireworkEffectMapping extends FireworkEffectMapping {
    public BukkitFireworkEffectMapping() {
        fireworkEffectConverter
                .registerP2W(FireworkEffect.Type.class, BukkitFireworkEffectHolder::new)
                .registerP2W(FireworkEffect.class, BukkitFireworkEffectHolder::new);

        Arrays.stream(FireworkEffect.Type.values()).forEach(fireworkEffectType -> {
            var holder = new BukkitFireworkEffectHolder(fireworkEffectType);
            mapping.put(NamespacedMappingKey.of(fireworkEffectType.name()), holder);
            values.add(holder);
        });
    }
}
