package org.screamingsandals.lib.spectator;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class Spectator {
    @Getter
    @Setter(onMethod_=@ApiStatus.Internal)
    private static SpectatorBackend backend;
}
