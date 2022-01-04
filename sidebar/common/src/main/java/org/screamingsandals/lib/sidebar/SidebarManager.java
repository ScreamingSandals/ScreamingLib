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

package org.screamingsandals.lib.sidebar;

import org.screamingsandals.lib.Core;
import org.screamingsandals.lib.packet.PacketMapper;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.visuals.Visual;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service(dependsOn = {
        Core.class,
        PacketMapper.class
})
public class SidebarManager {
    private static SidebarManager manager;
    protected final Map<UUID, TeamedSidebar<?>> activeSidebars = new HashMap<>();

    @Deprecated // internal use only
    public SidebarManager(Controllable controllable) {
        if (manager != null) {
            throw new UnsupportedOperationException("SidebarManager is already initialized!");
        }
        manager = this;

        controllable.disable(this::destroy);
    }

    public static Map<UUID, TeamedSidebar<?>> getActiveSidebars() {
        if (manager == null) {
            throw new UnsupportedOperationException("SidebarManager is not initialized yet!");
        }

        return Map.copyOf(manager.activeSidebars);
    }

    @SuppressWarnings("unchecked")
    public static <T extends TeamedSidebar<T>> Optional<TeamedSidebar<T>> getSidebar(UUID uuid) {
        if (manager == null) {
            throw new UnsupportedOperationException("SidebarManager is not initialized yet!");
        }

        return Optional.ofNullable((TeamedSidebar<T>) manager.activeSidebars.get(uuid));
    }

    public static void addSidebar(TeamedSidebar<?> sidebar) {
        if (manager == null) {
            throw new UnsupportedOperationException("SidebarManager is not initialized yet!");
        }

        manager.activeSidebars.put(sidebar.getUuid(), sidebar);
    }

    public static void removeSidebar(UUID uuid) {
        getSidebar(uuid).ifPresent(SidebarManager::removeSidebar);
    }

    public static void removeSidebar(TeamedSidebar<?> scoreboard) {
        if (manager == null) {
            throw new UnsupportedOperationException("SidebarManager is not initialized yet!");
        }

        manager.activeSidebars.remove(scoreboard.getUuid());
    }

    public static Sidebar sidebar() {
        return sidebar(UUID.randomUUID());
    }

    public static ScoreSidebar scoreboard() {
        return scoreboard(UUID.randomUUID());
    }

    public static Sidebar sidebar(UUID uuid) {
        if (manager == null) {
            throw new UnsupportedOperationException("SidebarManager is not initialized yet!");
        }

        final var scoreboard = manager.sidebar0(uuid);
        addSidebar(scoreboard);
        return scoreboard;
    }

    public static ScoreSidebar scoreboard(UUID uuid) {
        if (manager == null) {
            throw new UnsupportedOperationException("SidebarManager is not initialized yet!");
        }

        final var scoreboard = manager.scoreSidebar0(uuid);
        addSidebar(scoreboard);
        return scoreboard;
    }

    protected Sidebar sidebar0(UUID uuid) {
        return new SidebarImpl(uuid);
    }

    protected ScoreSidebar scoreSidebar0(UUID uuid) {
        return new ScoreSidebarImpl(uuid);
    }

    protected void destroy() {
        Map.copyOf(getActiveSidebars())
                .values()
                .forEach(Visual::destroy);
        manager.activeSidebars.clear();
    }
}
