package org.screamingsandals.lib.bukkit.world;

import org.bukkit.World;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.WorldHolder;

import java.util.UUID;

public class BukkitWorldHolder extends BasicWrapper<World> implements WorldHolder {

    public BukkitWorldHolder(World wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public UUID getUuid() {
        return wrappedObject.getUID();
    }

    @Override
    public String getName() {
        return wrappedObject.getName();
    }

}
