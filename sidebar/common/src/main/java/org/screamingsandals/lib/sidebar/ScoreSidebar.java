package org.screamingsandals.lib.sidebar;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.visuals.DatableVisual;

public interface ScoreSidebar extends DatableVisual<ScoreSidebar>, TeamedSidebar<ScoreSidebar> {
    /**
     * Creates new Scoreboard.
     *
     * @return created Scoreboard
     */
    static ScoreSidebar of() {
        return SidebarManager.scoreboard();
    }

    ScoreSidebar entity(String identifier, Component displayName);

    ScoreSidebar score(String identifier, int score);

    ScoreSidebar removeEntity(String identifier);
}
