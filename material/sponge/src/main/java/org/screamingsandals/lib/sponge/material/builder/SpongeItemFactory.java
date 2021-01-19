package org.screamingsandals.lib.sponge.material.builder;

import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.sponge.material.SpongeMaterialMapping;
import org.screamingsandals.lib.sponge.material.container.SpongeContainer;
import org.screamingsandals.lib.sponge.material.meta.SpongeEnchantmentMapping;
import org.screamingsandals.lib.sponge.material.meta.SpongePotionEffectMapping;
import org.screamingsandals.lib.sponge.material.meta.SpongePotionMapping;
import org.screamingsandals.lib.utils.InitUtils;
import org.spongepowered.api.item.inventory.Inventory;

import java.util.Optional;

public class SpongeItemFactory extends ItemFactory {
    public static void init() {
        ItemFactory.init(SpongeItemFactory::new);
    }

    public SpongeItemFactory() {
        InitUtils.doIfNot(SpongeMaterialMapping::isInitialized, SpongeMaterialMapping::init);
        InitUtils.doIfNot(SpongePotionMapping::isInitialized, SpongePotionMapping::init);
        InitUtils.doIfNot(SpongeEnchantmentMapping::isInitialized, SpongeEnchantmentMapping::init);
        InitUtils.doIfNot(SpongePotionEffectMapping::isInitialized, SpongePotionEffectMapping::init);

        // TODO
    }

    @Override
    public Optional<Container> wrapContainer0(Object container) {
        if (container instanceof Inventory) {
            return Optional.of(new SpongeContainer((Inventory) container));
        }
        return Optional.empty();
    }
}
