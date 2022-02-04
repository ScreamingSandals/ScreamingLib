package org.screamingsandals.lib.bungee.spectator.title;

import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.title.Title;

import java.time.Duration;

@Data
@Accessors(fluent = true)
public class BungeeTitle implements Title {
    private final Component title;
    private final Component subtitle;
    private final Duration fadeIn;
    private final Duration stay;
    private final Duration fadeOut;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type.isInstance(this)) {
            return (T) this;
        }
        if (type == net.md_5.bungee.api.Title.class) {
            return (T) raw();
        }
        throw new UnsupportedOperationException("Can't unwrap BungeeTitle to " + type.getName());
    }

    @Override
    public Object raw() {
        return ProxyServer.getInstance().createTitle()
                .title(title.as(BaseComponent.class))
                .subTitle(subtitle.as(BaseComponent.class))
                .fadeIn((int) (fadeIn.toMillis() / 50))
                .stay((int) (stay.toMillis() / 50))
                .fadeOut((int) (fadeOut.toMillis() / 50));
    }

    @Setter
    public static class BungeeTitleBuilder implements Title.Builder {
        private Component title;
        private Component subtitle;
        private Duration fadeIn;
        private Duration stay;
        private Duration fadeOut;

        @Override
        @NotNull
        public Title build() {
            return new BungeeTitle(
                    title == null ? Component.empty() : title,
                    subtitle == null ? Component.empty() : subtitle,
                    fadeIn == null ? Duration.ofMillis(500) : fadeIn,
                    stay == null ? Duration.ofMillis(3500) : stay,
                    fadeOut == null ? Duration.ofMillis(1000) : fadeOut
            );
        }
    }
}
