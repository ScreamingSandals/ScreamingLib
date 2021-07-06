package org.screamingsandals.lib.bukkit.sidebar;

import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.sidebar.ScoreSidebar;
import org.screamingsandals.lib.sidebar.Sidebar;
import org.screamingsandals.lib.sidebar.SidebarManager;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.UUID;

@Service(dependsOn = {
        EventManager.class,
        PlayerMapper.class
})
public class BukkitSidebarManager extends SidebarManager {

    public static void init(Controllable controllable) {
        SidebarManager.init(() -> new BukkitSidebarManager(controllable));
    }

    protected BukkitSidebarManager(Controllable controllable) {
        super(controllable);

        EventManager.getDefaultEventManager().register(SPlayerLeaveEvent.class, this::onLeave);
    }

    private void onLeave(SPlayerLeaveEvent event) {
        if (activeSidebars.isEmpty()) {
            return;
        }

        getActiveSidebars().forEach((key, scoreboard) -> {
            if (scoreboard.getViewers().contains(event.getPlayer())) {
                scoreboard.removeViewer(event.getPlayer());
            }
            if (!scoreboard.hasViewers()) {
                removeSidebar(scoreboard);
            }
        });
    }

    @Override
    protected Sidebar sidebar0(UUID uuid) {
        return new BukkitSidebar(uuid);
    }

    @Override
    protected ScoreSidebar scoreSidebar0(UUID uuid) {
        return new BukkitScoreSidebar(uuid);
    }
}
