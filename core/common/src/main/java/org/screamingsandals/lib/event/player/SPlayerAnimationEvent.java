package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SPlayerAnimationEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    PlayerAnimationType getAnimationType();

    /**
     * Different types of player animations
     */
    // TODO: holder?
    enum PlayerAnimationType {
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
