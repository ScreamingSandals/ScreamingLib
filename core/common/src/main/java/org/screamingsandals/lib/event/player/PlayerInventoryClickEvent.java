/*
 * Copyright 2023 ScreamingSandals
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
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.utils.*;

public interface PlayerInventoryClickEvent extends CancellableEvent, PlayerEvent, PlatformEvent {

    @Nullable ItemStack cursorItem();

    @Nullable ItemStack currentItem();

    void currentItem(@Nullable ItemStack currentItem);

    @Nullable Container clickedInventory();

    @NotNull ClickType clickType();

    @NotNull Container inventory();

    @NotNull InventoryAction action();

    int hotbarButton();

    int slot();

    @NotNull SlotType slotType();

    int rawSlot();

    @NotNull Result result();

    void result(@NotNull Result result);
}