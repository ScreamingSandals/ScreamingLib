package org.screamingsandals.lib.commands.common;

import org.screamingsandals.lib.commands.bukkit.command.BukkitCommandBase;

public class CommandBuilder {

    public static BukkitCommandBase bukkitCommand() {
        return new BukkitCommandBase();
    }

    public static void bungeeCommand() {

    }
}
