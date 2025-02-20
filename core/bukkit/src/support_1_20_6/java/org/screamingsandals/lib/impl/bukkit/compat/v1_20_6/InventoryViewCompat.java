/*
 * Copyright 2025 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.compat.v1_20_6;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

/**
 * Since ee9eafe6744e6911f9a06a27d3b90f22ec5a08e3, InventoryView is not an interface instead of abstract class.
 * That means, calling methods on it now requires more generic {@code invokeinterface} instead of {@code invokevirtual}.
 * <p>
 * Because we need to retain compatible with older versions, we need to keep compiling against older artifact.
 * For newer versions, {@code org.bukkit.craftbukkit.util.Commodore} will fix the compatibility.
 */
@UtilityClass
public class InventoryViewCompat {
    public static @NotNull Inventory getBottomInventory(@NotNull InventoryView inventoryView) {
        return inventoryView.getBottomInventory();
    }
}
