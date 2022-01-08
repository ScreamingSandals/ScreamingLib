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

package org.screamingsandals.lib.adventure.spectator.event.hover;

import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.UUID;

public class AdventureEntityContent extends BasicWrapper<HoverEvent.ShowEntity> implements EntityContent {
    public AdventureEntityContent(HoverEvent.ShowEntity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public UUID id() {
        return wrappedObject.id();
    }

    @Override
    public NamespacedMappingKey type() {
        return NamespacedMappingKey.of(wrappedObject.type().asString());
    }

    @Override
    @Nullable
    public Component name() {
        return AdventureBackend.wrapComponent(wrappedObject.name());
    }
}
