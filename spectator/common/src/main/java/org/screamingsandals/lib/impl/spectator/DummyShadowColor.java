package org.screamingsandals.lib.impl.spectator;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.ShadowColor;

@Data
@Accessors(fluent = true)
@ApiStatus.Internal
public class DummyShadowColor implements ShadowColor {
    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        throw new UnsupportedOperationException("Platform does not support shadow colors");
    }

    @Override
    public @NotNull Object raw() {
        throw new UnsupportedOperationException("Platform does not support shadow colors");
    }
}
