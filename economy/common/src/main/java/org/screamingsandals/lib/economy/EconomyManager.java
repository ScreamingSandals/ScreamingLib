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

package org.screamingsandals.lib.economy;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;
import org.screamingsandals.lib.utils.annotations.AbstractService;

@AbstractService
public abstract class EconomyManager {
    private static EconomyManager economyManager;

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

    @NotNull
    public static TransactionResult withdrawPlayer(@NotNull MultiPlatformOfflinePlayer player, double amount) {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.withdrawPlayer0(player, amount);
    }

    /**
     * Some economy plugins can use world-specific balance (Vault supports it). If this is not supported by the economy plugin, global balance is used.
     */
    @NotNull
    public static TransactionResult withdrawPlayer(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName, double amount) {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.withdrawPlayer0(player, worldName, amount);
    }

    @NotNull
    public static TransactionResult depositPlayer(@NotNull MultiPlatformOfflinePlayer player, double amount) {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.depositPlayer0(player, amount);
    }

    /**
     * Some economy plugins can use world-specific balance (Vault supports it). If this is not supported by the economy plugin, global balance is used.
     */
    @NotNull
    public static TransactionResult depositPlayer(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName, double amount) {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.depositPlayer0(player, worldName, amount);
    }

    @NotNull
    public static String getCurrencyNamePlural() {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.getCurrencyPluralName0();
    }

    @NotNull
    public static String getCurrencyNameSingular() {
        if (economyManager == null) {
            throw new UnsupportedOperationException("EconomyManager is not initialized yet!");
        }
        return economyManager.getCurrencyPluralName0();
    }

    protected abstract boolean isAvailable0();

    protected abstract double getBalance0(@NotNull MultiPlatformOfflinePlayer player);

    protected abstract double getBalance0(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName);

    protected abstract boolean has0(@NotNull MultiPlatformOfflinePlayer player, double amount);

    protected abstract boolean has0(@NotNull MultiPlatformOfflinePlayer player, String worldName, double amount);

    @NotNull
    protected abstract TransactionResult withdrawPlayer0(@NotNull MultiPlatformOfflinePlayer player, double amount);

    @NotNull
    protected abstract TransactionResult withdrawPlayer0(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName, double amount);

    @NotNull
    protected abstract TransactionResult depositPlayer0(@NotNull MultiPlatformOfflinePlayer player, double amount);

    @NotNull
    protected abstract TransactionResult depositPlayer0(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName, double amount);

    @NotNull
    protected abstract String getCurrencyPluralName0();

    @NotNull
    protected abstract String getCurrencyNameSingular0();
}
