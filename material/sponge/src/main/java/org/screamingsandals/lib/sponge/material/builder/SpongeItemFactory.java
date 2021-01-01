package org.screamingsandals.lib.sponge.material.builder;

import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.sponge.material.container.SpongeContainer;
import org.spongepowered.api.item.inventory.Inventory;

import java.util.Optional;

public class SpongeItemFactory extends ItemFactory {
    public static void init() {
        ItemFactory.init(SpongeItemFactory::new);
    }

    public SpongeItemFactory() {
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
