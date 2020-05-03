package org.screamingsandals.lib.commands.common;

import org.screamingsandals.lib.commands.bukkit.command.BukkitSubCommandBase;

public class SubCommandBuilder {

    public static BukkitSubCommandBase bukkitSubCommand() {
        return new BukkitSubCommandBase();
    }
}
