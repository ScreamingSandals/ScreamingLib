/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.sidebar;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Core;
import org.screamingsandals.lib.packet.PacketMapper;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.visuals.Visual;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@ServiceDependencies(dependsOn = {
        Core.class,
        PacketMapper.class
})
public class SidebarManager {
    private static @Nullable SidebarManager manager;
    protected final @NotNull Map<@NotNull UUID, TeamedSidebar<?>> activeSidebars = new HashMap<>();

    @ApiStatus.Internal
    public SidebarManager(@NotNull Controllable controllable) {
        if (manager != null) {
            throw new UnsupportedOperationException("SidebarManager is already initialized!");
        }
        manager = this;

        controllable.disable(this::destroy);
    }

    public static @NotNull Map<@NotNull UUID, @NotNull TeamedSidebar<?>> getActiveSidebars() {
        if (manager == null) {
            throw new UnsupportedOperationException("SidebarManager is not initialized yet!");
        }

        return Map.copyOf(manager.activeSidebars);
    }

    @SuppressWarnings("unchecked")
    @Contract("null -> null")
    public static <T extends TeamedSidebar<T>> @Nullable TeamedSidebar<T> getSidebar(@Nullable UUID uuid) {
        if (manager == null) {
            throw new UnsupportedOperationException("SidebarManager is not initialized yet!");
        }

        return (TeamedSidebar<T>) manager.activeSidebars.get(uuid);
    }

    public static void addSidebar(@NotNull TeamedSidebar<?> sidebar) {
        if (manager == null) {
            throw new UnsupportedOperationException("SidebarManager is not initialized yet!");
        }

        manager.activeSidebars.put(sidebar.uuid(), sidebar);
    }

    public static void removeSidebar(@Nullable UUID uuid) {
        var sidebar = getSidebar(uuid);
        if (sidebar != null) {
            removeSidebar(sidebar);
        }
    }

    public static void removeSidebar(@NotNull TeamedSidebar<?> scoreboard) {
        if (manager == null) {
            throw new UnsupportedOperationException("SidebarManager is not initialized yet!");
        }

        manager.activeSidebars.remove(scoreboard.uuid());
    }

    public static @NotNull Sidebar sidebar() {
        return sidebar(UUID.randomUUID());
    }

    public static @NotNull ScoreSidebar scoreboard() {
        return scoreboard(UUID.randomUUID());
    }

    public static @NotNull Sidebar sidebar(@NotNull UUID uuid) {
        if (manager == null) {
            throw new UnsupportedOperationException("SidebarManager is not initialized yet!");
        }

        final var scoreboard = new SidebarImpl(uuid);
        addSidebar(scoreboard);
        return scoreboard;
    }

    public static @NotNull ScoreSidebar scoreboard(@NotNull UUID uuid) {
        if (manager == null) {
            throw new UnsupportedOperationException("SidebarManager is not initialized yet!");
        }

        final var scoreboard = new ScoreSidebarImpl(uuid);
        addSidebar(scoreboard);
        return scoreboard;
    }

    protected void destroy() {
        Map.copyOf(getActiveSidebars())
                .values()
                .forEach(Visual::destroy);
        activeSidebars.clear();
    }
}
