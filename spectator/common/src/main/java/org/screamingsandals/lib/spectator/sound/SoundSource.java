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

package org.screamingsandals.lib.spectator.sound;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Spectator;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;

public interface SoundSource extends Wrapper, RawValueHolder {
    SoundSource MASTER = new SoundSourceLink("master");
    SoundSource MUSIC = new SoundSourceLink("music");
    SoundSource RECORD = new SoundSourceLink("record");
    SoundSource WEATHER = new SoundSourceLink("weather");
    SoundSource BLOCK = new SoundSourceLink("block");
    SoundSource HOSTILE = new SoundSourceLink("hostile");
    SoundSource NEUTRAL = new SoundSourceLink("neutral");
    SoundSource PLAYER = new SoundSourceLink("player");
    SoundSource AMBIENT = new SoundSourceLink("ambient");
    SoundSource VOICE = new SoundSourceLink("voice");

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    static SoundSource soundSource(@NotNull String source) {
        return Spectator.getBackend().soundSource(source);
    }

    @NotNull
    String name();
}
