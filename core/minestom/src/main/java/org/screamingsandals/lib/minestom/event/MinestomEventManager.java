package org.screamingsandals.lib.minestom.event;

import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class MinestomEventManager extends EventManager {
    public MinestomEventManager(Controllable controllable) {
        super(controllable);
    }

    public static void init(Controllable controllable) {
        EventManager.init(() -> new MinestomEventManager(controllable));
    }

    @Override
    public boolean isServerThread() {
        return true;
    }
}
