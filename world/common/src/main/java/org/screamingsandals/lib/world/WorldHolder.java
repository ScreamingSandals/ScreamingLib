package org.screamingsandals.lib.world;

import org.screamingsandals.lib.utils.Wrapper;

import java.util.UUID;

public interface WorldHolder extends Wrapper {

    UUID getUuid();

    String getName();

}
