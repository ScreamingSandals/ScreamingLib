package org.screamingsandals.lib.lang;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collection;

@Data
@RequiredArgsConstructor(staticName = "of")
public final class Translation {
    private final String[] keys;
    private final Component fallback;

    public static Translation of(String... keys) {
        return of(keys, Component.text(String.join(".", keys)).color(NamedTextColor.RED));
    }

    public static Translation of(Collection<String> keys) {
        return of(keys.toArray(String[]::new), Component.text(String.join(".", keys)).color(NamedTextColor.RED));
    }

    public static Translation of(Collection<String> keys, Component fallback) {
        return of(keys.toArray(String[]::new), fallback);
    }
}
