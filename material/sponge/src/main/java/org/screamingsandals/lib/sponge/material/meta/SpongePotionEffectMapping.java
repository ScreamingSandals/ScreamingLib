package org.screamingsandals.lib.sponge.material.meta;

import org.screamingsandals.lib.material.meta.PotionEffectMapping;

public class SpongePotionEffectMapping extends PotionEffectMapping {
    public static void init() {
        PotionEffectMapping.init(SpongePotionEffectMapping::new);
    }

    public SpongePotionEffectMapping() {
        // TODO
    }
}
