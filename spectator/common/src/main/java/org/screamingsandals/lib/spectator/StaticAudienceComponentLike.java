package org.screamingsandals.lib.spectator;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.audience.Audience;

@RequiredArgsConstructor
@ApiStatus.Internal
public final class StaticAudienceComponentLike implements AudienceComponentLike {
    private final Component component;

    @Override
    @NotNull
    public Component asComponent(@Nullable Audience audience) {
        return component;
    }

    @Override
    public Component asComponent() {
        return component;
    }
}
