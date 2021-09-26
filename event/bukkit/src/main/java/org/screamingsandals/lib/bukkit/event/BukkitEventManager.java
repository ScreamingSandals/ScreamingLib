package org.screamingsandals.lib.bukkit.event;

import org.bukkit.Bukkit;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class BukkitEventManager extends EventManager {

    public BukkitEventManager(Controllable controllable) {
        super(controllable);
    }

    public static void init(Controllable controllable) {
        EventManager.init(() -> new BukkitEventManager(controllable));
    }

    @Override
    public boolean isServerThread() {
        return Bukkit.getServer().isPrimaryThread();
    }
}
