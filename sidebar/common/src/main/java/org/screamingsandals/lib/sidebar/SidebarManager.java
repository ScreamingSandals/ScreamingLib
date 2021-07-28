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
