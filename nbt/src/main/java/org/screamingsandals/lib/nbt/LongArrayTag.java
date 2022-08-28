package org.screamingsandals.lib.nbt;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Data
@Accessors(fluent = true)
public final class LongArrayTag implements Tag {
    private final long @NotNull [] value;
}
