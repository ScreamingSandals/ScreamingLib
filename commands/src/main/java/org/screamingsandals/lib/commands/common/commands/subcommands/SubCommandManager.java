package org.screamingsandals.lib.commands.common.commands.subcommands;

import lombok.Data;
import org.screamingsandals.lib.commands.bukkit.command.BukkitCommandBase;

import java.util.HashMap;

@Data
public class SubCommandManager {
    private HashMap<BukkitCommandBase, SubCommand> subCommands = new HashMap<>();
}
