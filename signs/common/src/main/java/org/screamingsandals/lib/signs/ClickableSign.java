package org.screamingsandals.lib.signs;

import lombok.Data;

@Data
public class ClickableSign {
    private final SignLocation location;
    private final String key;
}
