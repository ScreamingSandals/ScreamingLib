/*
 * Copyright 2023 ScreamingSandals
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
import net.kyori.adventure.sound.SoundStop;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.sound.SoundSource;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public class AdventureSoundStop extends BasicWrapper<SoundStop> implements org.screamingsandals.lib.spectator.sound.SoundStop {
    public AdventureSoundStop(@NotNull SoundStop wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @Nullable NamespacedMappingKey soundKey() {
        var sound = wrappedObject.sound();
        if (sound == null) {
            return null;
        }
        return NamespacedMappingKey.of(sound.namespace(), sound.value());
    }

    @Override
    public @Nullable SoundSource source() {
        var source = wrappedObject.source();
        return source == null ? null : new AdventureSoundSource(source);
    }

    @Override
    public org.screamingsandals.lib.spectator.sound.@NotNull SoundStop withSoundKey(@Nullable NamespacedMappingKey soundKey) {
        return toBuilder().soundKey(soundKey).build();
    }

    @Override
    public org.screamingsandals.lib.spectator.sound.@NotNull SoundStop withSource(@Nullable SoundSource source) {
        return toBuilder().source(source).build();
    }

    @Override
    public org.screamingsandals.lib.spectator.sound.SoundStop.@NotNull Builder toBuilder() {
        return new AdventureSoundStopBuilder(
                soundKey(),
                source()
        );
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class AdventureSoundStopBuilder implements org.screamingsandals.lib.spectator.sound.SoundStop.Builder {

        private @Nullable NamespacedMappingKey soundKey;

        private @Nullable SoundSource source;

        @SuppressWarnings("PatternValidation")
        @Override
        public org.screamingsandals.lib.spectator.sound.@NotNull SoundStop build() {
            if (soundKey == null && source == null) {
                return new AdventureSoundStop(SoundStop.all());
            } else if (source == null) {
                return new AdventureSoundStop(SoundStop.named(
                        Key.key(soundKey.namespace(), soundKey.value())
                ));
            } else if (soundKey == null) {
                return new AdventureSoundStop(SoundStop.source(
                        source.as(Sound.Source.class)
                ));
            } else {
                return new AdventureSoundStop(SoundStop.namedOnSource(
                       Key.key(this.soundKey.namespace(), this.soundKey.value()),
                       source.as(Sound.Source.class)
                ));
            }
        }
    }

}
