package org.screamingsandals.lib.world;

import org.screamingsandals.lib.utils.Wrapper;

import java.io.Serializable;
import java.util.UUID;

public interface WorldHolder extends Wrapper, Serializable {

    UUID getUuid();

    String getName();

}
