package org.screamingsandals.lib.bungee.spectator;

import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.List;
import java.util.stream.Collectors;

public class BungeeComponent extends BasicWrapper<BaseComponent> implements Component {
    protected BungeeComponent(BaseComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public List<Component> children() {
        var extra = wrappedObject.getExtra();
        if (extra == null || extra.isEmpty()) {
            return List.of();
        }
        return extra.stream()
                .map(BungeeBackend::wrapComponent)
                .collect(Collectors.toList());
    }

    @Override
    @Nullable
    public Color color() {
        // TODO: should we use getColor() or getColorRaw()? idk, I just want to get the same value I'd get with adventure
        var color = wrappedObject.getColorRaw();
        if (color == null) {
            return null;
        }
        return new BungeeColor(color);
    }

    @Override
    @Nullable
    public NamespacedMappingKey font() {
        try {
            // TODO: should we use getFont() or getFontRaw()? idk, I just want to get the same value I'd get with adventure
            return NamespacedMappingKey.of(wrappedObject.getFontRaw());
        } catch (Throwable ignored) {
            // old version basically; or invalid font, thanks bungee for not checking the input
            return null;
        }
    }

    @Override
    public boolean bold() {
        // TODO: should we use isBold() or isBoldRaw()? idk, I just want to get the same value I'd get with adventure
        return Boolean.TRUE == wrappedObject.isBoldRaw();
    }

    @Override
    public boolean italic() {
        // TODO: should we use isItalic() or isItalicRaw()? idk, I just want to get the same value I'd get with adventure
        return Boolean.TRUE == wrappedObject.isItalicRaw();
    }

    @Override
    public boolean underlined() {
        // TODO: should we use isUnderlined() or isUnderlinedRaw()? idk, I just want to get the same value I'd get with adventure
        return Boolean.TRUE == wrappedObject.isUnderlinedRaw();
    }

    @Override
    public boolean strikethrough() {
        // TODO: should we use isStrikethrough() or isStrikethroughRaw()? idk, I just want to get the same value I'd get with adventure
        return Boolean.TRUE == wrappedObject.isStrikethroughRaw();
    }

    @Override
    public boolean obfuscated() {
        // TODO: should we use isObfuscated() or isObfuscatedRaw()? idk, I just want to get the same value I'd get with adventure
        return Boolean.TRUE == wrappedObject.isObfuscatedRaw();
    }

    @Override
    @Nullable
    public String insertion() {
        return wrappedObject.getInsertion();
    }

    @Override
    @Nullable
    public HoverEvent hoverEvent() {
        return null; // TODO
    }

    @Override
    @Nullable
    public ClickEvent clickEvent() {
        return null; // TODO
    }
}
