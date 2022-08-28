package org.screamingsandals.lib.nbt;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Data
@Accessors(fluent = true)
public class ByteArrayTag implements Tag {
    private final byte @NotNull [] value;
}
