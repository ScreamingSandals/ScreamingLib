package org.screamingsandals.lib.minestom.world;

import net.minestom.server.instance.Instance;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.WorldHolder;

import java.util.UUID;

public class MinestomWorldHolder extends BasicWrapper<Instance> implements WorldHolder {

    protected MinestomWorldHolder(Instance wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public UUID getUuid() {
        return wrappedObject.getUniqueId();
    }

    @Override
    public String getName() {
        if (wrappedObject.getData() != null) {
            return wrappedObject.getData().get("name"); //TODO
        }
        return "unknown";
    }

}
