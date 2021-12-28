package org.screamingsandals.lib.bukkit.item.meta;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.screamingsandals.lib.item.meta.PotionEffectMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.key.NumericMappingKey;

import java.util.Arrays;

@Service
public class BukkitPotionEffectMapping extends PotionEffectMapping {
    @SuppressWarnings("ConstantConditions")
    public BukkitPotionEffectMapping() {
        potionEffectConverter
                .registerP2W(PotionEffectType.class, BukkitPotionEffectHolder::new)
                .registerP2W(PotionEffect.class, BukkitPotionEffectHolder::new);

        Arrays.stream(PotionEffectType.values()).forEach(potionEffectType -> {
            if (potionEffectType != null) { // Yeah, this is possible
                var holder = new BukkitPotionEffectHolder(potionEffectType);
                mapping.put(NamespacedMappingKey.of(potionEffectType.getName()), holder);
                mapping.put(NumericMappingKey.of(potionEffectType.getId()), holder); // compatibility with older bw shops
                values.add(holder);
            }
        });
    }
}
