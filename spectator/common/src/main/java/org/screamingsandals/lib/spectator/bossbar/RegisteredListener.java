package org.screamingsandals.lib.spectator.bossbar;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.NonExtendable
public interface RegisteredListener {
    @NotNull
    BossBarListener listener();
}
