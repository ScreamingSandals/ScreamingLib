package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerAnimationEvent extends CancellableAbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<PlayerAnimationType> animationType;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public PlayerAnimationType getAnimationType() {
        return animationType.get();
    }

    /**
     * Different types of player animations
     */
    // TODO: holder?
    public enum PlayerAnimationType {
        ARM_SWING;

        public static PlayerAnimationType convert(String cause) {
            try {
                return PlayerAnimationType.valueOf(cause.toUpperCase());
            } catch (IllegalArgumentException ex) {
                return PlayerAnimationType.ARM_SWING;
            }
        }
    }
}
