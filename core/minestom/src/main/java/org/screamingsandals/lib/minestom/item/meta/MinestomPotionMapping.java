package org.screamingsandals.lib.minestom.item.meta;

import net.minestom.server.potion.PotionType;
import org.screamingsandals.lib.item.meta.PotionHolder;
import org.screamingsandals.lib.item.meta.PotionMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@Service
public class MinestomPotionMapping extends PotionMapping {
    public MinestomPotionMapping() {
        potionConverter
                .registerW2P(PotionType.class, e -> PotionType.fromNamespaceId(e.getPlatformName()))
                .registerP2W(PotionType.class, e -> new PotionHolder(e.name()));

        PotionType.values().forEach(potion -> {
            final var holder = new PotionHolder(potion.name());
            mapping.put(NamespacedMappingKey.of(potion.name()), holder);
            values.add(holder);
        });
    }
}
