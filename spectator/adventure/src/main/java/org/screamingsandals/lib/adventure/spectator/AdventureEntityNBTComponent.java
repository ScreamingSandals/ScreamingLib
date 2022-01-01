package org.screamingsandals.lib.adventure.spectator;

import org.screamingsandals.lib.spectator.EntityNBTComponent;

public class AdventureEntityNBTComponent extends AdventureNBTComponent<net.kyori.adventure.text.EntityNBTComponent> implements EntityNBTComponent {
    public AdventureEntityNBTComponent(net.kyori.adventure.text.EntityNBTComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String selector() {
        return ((net.kyori.adventure.text.EntityNBTComponent) wrappedObject).selector();
    }

    public static class AdventureEntityNBTBuilder extends AdventureNBTComponent.AdventureNBTBuilder<
            net.kyori.adventure.text.EntityNBTComponent,
            EntityNBTComponent.Builder,
            EntityNBTComponent,
            net.kyori.adventure.text.EntityNBTComponent.Builder
            > implements EntityNBTComponent.Builder {

        public AdventureEntityNBTBuilder(net.kyori.adventure.text.EntityNBTComponent.Builder builder) {
            super(builder);
        }

        @Override
        public EntityNBTComponent.Builder selector(String selector) {
            getBuilder().selector(selector);
            return self();
        }
    }

}
