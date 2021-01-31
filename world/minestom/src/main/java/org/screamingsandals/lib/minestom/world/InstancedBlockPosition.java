package org.screamingsandals.lib.minestom.world;

import lombok.Getter;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.BlockPosition;
import net.minestom.server.utils.Position;

@Getter
public class InstancedBlockPosition extends BlockPosition {
    private final Instance instance;

    public InstancedBlockPosition(Instance instance, Position position) {
        super(position.getX(), position.getY(), position.getZ());
        this.instance = instance;
    }

    public InstancedBlockPosition(Instance instance, BlockPosition position) {
        super(position.getX(), position.getY(), position.getZ());
        this.instance = instance;
    }

    public InstancedBlockPosition(Instance instance, double x, double y, double z) {
        super(x, y, z);
        this.instance = instance;
    }
}
