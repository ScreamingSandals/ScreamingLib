/*
 * Copyright 2025 ScreamingSandals
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
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;

import java.util.Arrays;
import java.util.Collections;

@RequiredArgsConstructor
public enum SkinLayerValues {
    V8(10, 8, 0),
    V9(12, 9, 0),
    V13(13, 13, 0),
    V14(15, 14, 0),
    V16(16, 15, 0),
    V17(17, 17, 0),
    V21_9(16, 21, 9);

    private final int layerValue;
    private final int minVersion;
    private final int patch;

    private static int currentLayerValue;

    public static int findLayerByVersion() {
        if (currentLayerValue == 0) {
            currentLayerValue = Arrays.stream(values())
                    .sorted(Collections.reverseOrder())
                    .filter(value -> Server.isVersion(1, value.minVersion, value.patch))
                    .map(value -> value.layerValue)
                    .findFirst()
                    .orElse(V9.layerValue);
        }
        return currentLayerValue;
    }
}
