package org.screamingsandals.lib.event.player;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerAnimationEvent extends SPlayerEvent {
    private final ImmutableObjectLink<PlayerAnimationType> animationType;

    public SPlayerAnimationEvent(ImmutableObjectLink<PlayerWrapper> player, ImmutableObjectLink<PlayerAnimationType> animationType) {
        super(player);
        this.animationType = animationType;
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
