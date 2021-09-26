package org.screamingsandals.lib.velocity.event;

import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class SpongeEventManager extends EventManager {

    public SpongeEventManager(Controllable controllable) {
        super(controllable);
    }

    public static void init(Controllable controllable) {
        EventManager.init(() -> new SpongeEventManager(controllable));
    }

    // TODO:
    @Override
    public boolean isServerThread() {
        return true;
    }
}
