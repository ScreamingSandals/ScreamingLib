package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.audience.Audience;
import org.screamingsandals.lib.spectator.title.TimesProvider;
import org.screamingsandals.lib.spectator.title.Title;

public interface TitleableAudienceComponentLike extends AudienceComponentLike {
    @NotNull
    Title asTitle(@Nullable Audience audience, @Nullable TimesProvider times);

    @NotNull
    Title asTitle(@Nullable Audience audience);

    @NotNull
    Title asTitle(@Nullable TimesProvider times);

    @NotNull
    Title asTitle();
}
