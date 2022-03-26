package org.screamingsandals.lib.minitag.tags;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true, chain = true)
public class StandardTag implements RegisteredTag {
    private final TagType tagType;
}
