package org.screamingsandals.lib.utils;

import org.jetbrains.annotations.NotNull;

public interface Controllable {

    Controllable enable(@NotNull Runnable enableMethod);

    Controllable postEnable(@NotNull Runnable postEnable);

    Controllable preDisable(@NotNull Runnable preDisable);

    Controllable disable(@NotNull Runnable disableMethod);

    Controllable child();
}
