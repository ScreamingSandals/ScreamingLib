package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

public interface SeparableComponent extends Component {
    @LimitedVersionSupport(">= 1.17")
    @Nullable
    Component separator();

    interface Builder<B extends Builder<B, C>, C extends SeparableComponent> extends Component.Builder<B, C> {
        @LimitedVersionSupport(">= 1.17")
        B separator(@Nullable Component separator);
    }
}
