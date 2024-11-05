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
