package org.screamingsandals.lib.commands.bukkit.command;

import lombok.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.commands.common.commands.subcommands.SubCommand;
import org.screamingsandals.lib.commands.common.functions.CompleteTab;
import org.screamingsandals.lib.commands.common.functions.Execute;
import org.screamingsandals.lib.commands.common.wrapper.CommandWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
public class BukkitCommandWrapper implements CommandWrapper<BukkitCommandBase, Command> {
    private final BukkitCommandBase commandBase;
    private Command commandInstance;

    //Internal shits for handling commands
    private final Execute.PlayerCommand<Player> playerCommand;
    private final Execute.ConsoleCommand<ConsoleCommandSender> consoleCommand;
    private final CompleteTab.PlayerCommandComplete<Player> playerCommandComplete;
    private final CompleteTab.ConsoleCommandComplete<ConsoleCommandSender> consoleCommandComplete;

    //Internal shits for handling subcommands
    private final Map<SubCommand, Execute.PlayerSubCommand<Player>> playerSubCommands;
    private final Map<SubCommand, Execute.ConsoleSubCommand<ConsoleCommandSender>> consoleSubCommands;
    private final Map<SubCommand, CompleteTab.PlayerSubCommandComplete<Player>> playerSubCompletes;
    private final Map<SubCommand, CompleteTab.ConsoleSubCommandComplete<ConsoleCommandSender>> consoleSubCompletes;

    public BukkitCommandWrapper(BukkitCommandBase commandBase) {
        this.commandBase = commandBase;
        commandInstance = createCommandInstance();

        //COMMANDS
        playerCommand = commandBase.getPlayerCommand();
        playerCommandComplete = commandBase.getPlayerCommandComplete();
        consoleCommand = commandBase.getConsoleCommand();
        consoleCommandComplete = commandBase.getConsoleCommandComplete();

        //SUB-COMMANDS
        playerSubCommands = commandBase.getPlayerSubCommands();
        consoleSubCommands = commandBase.getConsoleSubCommands();
        playerSubCompletes = commandBase.getPlayerSubCompletes();
        consoleSubCompletes = commandBase.getConsoleSubCompletes();
    }

    public Command createCommandInstance() {
        final String commandName = commandBase.getName();
        final List<String> aliases = commandBase.getAliases();

        final Command command = new Command(commandName, commandBase.getDescription(), commandBase.getUsage(), aliases) {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String label, @NotNull String[] args) {
                try {
                    if (commandSender instanceof Player) {
                        handlePlayerCommand((Player) commandSender, label, args);
                    } else {
                        handleConsoleCommand((ConsoleCommandSender) commandSender, label, args);
                    }
                    return true;
                } catch (Throwable tr) {
                    tr.printStackTrace();
                    return false;
                }
            }

            @Override
            @NotNull
            public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String alias1, @NotNull String[] args) {
                //TODO - check sub commands
                try {
                    if (commandSender instanceof Player) {
                        return commandBase.getPlayerCommandComplete().complete((Player) commandSender, Arrays.asList(args));
                    } else {
                        return commandBase.getConsoleCommandComplete().complete((ConsoleCommandSender) commandSender, Arrays.asList(args));
                    }
                } catch (Throwable tr) {
                    tr.printStackTrace();
                    return new ArrayList<>();
                }
            }
        };

        command.setPermission(commandBase.getPermission());
        command.setPermissionMessage("No permissions!"); //TODO
        command.setAliases(aliases);
        command.setDescription(commandBase.getDescription());
        command.setUsage(commandBase.getUsage());

        return command;
    }

    private void handlePlayerCommand(Player player, String label, String[] args) {
        if (!player.hasPermission(commandBase.getPermission())) {
            //TODO - languge!
            player.sendMessage("No permissions!");
            return;
        }

        if (args.length == 0) {

        }

        if (args.length >= 1 && playerSubCommands.keySet().size() <= 0) {
            //TODO - language!
            player.sendMessage("Command not found!");
            return;
        }

        if (args.length >= 1) {

        }
    }

    private void handleConsoleCommand(ConsoleCommandSender console, String label, String[] args) {

    }
}
