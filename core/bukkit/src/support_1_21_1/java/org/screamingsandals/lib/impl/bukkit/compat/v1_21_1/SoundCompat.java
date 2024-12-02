/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.compat.v1_21_1;

import lombok.experimental.UtilityClass;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.utils.Version;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Locale;
import java.util.Map;

@UtilityClass
public class SoundCompat {
    public static void fillSoundCache(@NotNull Map<@NotNull String, String> soundCache) {
        if (BukkitFeature.SOUND_KEYED.isSupported()) {
            for (var v : Sound.values()) {
                if ("minecraft".equals(v.getKey().getNamespace())) {
                    soundCache.put(v.name(), v.getKey().getKey());
                }
            }
        } else {
            boolean is1_9 = Version.isVersion(1, 9);
            for (var v : Sound.values()) {
                var craftSound = Reflect.getMethod(ClassStorage.CB.CraftSound, "getSound", Sound.class).invokeStatic(v);
                soundCache.put(v.name(), craftSound.toString());
                if (!is1_9) {
                    // 1.8.8: Bukkit sound keys are different from vanilla keys, let's map them as well (not to mention that some sounds are completly missing, TODO: we need to manually declare some sounds, which were new in 1.8)
                    soundCache.put(craftSound.toString().replace('.', '_').toUpperCase(Locale.ROOT), craftSound.toString());
                }
            }
        }
    }
}
