package org.screamingsandals.lib.gamecore.core;

import org.screamingsandals.lib.gamecore.core.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.store.GameStore;

import java.util.HashMap;
import java.util.Map;

public abstract class GameBuilder<T> {
    private T gameFrame;
    private Map<LocationAdapter, GameStore> gameStores = new HashMap<>();
}
