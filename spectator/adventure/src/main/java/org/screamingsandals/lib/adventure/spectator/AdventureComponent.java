package org.screamingsandals.lib.adventure.spectator;

import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.adventure.spectator.event.AdventureClickEvent;
import org.screamingsandals.lib.adventure.spectator.event.AdventureHoverEvent;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.List;
import java.util.stream.Collectors;

public class AdventureComponent extends BasicWrapper<net.kyori.adventure.text.Component> implements Component {
    public AdventureComponent(net.kyori.adventure.text.Component wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public List<Component> children() {
        return wrappedObject.children()
                .stream()
                .map(AdventureBackend::wrapComponent)
                .collect(Collectors.toList());
    }

    @Override
    @Nullable
    public Color color() {
        var color = wrappedObject.style().color();
        if (color == null) {
            return null;
        }
        return new AdventureColor(color);
    }

    @Override
    @Nullable
    public NamespacedMappingKey font() {
        var font = wrappedObject.style().font();
        if (font == null) {
            return null;
        }
        return NamespacedMappingKey.of(font.asString());
    }

    @Override
    public boolean bold() {
        return wrappedObject.style().hasDecoration(TextDecoration.BOLD);
    }

    @Override
    public boolean italic() {
        return wrappedObject.style().hasDecoration(TextDecoration.ITALIC);
    }

    @Override
    public boolean underlined() {
        return wrappedObject.style().hasDecoration(TextDecoration.UNDERLINED);
    }

    @Override
    public boolean strikethrough() {
        return wrappedObject.style().hasDecoration(TextDecoration.STRIKETHROUGH);
    }

    @Override
    public boolean obfuscated() {
        return wrappedObject.style().hasDecoration(TextDecoration.OBFUSCATED);
    }

    @Override
    @Nullable
    public String insertion() {
        return wrappedObject.style().insertion();
    }

    @Override
    @Nullable
    public HoverEvent hoverEvent() {
        var hoverEvent = wrappedObject.style().hoverEvent();
        if (hoverEvent == null) {
            return null;
        }
        return new AdventureHoverEvent(hoverEvent);
    }

    @Override
    @Nullable
    public ClickEvent clickEvent() {
        var clickEvent = wrappedObject.style().clickEvent();
        if (clickEvent == null) {
            return null;
        }
        return new AdventureClickEvent(clickEvent);
    }
}
