/*
 * Copyright 2025 ScreamingSandals
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

package org.screamingsandals.lib.impl.bungee.spectator.event.click;

import net.md_5.bungee.api.chat.ClickEventCustom;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bungee.spectator.AbstractBungeeBackend;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.nbt.Tag;
import org.screamingsandals.lib.spectator.event.click.Payload;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

public class BungeeClickEventCustom extends BasicWrapper<ClickEventCustom> implements Payload.Custom {
    public BungeeClickEventCustom(@NotNull ClickEventCustom wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull ResourceLocation location() {
        return ResourceLocation.of(wrappedObject.getValue());
    }

    @Override
    public @NotNull Tag tag() {
        if (wrappedObject.getPayload() != null && !wrappedObject.getPayload().isEmpty()) {
            return AbstractBungeeBackend.getSnbtSerializer().deserialize(wrappedObject.getPayload());
        } else {
            return CompoundTag.EMPTY;
        }
    }
}
