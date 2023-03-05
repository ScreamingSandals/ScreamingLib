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

package org.screamingsandals.lib.spectator.sound;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Spectator;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.api.Wrapper;

public interface SoundSource extends Wrapper, RawValueHolder {
    @NotNull SoundSource MASTER = new SoundSourceLink("master");
    @NotNull SoundSource MUSIC = new SoundSourceLink("music");
    @NotNull SoundSource RECORD = new SoundSourceLink("record");
    @NotNull SoundSource WEATHER = new SoundSourceLink("weather");
    @NotNull SoundSource BLOCK = new SoundSourceLink("block");
    @NotNull SoundSource HOSTILE = new SoundSourceLink("hostile");
    @NotNull SoundSource NEUTRAL = new SoundSourceLink("neutral");
    @NotNull SoundSource PLAYER = new SoundSourceLink("player");
    @NotNull SoundSource AMBIENT = new SoundSourceLink("ambient");
    @NotNull SoundSource VOICE = new SoundSourceLink("voice");

    @Contract(value = "_ -> new", pure = true)
    static @NotNull SoundSource soundSource(@NotNull String source) {
        return Spectator.getBackend().soundSource(source);
    }

    @NotNull String name();
}
