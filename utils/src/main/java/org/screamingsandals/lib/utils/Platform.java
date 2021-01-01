package org.screamingsandals.lib.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Platform {
    JAVA_FLATTENING(false),
    JAVA_LEGACY(true),
    BEDROCK_LEGACY(true);
    // Do you think we will ever get bedrock flattening version?

    private final boolean usingLegacyNames;
}
