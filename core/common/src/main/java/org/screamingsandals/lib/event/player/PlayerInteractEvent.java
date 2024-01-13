/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.PlatformEvent;
import org.screamingsandals.lib.event.CancellableEvent;
import org.screamingsandals.lib.event.Event;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.item.ItemStackView;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.block.BlockPlacement;

import java.util.List;

public interface PlayerInteractEvent extends CancellableEvent, PlayerEvent, PlatformEvent {

    @Nullable ItemStackView item();

    @NotNull Action action();

    @Nullable BlockPlacement clickedBlock();

    @NotNull BlockFace blockFace();

    @NotNull Result useClickedBlock();

    void useClickedBlock(@NotNull Result useClickedBlock);

    @NotNull Result useItemInHand();

    void useItemInHand(@NotNull Result useItemInHand);

    @Nullable EquipmentSlot hand();

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
    default void cancelled(boolean cancel) {
        useClickedBlock(cancel ? Event.Result.DENY : useClickedBlock() == Event.Result.DENY ? Event.Result.DEFAULT : useClickedBlock());
        useItemInHand(cancel ? Event.Result.DENY : useItemInHand() == Event.Result.DENY ? Event.Result.DEFAULT : useItemInHand());
    }

    /**
     * Check if this event involved a block
     *
     * @return boolean true if it did
     */
    default boolean hasBlock() {
        return clickedBlock() != null;
    }

    /**
     * Check if this event involved an item
     *
     * @return boolean true if it did
     */
    default boolean hasItem() {
        return item() != null;
    }

    /**
     * Convenience method. Returns the material of the item represented by
     * this event
     *
     * @return Material the material of the item used
     */
    default @NotNull ItemType material() {
        var item = item();
        if (item == null) {
            return ItemType.air();
        }

        return item.getType();
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
    default boolean cancelled() {
        return useClickedBlock() == Result.DENY;
    }

    // TODO: holder?
    enum Action {
        LEFT_CLICK_BLOCK,
        RIGHT_CLICK_BLOCK,
        LEFT_CLICK_AIR,
        RIGHT_CLICK_AIR,
        PHYSICAL;

        public static final @NotNull List<@NotNull Action> VALUES = List.of(values());

        public static @NotNull Action convert(@NotNull String name) {
            return VALUES.stream()
                    .filter(next -> next.name().equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(LEFT_CLICK_AIR);
        }
    }
}
