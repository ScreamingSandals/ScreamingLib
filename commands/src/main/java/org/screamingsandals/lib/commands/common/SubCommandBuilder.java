package org.screamingsandals.lib.commands.common;

import org.screamingsandals.lib.commands.bukkit.command.BukkitSubCommandBase;
import org.screamingsandals.lib.commands.bungee.command.BungeeSubCommandBase;

public class SubCommandBuilder {

    public static BukkitSubCommandBase bukkitSubCommand() {
        return new BukkitSubCommandBase();
    }

    public static BungeeSubCommandBase bungeeSubCommand() {
        return new BungeeSubCommandBase();
    }
}
