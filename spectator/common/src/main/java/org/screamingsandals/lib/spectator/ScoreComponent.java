package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

public interface ScoreComponent extends Component {
    String name();

    String objective();

    @LimitedVersionSupport("< 1.16.5")
    @Deprecated
    @Nullable
    String value();
}
