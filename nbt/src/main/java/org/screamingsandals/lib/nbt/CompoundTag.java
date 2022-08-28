package org.screamingsandals.lib.nbt;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

@Data
@Accessors(fluent = true)
public class CompoundTag implements Tag {
    @NotNull
    private final Map<@NotNull String, @NotNull Tag> value;

    @NotNull
    @Contract(value = "-> new", pure = true)
    public Map<@NotNull String, @NotNull Tag> value() {
        return Map.copyOf(value); // keep this class immutable
    }

    public boolean hasTag(@NotNull String name) {
        return value.containsKey(name);
    }

    @Nullable
    public Tag tag(@NotNull String name) {
        return value.get(name);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public CompoundTag with(@NotNull String name, @NotNull Tag tag) {
        var clone = new HashMap<>(value);
        clone.put(name, tag);
        return new CompoundTag(clone);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public CompoundTag without(@NotNull String name) {
        var clone = new HashMap<>(value);
        clone.remove(name);
        return new CompoundTag(clone);
    }

    public void forEach(@NotNull BiConsumer<@NotNull String, @NotNull Tag> consumer) {
        for (var entry : value.entrySet()) {
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }

    @NotNull
    public Stream<Map.@NotNull Entry<@NotNull String, @NotNull Tag>> stream() {
        return value.entrySet().stream();
    }

}
