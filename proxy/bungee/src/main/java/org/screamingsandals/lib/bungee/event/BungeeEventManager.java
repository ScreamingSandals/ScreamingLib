package org.screamingsandals.lib.bungee.event;

import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class BungeeEventManager extends EventManager {

    public BungeeEventManager(Controllable controllable) {
        super(controllable);
    }

    public static void init(Controllable controllable) {
        EventManager.init(() -> new BungeeEventManager(controllable));
    }

    @Override
    public boolean isServerThread() {
        return false;
    }
}
