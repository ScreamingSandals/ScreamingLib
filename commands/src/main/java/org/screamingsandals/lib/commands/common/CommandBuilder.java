package org.screamingsandals.lib.commands.common;

import org.screamingsandals.lib.commands.bukkit.command.BukkitCommandBase;
import org.screamingsandals.lib.commands.bungee.command.BungeeCommandBase;

public class CommandBuilder {

    public static BukkitCommandBase bukkitCommand() {
        return new BukkitCommandBase();
    }

    public static BungeeCommandBase bungeeCommand() {
        return new BungeeCommandBase();
    }
}
