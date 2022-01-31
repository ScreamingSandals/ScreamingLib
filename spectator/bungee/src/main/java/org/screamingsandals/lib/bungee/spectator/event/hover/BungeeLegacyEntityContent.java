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

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.UUID;

public class BungeeLegacyEntityContent extends BasicWrapper<String> implements EntityContent {
    public BungeeLegacyEntityContent(String snbt) {
        super(snbt);
    }

    // TODO: parse snbt
    @Override
    public UUID id() {
        return null; // TODO
    }

    @Override
    public NamespacedMappingKey type() {
        return null; // TODO
    }

    @Override
    public @Nullable Component name() {
        return null; // TODO
    }

    public static class BungeeLegacyEntityContentBuilder implements EntityContent.Builder {
        private UUID id;
        private NamespacedMappingKey type;
        private Component name;

        @Override
        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder type(NamespacedMappingKey type) {
            this.type = type;
            return this;
        }

        @Override
        public Builder name(@Nullable Component name) {
            this.name = name;
            return this;
        }

        @Override
        public EntityContent build() {
            return new BungeeLegacyEntityContent(
                    "{id: \"" + (id != null ? id.toString() : UUID.randomUUID().toString()) + "\"" +
                            ", type: \"" + (type != null ? type.asString() : "minecraft:pig") + "\""
                            // escape every quote in the json because the json should be string
                            + (name != null ? ", name: \"" + ComponentSerializer.toString(name.as(BaseComponent.class)).replace("\"", "\\\"") + "\"" : "")
                            + "}" // I hope this is correct
            );
        }
    }
}
