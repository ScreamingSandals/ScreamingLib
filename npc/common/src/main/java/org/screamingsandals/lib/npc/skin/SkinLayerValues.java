package org.screamingsandals.lib.npc.skin;

import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.Server;

import java.util.Arrays;
import java.util.Collections;

@RequiredArgsConstructor
enum SkinLayerValues {
    V9(12, 8),
    V13(13, 13),
    V14(15, 14),
    V16(16, 15),
    V17(17, 17);

    private final int layerValue;
    private final int minVersion;

    public static int findLayerByVersion() {
        return Arrays.stream(values())
                .sorted(Collections.reverseOrder())
                .filter(value -> Server.isVersion(1, value.minVersion))
                .map(value -> value.layerValue)
                .findAny()
                .orElse(V9.layerValue);
    }
}
