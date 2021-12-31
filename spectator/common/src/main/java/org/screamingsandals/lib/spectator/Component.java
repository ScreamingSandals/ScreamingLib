package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.Content;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Collection;
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

    interface Builder<B extends Builder<B, C>, C extends Component> {
        B color(Color color);

        B append(Component component);

        B append(Component... components);

        B append(Collection<Component> components);

        @LimitedVersionSupport(">= 1.16")
        B font(NamespacedMappingKey font);

        default B bold() {
            return bold(true);
        }

        B bold(boolean bold);

        default B italic() {
            return italic(true);
        }

        B italic(boolean italic);

        default B underlined() {
            return underlined(true);
        }

        B underlined(boolean underlined);

        default B strikethrough() {
            return strikethrough(true);
        }

        B strikethrough(boolean strikethrough);

        default B obfuscated() {
            return obfuscated(true);
        }

        B obfuscated(boolean obfuscated);

        B insertion(@Nullable String insertion);

        B hoverEvent(@Nullable HoverEvent event);

        B clickEvent(@Nullable ClickEvent event);

        C build();
    }
}
