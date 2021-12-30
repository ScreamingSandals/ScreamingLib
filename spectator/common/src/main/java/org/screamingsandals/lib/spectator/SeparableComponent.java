package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

public interface SeparableComponent extends Component {
    @LimitedVersionSupport(">= 1.17")
    @Nullable
    Component separator();
}
