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

package org.screamingsandals.lib.economy;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;
import org.screamingsandals.lib.utils.annotations.AbstractService;

@AbstractService
public abstract class EconomyManager {
    private static @Nullable EconomyManager economyManager;

    @ApiStatus.Internal
    public EconomyManager() {
        if (economyManager != null) {
            throw new UnsupportedOperationException("EconomyManager is already initialized!");
        }
        economyManager = this;
    }

    public static boolean isAvailable() {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.isAvailable0();
    }

    public static double getBalance(@NotNull MultiPlatformOfflinePlayer player) {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.getBalance0(player);
    }

    /**
     * Some economy plugins can use world-specific balance (Vault supports it). If this is not supported by the economy plugin, global balance is returned.
     */
    public static double getBalance(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName) {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.getBalance0(player, worldName);
    }

    public static boolean has(@NotNull MultiPlatformOfflinePlayer player, double amount) {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.has0(player, amount);
    }

    /**
     * Some economy plugins can use world-specific balance (Vault supports it). If this is not supported by the economy plugin, global balance is used.
     */
    public static boolean has(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName, double amount) {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.has0(player, worldName, amount);
    }

    public static @NotNull TransactionResult withdrawPlayer(@NotNull MultiPlatformOfflinePlayer player, double amount) {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.withdrawPlayer0(player, amount);
    }

    /**
     * Some economy plugins can use world-specific balance (Vault supports it). If this is not supported by the economy plugin, global balance is used.
     */
    public static @NotNull TransactionResult withdrawPlayer(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName, double amount) {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.withdrawPlayer0(player, worldName, amount);
    }

    public static @NotNull TransactionResult depositPlayer(@NotNull MultiPlatformOfflinePlayer player, double amount) {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.depositPlayer0(player, amount);
    }

    /**
     * Some economy plugins can use world-specific balance (Vault supports it). If this is not supported by the economy plugin, global balance is used.
     */
    public static @NotNull TransactionResult depositPlayer(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName, double amount) {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.depositPlayer0(player, worldName, amount);
    }

    public static @NotNull String getCurrencyNamePlural() {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.getCurrencyPluralName0();
    }

    public static @NotNull String getCurrencyNameSingular() {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.getCurrencyPluralName0();
    }

    protected abstract boolean isAvailable0();

    protected abstract double getBalance0(@NotNull MultiPlatformOfflinePlayer player);

    protected abstract double getBalance0(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName);

    protected abstract boolean has0(@NotNull MultiPlatformOfflinePlayer player, double amount);

    protected abstract boolean has0(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName, double amount);

    protected abstract @NotNull TransactionResult withdrawPlayer0(@NotNull MultiPlatformOfflinePlayer player, double amount);

    protected abstract @NotNull TransactionResult withdrawPlayer0(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName, double amount);

    protected abstract @NotNull TransactionResult depositPlayer0(@NotNull MultiPlatformOfflinePlayer player, double amount);

    protected abstract @NotNull TransactionResult depositPlayer0(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName, double amount);

    protected abstract @NotNull String getCurrencyPluralName0();

    protected abstract @NotNull String getCurrencyNameSingular0();
}
