/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.npc.skin;

import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.Server;

import java.util.Arrays;
import java.util.Collections;

@RequiredArgsConstructor
public enum SkinLayerValues {
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
