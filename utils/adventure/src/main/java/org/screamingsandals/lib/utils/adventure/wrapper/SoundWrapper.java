package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.sound.Sound;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.SoundUtils;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Arrays;

@Data
public final class SoundWrapper implements Wrapper {
    private final Sound sound;

    @NotNull
    public Sound asSound() {
        return sound;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (String.class.isAssignableFrom(type)) {
            return (T) sound.name().asString();
        } else if (type.isInstance(sound)) {
            return (T) sound;
        }

        var generalPackageSplit = Arrays.asList(type.getPackageName().split("\\."));
        generalPackageSplit.remove(generalPackageSplit.size() - 1);
        var generalPackage = String.join(".", generalPackageSplit);

        var keyClass = Reflect.getClassSafe(generalPackage + ".key.Key");
        var sourceClass = Reflect.getClassSafe(generalPackage + ".sound.Sound$Source");
        if (keyClass == null || sourceClass == null) {
            throw new UnsupportedOperationException("Not supported! The target adventure is badly relocated!");
        }

        return (T) SoundUtils.soundToPlatform(sound, type, keyClass, sourceClass);
    }
}
