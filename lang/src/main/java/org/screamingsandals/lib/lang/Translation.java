package org.screamingsandals.lib.lang;

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Data
public final class Translation {
    private final List<String> keys = new LinkedList<>();
    private final Component fallback;

    private Translation(Collection<String> keys, Component fallback) {
        this.keys.addAll(keys);
        this.fallback = fallback;
    }

    public static Translation of(String... keys) {
        return of(Arrays.asList(keys), Component.text(String.join(".", keys)).color(NamedTextColor.RED));
    }

    public static Translation of(Collection<String> keys) {
        return of(keys, Component.text(String.join(".", keys)).color(NamedTextColor.RED));
    }

    public static Translation of(Collection<String> keys, Component fallback) {
        return new Translation(keys, fallback);
    }

    public Translation join(String... key) {
        final var copied = new LinkedList<>(keys);
        copied.addAll(Arrays.asList(key));

        return of(copied);
    }

    public Translation join(Collection<String> keys) {
        final var copied = new LinkedList<>(this.keys);
        copied.addAll(keys);

        return of(copied, Component.text(String.join(".", keys)).color(NamedTextColor.RED));
    }

    public Translation join(Collection<String> keys, Component fallback) {
        final var copied = new LinkedList<>(this.keys);
        copied.addAll(keys);

        return of(copied, fallback);
    }
}
