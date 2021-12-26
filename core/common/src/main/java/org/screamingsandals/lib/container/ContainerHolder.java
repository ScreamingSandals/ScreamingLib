package org.screamingsandals.lib.container;

import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Optional;

public interface ContainerHolder extends Wrapper, RawValueHolder {
    boolean holdsInventory();

    Optional<Container> getInventory();
}
