package org.screamingsandals.lib.sidebar;

import org.screamingsandals.lib.visuals.DatableVisual;
import org.screamingsandals.lib.visuals.LinedVisual;
public interface Sidebar extends LinedVisual<Sidebar>, DatableVisual<Sidebar>, TeamedSidebar<Sidebar> {

    /**
     * Creates new Sidebar.
     *
     * @return created Sidebar
     */
    static Sidebar of() {
        return SidebarManager.sidebar();
    }
}