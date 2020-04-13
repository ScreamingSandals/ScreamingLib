package org.screamingsandals.gamecore.store;

import org.screamingsandals.gamecore.core.adapter.LocationAdapter;

import java.io.Serializable;

public abstract class GameStore implements Serializable {
    private LocationAdapter storeLocation;
}
