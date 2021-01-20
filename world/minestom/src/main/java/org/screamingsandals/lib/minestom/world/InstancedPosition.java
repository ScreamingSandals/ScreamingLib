package org.screamingsandals.lib.minestom.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.Position;

@EqualsAndHashCode(callSuper = false)
@Data
public class InstancedPosition extends Position {
    private Instance instance;

    public InstancedPosition(Instance instance, Position position) {
        super(position.getX(), position.getY(), position.getZ(), position.getYaw(), position.getPitch());
        this.instance = instance;
    }

    public InstancedPosition(Instance instance, float x, float y, float z, float yaw, float pitch) {
       super(x, y, z, yaw, pitch);
       this.instance = instance;
    }

    public InstancedPosition(Instance instance, float x, float y, float z) {
        super(x, y, z, 0, 0);
        this.instance = instance;
    }

    public InstancedPosition(Instance instance) {
        super(0, 0, 0);
        this.instance = instance;
    }
}
