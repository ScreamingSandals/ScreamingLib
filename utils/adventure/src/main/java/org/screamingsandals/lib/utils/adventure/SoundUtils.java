package org.screamingsandals.lib.utils.adventure;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import org.screamingsandals.lib.utils.reflect.Reflect;

@UtilityClass
public class SoundUtils {
    public final Class<?> NATIVE_SOUND_CLASS
            = Reflect.getClassSafe(String.join(".", "net", "kyori", "adventure", "sound", "Sound"));
    public final Class<?> NATIVE_SOURCE_CLASS
            = Reflect.getClassSafe(String.join(".", "net", "kyori", "adventure", "sound", "Sound$Source"));
    public final Class<?> NATIVE_SOUND_STOP_CLASS
            = Reflect.getClassSafe(String.join(".", "net", "kyori", "adventure", "sound", "SoundStop"));

    public Object soundToPlatform(Sound sound) {
        if (NATIVE_SOUND_CLASS.isInstance(sound)) {
            return sound;
        }
        return soundToPlatform(sound, NATIVE_SOUND_CLASS, KeyUtils.NATIVE_KEY_CLASS, NATIVE_SOURCE_CLASS);
    }

    public Object soundToPlatform(Sound sound, Class<?> soundClass, Class<?> keyClass, Class<?> sourceClass) {
        return Reflect
                .getMethod(soundClass, "sound", keyClass, sourceClass, float.class, float.class)
                .invokeStatic(
                        KeyUtils.keyToPlatform(sound.name(), soundClass),
                        sourceToPlatform(sound.source(), sourceClass),
                        sound.volume(),
                        sound.pitch()
                );
    }

    @SuppressWarnings("unchecked")
    public Sound soundFromPlatform(Object platformObject) {
        if (platformObject instanceof Sound) {
            return (Sound) platformObject;
        }
        return Sound.sound(
                KeyUtils.keyFromPlatform(Reflect.fastInvoke(platformObject, "name")),
                sourceFromPlatform(Reflect.fastInvoke(platformObject, "source")),
                Reflect.fastInvokeResulted(platformObject, "volume").as(float.class),
                Reflect.fastInvokeResulted(platformObject, "pitch").as(float.class)
        );
    }

    public Object sourceToPlatform(Sound.Source source) {
        if (NATIVE_SOURCE_CLASS.isInstance(source)) {
            return source;
        }
        return sourceToPlatform(source, NATIVE_SOURCE_CLASS);
    }

    public Object sourceToPlatform(Sound.Source source, Class<?> sourceClass) {
        return Reflect.findEnumConstant(sourceClass, source.name());
    }

    public Sound.Source sourceFromPlatform(Object platformObject) {
        if (platformObject instanceof Sound.Source) {
            return (Sound.Source) platformObject;
        }
        return Sound.Source.valueOf(Reflect.fastInvoke(platformObject, "name").toString());
    }

    public Object stopSoundToPlatform(SoundStop soundStop) {
        if (NATIVE_SOUND_STOP_CLASS.isInstance(soundStop)) {
            return soundStop;
        }
        return stopSoundToPlatform(soundStop, NATIVE_SOUND_STOP_CLASS, KeyUtils.NATIVE_KEY_CLASS, NATIVE_SOURCE_CLASS);
    }

    public Object stopSoundToPlatform(SoundStop soundStop, Class<?> soundStopClass, Class<?> keyClass, Class<?> sourceClass) {
        if (soundStop.sound() != null && soundStop.source() != null) {
            return Reflect
                    .getMethod(soundStopClass, "namedOnSource", keyClass, sourceClass)
                    .invokeStatic(
                            KeyUtils.keyToPlatform(soundStop.sound(), keyClass),
                            sourceToPlatform(soundStop.source(), sourceClass)
                    );
        } else if (soundStop.sound() != null) {
            return Reflect
                    .getMethod(soundStopClass, "named", keyClass)
                    .invokeStatic(
                            KeyUtils.keyToPlatform(soundStop.sound(), keyClass)
                    );
        } else if (soundStop.source() != null) {
            return Reflect
                    .getMethod(soundStopClass, "source", sourceClass)
                    .invokeStatic(
                            sourceToPlatform(soundStop.source(), sourceClass)
                    );
        } else {
            return Reflect.fastInvoke(soundStopClass, "all");
        }
    }

    public SoundStop stopSoundFromPlatform(Object platformObject) {
        if (platformObject instanceof SoundStop) {
            return (SoundStop) platformObject;
        }
        var sound = Reflect.fastInvoke(platformObject, "sound");
        var source = Reflect.fastInvoke(platformObject, "source");
        if (sound != null && source != null) {
            return SoundStop.namedOnSource(KeyUtils.keyFromPlatform(sound), sourceFromPlatform(source));
        } else if (sound != null) {
            return SoundStop.named(KeyUtils.keyFromPlatform(sound));
        } else if (source != null) {
            return SoundStop.source(sourceFromPlatform(source));
        } else {
            return SoundStop.all();
        }
    }
}
