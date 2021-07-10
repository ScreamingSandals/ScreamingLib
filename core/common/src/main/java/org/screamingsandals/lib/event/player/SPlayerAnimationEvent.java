package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerAnimationEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final PlayerAnimationType animationType;

    /**
     * Different types of player animations
     */
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
