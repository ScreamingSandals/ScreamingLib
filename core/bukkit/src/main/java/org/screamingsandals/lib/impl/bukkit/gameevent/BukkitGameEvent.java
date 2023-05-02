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

package org.screamingsandals.lib.impl.bukkit.gameevent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.gameevent.GameEvent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;

public class BukkitGameEvent extends BasicWrapper<org.bukkit.GameEvent> implements GameEvent {

    public BukkitGameEvent(@NotNull org.bukkit.GameEvent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.getKey().toString();
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof org.bukkit.GameEvent || object instanceof GameEvent) {
            return equals(object);
        }
        return equals(GameEvent.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public @NotNull ResourceLocation location() {
        var bukkitKey = wrappedObject.getKey();
        return ResourceLocation.of(bukkitKey.getNamespace(), bukkitKey.getKey());
    }
}
