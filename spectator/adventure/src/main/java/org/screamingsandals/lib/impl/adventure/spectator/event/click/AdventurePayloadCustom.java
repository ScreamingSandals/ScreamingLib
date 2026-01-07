/*
 * Copyright 2026 ScreamingSandals
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

package org.screamingsandals.lib.impl.adventure.spectator.event.click;

import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.nbt.Tag;
import org.screamingsandals.lib.spectator.event.click.Payload;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

public class AdventurePayloadCustom extends BasicWrapper<ClickEvent.Payload.Custom> implements Payload.Custom {
    public AdventurePayloadCustom(ClickEvent.Payload.@NotNull Custom wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull ResourceLocation location() {
        return ResourceLocation.of(wrappedObject.key().namespace(), wrappedObject.key().value());
    }

    @Override
    public @NotNull Tag tag() {
        return AdventureBackend.getSnbtSerializer().deserialize(wrappedObject.nbt().string());
    }
}
