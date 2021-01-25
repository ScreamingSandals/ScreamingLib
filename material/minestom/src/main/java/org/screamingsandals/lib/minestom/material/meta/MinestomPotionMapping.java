package org.screamingsandals.lib.minestom.material.meta;

import net.minestom.server.potion.PotionType;
import org.screamingsandals.lib.material.meta.PotionHolder;
import org.screamingsandals.lib.material.meta.PotionMapping;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Arrays;

@Service
public class MinestomPotionMapping extends PotionMapping {

    public static void init() {
        PotionMapping.init(MinestomPotionMapping::new);
    }

    public MinestomPotionMapping() {
        potionConverter
                .registerW2P(PotionType.class, e -> PotionType.valueOf(e.getPlatformName().toUpperCase()))
                .registerP2W(PotionType.class, e -> new PotionHolder(e.name()));

        Arrays.stream(PotionType.values()).forEach(potion -> potionMapping.put(potion.name().toUpperCase(), new PotionHolder(potion.name())));
    }
}
