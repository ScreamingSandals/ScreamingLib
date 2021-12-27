package org.screamingsandals.lib.bukkit.item.meta;

import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.screamingsandals.lib.item.meta.PotionMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitPotionMapping extends PotionMapping {

    public BukkitPotionMapping() {
        potionConverter
                .registerP2W(PotionType.class, BukkitPotionHolder::new)
                .registerP2W(PotionData.class, BukkitPotionHolder::new);

        Arrays.stream(PotionType.values()).forEach(potion -> {
            var holder = new BukkitPotionHolder(potion);
            mapping.put(NamespacedMappingKey.of(potion.name()), holder);
            values.add(holder);
            if (potion.isExtendable()) {
                var holder2 = new BukkitPotionHolder(new PotionData(potion, true, false));
                mapping.put(NamespacedMappingKey.of("long_" + potion.name()), holder2);
                values.add(holder2);
            }
            if (potion.isUpgradeable()) {
                var holder3 = new BukkitPotionHolder(new PotionData(potion, false, true));
                mapping.put(NamespacedMappingKey.of("strong_" + potion.name()), holder3);
                values.add(holder3);
            }
        });
    }
}
