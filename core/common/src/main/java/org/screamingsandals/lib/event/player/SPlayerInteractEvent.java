package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.world.BlockHolder;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerInteractEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    @Nullable
    protected Item item;
    protected Action action;
    @Nullable
    protected BlockHolder blockClicked;
    protected BlockFace blockFace;
    private AbstractEvent.Result useClickedBlock;
    private AbstractEvent.Result useItemInHand;
    @Nullable
    private EquipmentSlotHolder hand;

    /**
     * Sets the cancellation state of this event. A canceled event will not be
     * executed in the server, but will still pass to other plugins
     * <p>
     * Canceling this event will prevent use of food (player won't lose the
     * food item), prevent bows/snowballs/eggs from firing, etc. (player won't
     * lose the ammo)
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        setUseClickedBlock(cancel ? AbstractEvent.Result.DENY : useClickedBlock == AbstractEvent.Result.DENY ? AbstractEvent.Result.DEFAULT : useClickedBlock);
        setUseItemInHand(cancel ? AbstractEvent.Result.DENY : useItemInHand == AbstractEvent.Result.DENY ? AbstractEvent.Result.DEFAULT : useItemInHand);
    }

    /**
     * Check if this event involved a block
     *
     * @return boolean true if it did
     */
    public boolean hasBlock() {
        return this.blockClicked != null;
    }

    /**
     * Check if this event involved an item
     *
     * @return boolean true if it did
     */
    public boolean hasItem() {
        return this.item != null;
    }

    /**
     * Convenience method. Returns the material of the item represented by
     * this event
     *
     * @return Material the material of the item used
     */
    @NotNull
    public MaterialHolder getMaterial() {
        if (!hasItem()) {
            return MaterialMapping.getAir();
        }

        return item.getMaterial();
    }

    /**
     * Gets the cancellation state of this event. Set to true if you want to
     * prevent buckets from placing water and so forth
     *
     * @return boolean cancellation state
     * @deprecated This event has two possible cancellation states. It is
     * possible a call might have the former false, but the latter true, eg in
     * the case of using a firework whilst gliding. Callers should check the
     * relevant methods individually.
     */
    @Deprecated
    @Override
    public boolean isCancelled() {
        return useClickedBlock == Result.DENY;
    }

    public enum Action {
        LEFT_CLICK_BLOCK,
        RIGHT_CLICK_BLOCK,
        LEFT_CLICK_AIR,
        RIGHT_CLICK_AIR,
        PHYSICAL;

        public static List<Action> VALUES = Arrays.asList(values());

        public static Action convert(String name) {
            return VALUES.stream()
                    .filter(next -> next.name().equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(LEFT_CLICK_AIR);
        }
    }
}
