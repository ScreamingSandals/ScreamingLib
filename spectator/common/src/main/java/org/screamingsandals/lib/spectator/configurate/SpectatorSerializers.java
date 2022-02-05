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

package org.screamingsandals.lib.spectator.configurate;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

@UtilityClass
public class SpectatorSerializers {
    @NotNull
    public TypeSerializerCollection.Builder appendSerializers(TypeSerializerCollection.@NotNull Builder builder) {
        return builder
                .register(SoundStart.class, new SoundStartSerializer())
                .register(SoundStop.class, new SoundStopSerializer())
                .register(Color.class, new ColorSerializer());
    }

    @NotNull
    public TypeSerializerCollection makeSerializers(TypeSerializerCollection.@NotNull Builder builder) {
        return appendSerializers(builder).build();
    }
}
