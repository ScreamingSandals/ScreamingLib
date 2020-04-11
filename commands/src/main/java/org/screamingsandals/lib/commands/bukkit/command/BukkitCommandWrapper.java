package org.screamingsandals.lib.commands.bukkit.command;

import lombok.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.commands.Commands;
import org.screamingsandals.lib.commands.common.commands.SubCommand;
import org.screamingsandals.lib.commands.common.environment.CommandEnvironment;
import org.screamingsandals.lib.commands.common.functions.CompleteTab;
import org.screamingsandals.lib.commands.common.functions.Execute;
import org.screamingsandals.lib.commands.common.language.CommandsLanguage;
import org.screamingsandals.lib.commands.common.wrapper.CommandWrapper;

import java.util.*;

@Data
public class BukkitCommandWrapper implements CommandWrapper<BukkitCommandBase, Command> {
    private final CommandsLanguage commandsLanguage;
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
        this.commandsLanguage = CommandEnvironment.getInstance().getCommandLanguage();
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
                boolean result = true;
                try {
                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        if (!player.hasPermission(commandBase.getPermission())) {
                            commandsLanguage.sendMessage(player, commandsLanguage.getNoPermissions());
                        }
                        result = handlePlayerCommand((Player) commandSender, args);
                    } else {
                        result = handleConsoleCommand((ConsoleCommandSender) commandSender, args);
                    }
                } catch (Throwable tr) {
                    tr.printStackTrace();
                }

                if (!result) {
                    final CommandsLanguage commandsLanguage = Commands.getInstance().getCommandLanguage();
                    commandsLanguage.sendMessage(commandSender, commandsLanguage.getSomethingsFucked()); //lol
                }

                return true;
            }

            @Override
            @NotNull
            public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String label, @NotNull String[] args) {
                try {
                    if (commandSender instanceof Player) {
                        return handlePlayerTab((Player) commandSender, args);
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
        command.setAliases(aliases);
        command.setDescription(commandBase.getDescription());
        command.setUsage(commandBase.getUsage());

        return command;
    }

    private boolean handlePlayerCommand(Player player, String[] args) {
        List<String> convertedArgs = Arrays.asList(args);

        if (args.length == 0) {
            playerCommand.execute(player, convertedArgs);
        }

        if (args.length >= 1 && playerSubCommands.keySet().size() <= 0) {
            player.sendMessage("Command not found!");
            return false;
        }

        if (args.length >= 1) {
            final String subCommandName = args[0].toLowerCase();
            final SubCommand subCommand = commandBase.getSubCommand(subCommandName);

            if (subCommand == null) {
                return false;
            }

            if (!player.hasPermission(subCommand.getPermission())) {
                commandsLanguage.sendMessage(player, commandsLanguage.getNoPermissions());
                return true;
            }

            playerSubCommands.get(subCommand).execute(player, convertedArgs);
        }
        return true;
    }

    private boolean handleConsoleCommand(ConsoleCommandSender console, String[] args) {
        return true;
    }

    private List<String> handlePlayerTab(Player player, String[] args) {
        final List<String> convertedArgs = Arrays.asList(args);
        final List<String> toReturn = new LinkedList<>();

        if (args.length == 0) {
            toReturn.addAll(playerCommandComplete.complete(player, convertedArgs));
        }

        if (args.length == 1) {
            final String subCommandName = args[0].toLowerCase();
            final SubCommand subCommand = commandBase.getSubCommand(subCommandName);

            for (SubCommand found : playerSubCommands.keySet()) {
                if (subCommandName.startsWith(found.getName())) {
                    if (player.hasPermission(subCommand.getPermission())) {
                        toReturn.add(found.getName());
                    }
                }
            }

            return toReturn;
        }

        if (args.length > 1) {
            final String subCommandName = args[0].toLowerCase();
            final SubCommand subCommand = commandBase.getSubCommand(subCommandName);

            if (subCommand == null) {
                return toReturn;
            }

            toReturn.addAll(playerSubCompletes.get(subCommand).complete(player, convertedArgs));
        }

        return toReturn;
    }
}
