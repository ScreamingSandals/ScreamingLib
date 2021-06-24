package org.screamingsandals.lib.sponge.world;

import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.WorldHolder;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.UUID;

public class SpongeWorldHolder extends BasicWrapper<ServerWorld> implements WorldHolder {
    protected SpongeWorldHolder(ServerWorld wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public UUID getUuid() {
        return wrappedObject.getUniqueId();
    }

    @Override
    public String getName() {
        return "unknown"; //todo
    }
}
