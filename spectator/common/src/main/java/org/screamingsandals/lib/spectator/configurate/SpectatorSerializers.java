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
import org.screamingsandals.lib.spectator.Book;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.spectator.title.Title;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

@UtilityClass
public class SpectatorSerializers {
    @NotNull
    public TypeSerializerCollection.Builder appendSerializers(TypeSerializerCollection.@NotNull Builder builder) {
        return builder
                .register(DurationSerializer.INSTANCE)
                .register(SoundStart.class, SoundStartSerializer.INSTANCE)
                .register(SoundStop.class, SoundStopSerializer.INSTANCE)
                .register(Color.class, ColorSerializer.INSTANCE)
                .register(EntityContent.class, EntityContentSerializer.INSTANCE)
                .register(ItemContent.class, ItemContentSerializer.INSTANCE)
                .register(Component.class, ComponentSerializer.INSTANCE)
                .register(Title.class, TitleSerializer.INSTANCE)
                .register(Book.class, BookSerializer.INSTANCE);
    }

    @NotNull
    public TypeSerializerCollection makeSerializers(TypeSerializerCollection.@NotNull Builder builder) {
        return appendSerializers(builder).build();
    }
}
