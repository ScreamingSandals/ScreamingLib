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

package org.screamingsandals.lib.bungee.spectator.event.hover;

import net.md_5.bungee.api.chat.hover.content.Entity;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bungee.spectator.AbstractBungeeBackend;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.UUID;

public class BungeeEntityContent extends BasicWrapper<Entity> implements EntityContent {
    public BungeeEntityContent(Entity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public UUID id() {
        return UUID.fromString(wrappedObject.getId());
    }

    @Override
    public NamespacedMappingKey type() {
        var type = wrappedObject.getType();
        if (type == null) {
            return NamespacedMappingKey.of("minecraft:pig"); // md_5's nice api said: will be pig if null
        }
        return NamespacedMappingKey.of(type);
    }

    @Override
    @Nullable
    public Component name() {
        return AbstractBungeeBackend.wrapComponent(wrappedObject.getName());
    }
}
