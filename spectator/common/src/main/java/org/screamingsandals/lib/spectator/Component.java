package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.Content;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.List;

public interface Component extends ComponentLike, Wrapper, Content {

    static Component empty() {
        return Spectator.getBackend().empty();
    }

    List<Component> children();

    @Nullable
    Color color();

    @LimitedVersionSupport(">= 1.16")
    @Nullable
    NamespacedMappingKey font();

    boolean bold();

    boolean italic();

    boolean underlined();

    boolean strikethrough();

    boolean obfuscated();

    @Nullable
    String insertion();

    @Nullable
    HoverEvent hoverEvent();

    @Nullable
    ClickEvent clickEvent();

    @Override
    default Component asComponent() {
        return this;
    }
}
