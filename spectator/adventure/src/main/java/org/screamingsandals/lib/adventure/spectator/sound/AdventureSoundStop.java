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

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.sound.SoundSource;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public class AdventureSoundStop extends BasicWrapper<SoundStop> implements org.screamingsandals.lib.spectator.sound.SoundStop {
    public AdventureSoundStop(SoundStop wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @Nullable
    public NamespacedMappingKey soundKey() {
        var sound = wrappedObject.sound();
        if (sound == null) {
            return null;
        }
        return NamespacedMappingKey.of(sound.namespace(), sound.value());
    }

    @Override
    @Nullable
    public SoundSource source() {
        var source = wrappedObject.source();
        return source == null ? null : new AdventureSoundSource(source);
    }

    public static class AdventureSoundStopBuilder implements org.screamingsandals.lib.spectator.sound.SoundStop.Builder {

        @Nullable
        private NamespacedMappingKey key;

        @Nullable
        private SoundSource source;

        @Override
        @NotNull
        public Builder soundKey(@Nullable NamespacedMappingKey key) {
            this.key = key;
            return this;
        }

        @Override
        @NotNull
        public Builder source(@Nullable SoundSource source) {
            this.source = source;
            return this;
        }

        @Override
        @NotNull
        public org.screamingsandals.lib.spectator.sound.SoundStop build() {
            if (key == null && source == null) {
                return new AdventureSoundStop(SoundStop.all());
            } else if (source == null) {
                return new AdventureSoundStop(SoundStop.named(
                        Key.key(key.namespace(), key.value())
                ));
            } else if (key == null) {
                return new AdventureSoundStop(SoundStop.source(
                        source.as(Sound.Source.class)
                ));
            } else {
                return new AdventureSoundStop(SoundStop.namedOnSource(
                       Key.key(this.key.namespace(), this.key.value()),
                       source.as(Sound.Source.class)
                ));
            }
        }
    }

}
