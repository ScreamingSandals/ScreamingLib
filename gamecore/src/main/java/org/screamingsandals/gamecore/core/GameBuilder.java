package org.screamingsandals.gamecore.core;

import org.screamingsandals.gamecore.core.adapter.LocationAdapter;
import org.screamingsandals.gamecore.store.GameStore;

import java.util.HashMap;
import java.util.Map;

public abstract class GameBuilder<T> {
    private T gameFrame;
    private Map<LocationAdapter, GameStore> gameStores = new HashMap<>();
}
