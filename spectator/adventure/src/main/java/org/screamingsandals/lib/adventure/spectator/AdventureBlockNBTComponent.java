package org.screamingsandals.lib.adventure.spectator;

import org.screamingsandals.lib.spectator.BlockNBTComponent;

public class AdventureBlockNBTComponent extends AdventureNBTComponent<net.kyori.adventure.text.BlockNBTComponent> implements BlockNBTComponent {
    public AdventureBlockNBTComponent(net.kyori.adventure.text.BlockNBTComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String blockPosition() {
        return ((net.kyori.adventure.text.BlockNBTComponent) wrappedObject).pos().asString();
    }

    public static class AdventureBlockNBTBuilder extends AdventureNBTComponent.AdventureNBTBuilder<
            net.kyori.adventure.text.BlockNBTComponent,
            BlockNBTComponent.Builder,
            BlockNBTComponent,
            net.kyori.adventure.text.BlockNBTComponent.Builder
            > implements BlockNBTComponent.Builder {

        public AdventureBlockNBTBuilder(net.kyori.adventure.text.BlockNBTComponent.Builder builder) {
            super(builder);
        }

        @Override
        public BlockNBTComponent.Builder blockPosition(String blockPosition) {
            getBuilder().pos(net.kyori.adventure.text.BlockNBTComponent.Pos.fromString(blockPosition));
            return self();
        }
    }
}
