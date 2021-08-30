package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.utils.math.Vector3D;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerVelocityChangeEvent extends CancellableAbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ObjectLink<Vector3D> velocity;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public Vector3D getVelocity() {
        return velocity.get();
    }

    public void setVelocity(Vector3D velocity) {
        this.velocity.set(velocity);
    }
}
