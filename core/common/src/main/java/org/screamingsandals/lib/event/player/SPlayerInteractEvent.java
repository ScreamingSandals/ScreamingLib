/*
 * Copyright 2022 ScreamingSandals
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
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.item.ItemTypeHolder;;
import org.screamingsandals.lib.item.ItemView;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.block.BlockHolder;

import java.util.Arrays;
import java.util.List;

public interface SPlayerInteractEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    @Nullable
    ItemView item();

    Action action();

    @Nullable
    BlockHolder clickedBlock();

    BlockFace blockFace();

    Result useClickedBlock();

    void useClickedBlock(Result useClickedBlock);

    Result useItemInHand();

    void useItemInHand(Result useItemInHand);

    @Nullable
    EquipmentSlotHolder hand();

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
        useClickedBlock(cancel ? SEvent.Result.DENY : useClickedBlock() == SEvent.Result.DENY ? SEvent.Result.DEFAULT : useClickedBlock());
        useItemInHand(cancel ? SEvent.Result.DENY : useItemInHand() == SEvent.Result.DENY ? SEvent.Result.DEFAULT : useItemInHand());
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
    @NotNull
    default ItemTypeHolder material() {
        if (!hasItem()) {
            return ItemTypeHolder.air();
        }

        return item().getMaterial();
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

        public static List<Action> VALUES = Arrays.asList(values());

        public static Action convert(String name) {
            return VALUES.stream()
                    .filter(next -> next.name().equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(LEFT_CLICK_AIR);
        }
    }
}
