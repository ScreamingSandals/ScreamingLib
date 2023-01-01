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

package org.screamingsandals.lib.hologram;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Core;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.hologram.event.HologramTouchEvent;
import org.screamingsandals.lib.packet.PacketMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.InteractType;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.visuals.AbstractVisualsManager;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Map;
import java.util.UUID;

@Service(dependsOn = {
        Core.class,
        PacketMapper.class
})
public class HologramManager extends AbstractVisualsManager<Hologram> {
    private static @Nullable HologramManager manager;

    public HologramManager() {
        if (manager != null) {
            throw new UnsupportedOperationException("HologramManager is already initialized");
        }
        manager = this;
    }

    public static @NotNull Hologram hologram(@NotNull LocationHolder holder) {
        return hologram(UUID.randomUUID(), holder, false);
    }

    public static @NotNull Hologram hologram(@NotNull LocationHolder holder, boolean touchable) {
        return hologram(UUID.randomUUID(), holder, touchable);
    }

    public static @NotNull Hologram hologram(@NotNull UUID uuid, @NotNull LocationHolder holder) {
        return hologram(uuid, holder, false);
    }

    public static @NotNull Hologram hologram(@NotNull UUID uuid, @NotNull LocationHolder holder, boolean touchable) {
        if (manager == null) {
            throw new UnsupportedOperationException("HologramManager is not initialized yet!");
        }

        final var hologram = new HologramImpl(uuid, holder, touchable);
        addHologram(hologram);
        return hologram;
    }

    public static @NotNull Map<@NotNull UUID, @NotNull Hologram> getActiveHolograms() {
        if (manager == null) {
            throw new UnsupportedOperationException("HologramManager is not initialized yet!");
        }
        return manager.getActiveVisuals();
    }

    @Contract("null -> null")
    public static @Nullable Hologram getHologram(@Nullable UUID uuid) {
        if (manager == null) {
            throw new UnsupportedOperationException("HologramManager is not initialized yet!");
        }
        return getActiveHolograms().get(uuid);
    }

    public static void addHologram(@NotNull Hologram hologram) {
        if (manager == null) {
            throw new UnsupportedOperationException("HologramManager is not initialized yet!");
        }
        manager.addVisual(hologram.uuid(), hologram);
    }

    public static void removeHologram(@Nullable UUID uuid) {
        var hologram = getHologram(uuid);
        if (hologram != null) {
            removeHologram(hologram);
        }
    }

    public static void removeHologram(@NotNull Hologram hologram) {
        if (manager == null) {
            throw new UnsupportedOperationException("HologramManager is not initialized yet!");
        }
        manager.removeVisual(hologram.uuid());
    }

    @Override
    public void fireVisualTouchEvent(@NotNull PlayerWrapper sender, @NotNull Hologram visual, @NotNull InteractType interactType) {
        EventManager.fireAsync(new HologramTouchEvent(sender, visual, interactType));
    }
}
