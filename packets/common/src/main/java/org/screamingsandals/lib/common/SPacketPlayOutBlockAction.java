package org.screamingsandals.lib.common;

import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.LocationHolder;

public interface SPacketPlayOutBlockAction {
    void setBlocKLocation(LocationHolder location);

    void setActionId(int actionId);

    void setActionParameter(int actionParameter);

    void setBlockType(BlockHolder block);
}
