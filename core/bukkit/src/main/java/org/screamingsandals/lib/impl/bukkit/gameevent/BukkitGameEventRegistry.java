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

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.gameevent.GameEvent;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.gameevent.GameEventRegistry;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

import java.util.List;

@Service
public class BukkitGameEventRegistry extends GameEventRegistry {
    public BukkitGameEventRegistry() {
        if (BukkitFeature.GAME_EVENT.isSupported()) {
            specialType(org.bukkit.GameEvent.class, BukkitGameEvent::new);
        }
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull GameEvent> getRegistryItemStream0() {
        if (BukkitFeature.GAME_EVENT.isSupported()) {
            return new SimpleRegistryItemStream<>(
                    org.bukkit.GameEvent.values()::stream,
                    BukkitGameEvent::new,
                    gameEvent -> {
                        var namespaced = gameEvent.getKey();
                        return ResourceLocation.of(namespaced.getNamespace(), namespaced.getKey());
                    },
                    (gameEvent, literal) -> gameEvent.getKey().getKey().contains(literal),
                    (gameEvent, namespace) -> gameEvent.getKey().getNamespace().equals(namespace),
                    List.of()
            );
        } else {
            return SimpleRegistryItemStream.createDummy();
        }
    }

    @Override
    protected @Nullable GameEvent resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (BukkitFeature.GAME_EVENT.isSupported()) {
            var gameEvent = Registry.GAME_EVENT.get(new NamespacedKey(location.namespace(), location.path()));
            if (gameEvent != null) {
                return new BukkitGameEvent(gameEvent);
            }
        }
        return null;
    }
}
