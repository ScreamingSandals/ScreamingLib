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

package org.screamingsandals.lib;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Optional;

/**
 * Adds support for bukkit sound names and capital letters.
 */
@ApiStatus.Experimental // will be replaced with custom adventure in the future
public class SpecialSoundKey extends NamespacedMappingKey {
    protected SpecialSoundKey(String namespace, String key) {
        super(namespace, key);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.SOUND)
    public static Optional<SpecialSoundKey> keyOptional(String combinedString) {
        var matcher = RESOLUTION_PATTERN.matcher(combinedString);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        var namespace = matcher.group("namespace") != null ? matcher.group("namespace").toLowerCase() : "minecraft";
        var key = matcher.group("key").replaceAll(" ", "_").toLowerCase();

        return keyOptional(namespace, key);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.SOUND)
    public static SpecialSoundKey key(String combinedString) {
        return keyOptional(combinedString).orElseThrow(() -> new IllegalArgumentException(combinedString + " doesn't match validation patterns!"));
    }

    public static Optional<SpecialSoundKey> keyOptional(String namespace, String key) {
        if (!VALID_NAMESPACE.matcher(namespace).matches() || !VALID_KEY.matcher(key).matches()) {
            return Optional.empty();
        }

        if (namespace.equals("minecraft")) {
            key = Server.UNSAFE_normalizeSoundKey(key);
        }

        return Optional.of(new SpecialSoundKey(namespace, key));
    }

    public static SpecialSoundKey key(String namespace, String key) {
        return keyOptional(namespace, key).orElseThrow(() -> new IllegalArgumentException(namespace + ":" + key + " doesn't match validation patterns!"));
    }
}
