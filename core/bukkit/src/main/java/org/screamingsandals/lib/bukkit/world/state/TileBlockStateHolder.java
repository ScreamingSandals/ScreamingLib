package org.screamingsandals.lib.bukkit.world.state;

import org.bukkit.block.TileState;
import org.screamingsandals.lib.world.state.TileStateHolder;

// TODO: Persistent data container
public class TileBlockStateHolder extends GenericBlockStateHolder implements TileStateHolder {
    protected TileBlockStateHolder(TileState wrappedObject) {
        super(wrappedObject);
    }
}
