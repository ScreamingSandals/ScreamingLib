package org.screamingsandals.lib.block.tags;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Data
public class TagPortHelper {
    @NotNull
    private final Predicate<@NotNull String> nativeTagChecker;
    @Nullable
    private List<@NotNull String> ports;

    public void port(@NotNull String tag) {
        if (ports == null) {
            ports = new ArrayList<>();
        }
        ports.add(tag);
    }

    public boolean hasTag(@NotNull String tag) {
        if (ports != null && ports.contains(tag)) {
            return true;
        }
        return nativeTagChecker.test(tag);
    }

    public boolean hasTag(@NotNull String @NotNull... tags) {
        for (var tag : tags) {
            if (hasTag(tag)) {
                return true;
            }
        }
        return false;
    }
}
