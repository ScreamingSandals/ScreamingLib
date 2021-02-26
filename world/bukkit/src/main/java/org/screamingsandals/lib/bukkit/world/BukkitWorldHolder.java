package org.screamingsandals.lib.bukkit.world;

import org.bukkit.World;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.WorldHolder;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.UUID;

@ConfigSerializable
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
