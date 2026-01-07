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

package org.screamingsandals.lib.impl.bungee.spectator.compat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.BaseComponentSerializer;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;

public class PortedComponentSerializerOld<T extends BaseComponent> extends BaseComponentSerializer implements JsonSerializer<T> {
    private final BiConsumer<T, JsonObject> serializer;

    public PortedComponentSerializerOld(BiConsumer<T, JsonObject> serializer) {
        this.serializer = serializer;
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        serialize(object, src, context);
        this.serializer.accept(src, object);
        return object;
    }
}

