package org.screamingsandals.lib.bukkit.economy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.economy.EconomyManager;
import org.screamingsandals.lib.economy.TransactionResult;
import org.screamingsandals.lib.plugin.PluginManager;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnDisable;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

@Service
public class BukkitEconomyManager extends EconomyManager {
    private static final TransactionResult MISSING_PLUGIN_FAILURE = new TransactionResult(0, 0, false, "Vault is not installed or economy is not supported!");
    private static final TransactionResult VAULT_RETURNED_NULL = new TransactionResult(0, 0, false, "Vault returned null!");


    private Economy vaultEcon;

    @OnEnable
    public void onEnable() {
        if (PluginManager.isEnabled(PluginManager.createKey("Vault").orElseThrow())) {
            var econProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (econProvider != null) {
                vaultEcon = econProvider.getProvider();
            } else {
                vaultEcon = Bukkit.getServer().getServicesManager().load(net.milkbowl.vault.economy.Economy.class);
            }
        }
    }

    @OnDisable
    public void onDisable() {
        vaultEcon = null;
    }

    @Override
    protected boolean isAvailable0() {
        return vaultEcon != null;
    }

    @Override
    protected double getBalance0(@NotNull MultiPlatformOfflinePlayer player) {
        if (vaultEcon != null) {
            return vaultEcon.getBalance(player.as(OfflinePlayer.class));
        }
        return 0;
    }

    @Override
    protected double getBalance0(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName) {
        if (vaultEcon != null) {
            return vaultEcon.getBalance(player.as(OfflinePlayer.class), worldName);
        }
        return 0;
    }

    @Override
    protected boolean has0(@NotNull MultiPlatformOfflinePlayer player, double amount) {
        if (vaultEcon != null) {
            return vaultEcon.has(player.as(OfflinePlayer.class), amount);
        }
        return false;
    }

    @Override
    protected boolean has0(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName, double amount) {
        if (vaultEcon != null) {
            return vaultEcon.has(player.as(OfflinePlayer.class), worldName, amount);
        }
        return false;
    }

    @Override
    @NotNull
    protected TransactionResult withdrawPlayer0(@NotNull MultiPlatformOfflinePlayer player, double amount) {
        if (vaultEcon != null) {
            return convert(vaultEcon.withdrawPlayer(player.as(OfflinePlayer.class), amount));
        }
        return MISSING_PLUGIN_FAILURE;
    }

    @Override
    @NotNull
    protected TransactionResult withdrawPlayer0(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName, double amount) {
        if (vaultEcon != null) {
            return convert(vaultEcon.withdrawPlayer(player.as(OfflinePlayer.class), worldName, amount));
        }
        return MISSING_PLUGIN_FAILURE;
    }

    @Override
    @NotNull
    protected TransactionResult depositPlayer0(@NotNull MultiPlatformOfflinePlayer player, double amount) {
        if (vaultEcon != null) {
            return convert(vaultEcon.depositPlayer(player.as(OfflinePlayer.class), amount));
        }
        return MISSING_PLUGIN_FAILURE;
    }

    @Override
    @NotNull
    protected TransactionResult depositPlayer0(@NotNull MultiPlatformOfflinePlayer player, @NotNull String worldName, double amount) {
        if (vaultEcon != null) {
            return convert(vaultEcon.depositPlayer(player.as(OfflinePlayer.class), worldName, amount));
        }
        return MISSING_PLUGIN_FAILURE;
    }

    @NotNull
    private TransactionResult convert(EconomyResponse response) {
        if (response == null) {
            return VAULT_RETURNED_NULL;
        }

        return new TransactionResult(response.amount, response.balance, response.transactionSuccess(), response.errorMessage);
    }
}
