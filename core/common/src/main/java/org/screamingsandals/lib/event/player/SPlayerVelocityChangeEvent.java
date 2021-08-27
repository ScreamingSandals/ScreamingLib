package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.utils.math.Vector3D;

@EqualsAndHashCode(callSuper = false)
public class SPlayerVelocityChangeEvent extends SPlayerCancellableEvent {
    private final ObjectLink<Vector3D> velocity;

    public SPlayerVelocityChangeEvent(ImmutableObjectLink<PlayerWrapper> player,
                                      ObjectLink<Vector3D> velocity) {
        super(player);
        this.velocity = velocity;
    }

    public Vector3D getVelocity() {
        return velocity.get();
    }

    public void setVelocity(Vector3D velocity) {
        this.velocity.set(velocity);
    }
}
