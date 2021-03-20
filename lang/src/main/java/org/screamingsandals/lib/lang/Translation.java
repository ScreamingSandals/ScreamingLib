package org.screamingsandals.lib.lang;

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public final class Translation {
    private final List<String> keys = new CopyOnWriteArrayList<>();
    private final Component fallback;

    private Translation(Collection<String> keys, Component fallback) {
        this.keys.addAll(keys);
        this.fallback = fallback;
    }

    public static Translation of(Collection<String> keys, Component fallback) {
        return new Translation(keys, fallback);
    }

    public static Translation of(String... keys) {
        return of(Arrays.asList(keys), Component.text(String.join(".", keys)).color(NamedTextColor.RED));
    }

    public static Translation of(Collection<String> keys) {
        return of(keys, Component.text(String.join(".", keys)).color(NamedTextColor.RED));
    }

    public Translation join(String... key) {
        keys.addAll(Arrays.asList(key));
        return this;
    }

    public Translation join(Collection<String> key) {
        keys.addAll(key);
        return this;
    }
}
