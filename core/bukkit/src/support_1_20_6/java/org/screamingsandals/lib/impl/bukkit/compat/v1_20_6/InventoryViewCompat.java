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
// TODO: compile it also using newer artifact to not depend on Commodore
@UtilityClass
public class InventoryViewCompat {
    public static @NotNull Inventory getBottomInventory(@NotNull InventoryView inventoryView) {
        return inventoryView.getBottomInventory();
    }
}
