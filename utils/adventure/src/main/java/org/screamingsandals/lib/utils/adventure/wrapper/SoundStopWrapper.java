package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.sound.SoundStop;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.SoundUtils;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Arrays;

@Data
public final class SoundStopWrapper implements Wrapper {
    private final SoundStop soundStop;

    @NonNull
    public SoundStop asSoundStop() {
        return soundStop;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type.isInstance(soundStop)) {
            return (T) soundStop;
        }

        var generalPackageSplit = Arrays.asList(type.getPackageName().split("\\."));
        generalPackageSplit.remove(generalPackageSplit.size() - 1);
        var generalPackage = String.join(".", generalPackageSplit);

        var keyClass = Reflect.getClassSafe(generalPackage + ".key.Key");
        var sourceClass = Reflect.getClassSafe(generalPackage + ".sound.Sound$Source");
        if (keyClass == null || sourceClass == null) {
            throw new UnsupportedOperationException("Not supported! The target adventure is badly relocated!");
        }

        return (T) SoundUtils.stopSoundToPlatform(soundStop, type, keyClass, sourceClass);
    }
}
