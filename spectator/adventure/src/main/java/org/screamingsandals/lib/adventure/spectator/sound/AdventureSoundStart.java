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

package org.screamingsandals.lib.adventure.spectator.sound;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.sound.SoundSource;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public class AdventureSoundStart extends BasicWrapper<Sound> implements SoundStart {
    public AdventureSoundStart(Sound wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @NotNull
    public NamespacedMappingKey soundKey() {
        return NamespacedMappingKey.of(wrappedObject.name().namespace(), wrappedObject.name().value());
    }

    @SuppressWarnings("PatternValidation")
    @Override
    @NotNull
    public SoundStart withSoundKey(@NotNull NamespacedMappingKey soundKey) {
        return new AdventureSoundStart(Sound.sound(Key.key(soundKey.namespace(), soundKey.value()), wrappedObject.source(), wrappedObject.volume(), wrappedObject.pitch()));
    }

    @Override
    @NotNull
    public SoundSource source() {
        return new AdventureSoundSource(wrappedObject.source());
    }

    @Override
    @NotNull
    public SoundStart withSource(@NotNull SoundSource source) {
        return new AdventureSoundStart(Sound.sound(wrappedObject.name(), source.as(Sound.Source.class), wrappedObject.volume(), wrappedObject.pitch()));
    }

    @Override
    public float volume() {
        return wrappedObject.volume();
    }

    @Override
    @NotNull
    public SoundStart withVolume(float volume) {
        return new AdventureSoundStart(Sound.sound(wrappedObject.name(), wrappedObject.source(), volume, wrappedObject.pitch()));
    }

    @Override
    public float pitch() {
        return wrappedObject.pitch();
    }

    @Override
    @NotNull
    public SoundStart withPitch(float pitch) {
        return new AdventureSoundStart(Sound.sound(wrappedObject.name(), wrappedObject.source(), wrappedObject.volume(), pitch));
    }

    @Override
    @NotNull
    public SoundStart.Builder toBuilder() {
        return new AdventureSoundStartBuilder(
                soundKey(),
                source(),
                volume(),
                pitch()
        );
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class AdventureSoundStartBuilder implements SoundStart.Builder {
        private static final SoundSource MASTER = SoundSource.soundSource("master");

        private NamespacedMappingKey soundKey;
        private SoundSource source = MASTER;
        private float volume = 1;
        private float pitch = 1;

        @SuppressWarnings("PatternValidation")
        @Override
        @NotNull
        public SoundStart build() {
            return new AdventureSoundStart(Sound.sound(Key.key(soundKey.namespace(), soundKey.value()), source.as(Sound.Source.class), volume, pitch));
        }
    }
}
