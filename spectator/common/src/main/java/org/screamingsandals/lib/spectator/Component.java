package org.screamingsandals.lib.spectator;

import org.screamingsandals.lib.utils.Wrapper;

public interface Component extends Wrapper {

    static Component empty() {
        return Spectator.getBackend().empty();
    }

}
