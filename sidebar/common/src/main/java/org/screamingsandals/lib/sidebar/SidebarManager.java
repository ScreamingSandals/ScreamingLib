package org.screamingsandals.lib.sidebar;

import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.visuals.Visual;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@AbstractService
public abstract class SidebarManager {
    private static SidebarManager manager;
    protected final Map<UUID, TeamedSidebar<?>> activeSidebars = new HashMap<>();

    @Deprecated //INTERNAL USE ONLY!
    public static void init(Supplier<SidebarManager> supplier) {
        if (manager != null) {
            throw new UnsupportedOperationException("SidebarManager is already initialized");
        }
        manager = supplier.get();
    }

    protected SidebarManager(Controllable controllable) {
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

    protected abstract Sidebar sidebar0(UUID uuid);

    protected abstract ScoreSidebar scoreSidebar0(UUID uuid);

    protected void destroy() {
        Map.copyOf(getActiveSidebars())
                .values()
                .forEach(Visual::destroy);
        manager.activeSidebars.clear();
    }
}
