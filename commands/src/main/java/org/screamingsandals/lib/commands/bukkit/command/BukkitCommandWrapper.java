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
    private final Map<SubCommand, Execute.PlayerSubCommand<Player>> playerSubExecutors;
    private final Map<SubCommand, Execute.ConsoleSubCommand<ConsoleCommandSender>> consoleSubExecutors;
    private final Map<SubCommand, CompleteTab.PlayerSubCommandComplete<Player>> playerSubCompletes;
    private final Map<SubCommand, CompleteTab.ConsoleSubCommandComplete<ConsoleCommandSender>> consoleSubCompletes;

    private final CompleteTab.SubCommandComplete<CommandSender> subCommandComplete;

    public BukkitCommandWrapper(BukkitCommandBase commandBase) {
        this.commandsLanguage = CommandEnvironment.getInstance().getCommandLanguage();
        this.commandBase = commandBase;
        commandInstance = createCommandInstance();

        //COMMANDS
        playerCommand = commandBase.getPlayerCommandExecutor();
        playerCommandComplete = commandBase.getPlayerCommandComplete();
        consoleCommand = commandBase.getConsoleCommandExecutor();
        consoleCommandComplete = commandBase.getConsoleCommandComplete();

        //SUB-COMMANDS
        playerSubExecutors = commandBase.getPlayerSubExecutors();
        consoleSubExecutors = commandBase.getConsoleSubExecutor();
        playerSubCompletes = commandBase.getPlayerSubCompletes();
        consoleSubCompletes = commandBase.getConsoleSubCompletes();

        subCommandComplete = (CompleteTab.SubCommandComplete<CommandSender>) commandBase.getSubCommandComplete();
    }

    public Command createCommandInstance() {
        final String commandName = commandBase.getName();
        List<String> aliases = commandBase.getAliases();

        if (aliases == null) {
            aliases = Collections.emptyList();
        }

        final Command command = new Command(commandName, commandBase.getDescription(), commandBase.getUsage(), aliases) {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String label, @NotNull String[] args) {
                boolean result;
                try {
                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        if (!player.hasPermission(commandBase.getPermission())) {
                            commandsLanguage.sendMessage(player, CommandsLanguage.LangKey.NO_PERMISSIONS);
                        }
                        result = handlePlayerCommand((Player) commandSender, args);
                    } else {
                        try {
                            result = handleConsoleCommand((ConsoleCommandSender) commandSender, args);
                        } catch (Throwable ignored) {
                            commandsLanguage.sendMessage(commandSender, CommandsLanguage.LangKey.NOT_FOR_CONSOLE);
                            result = true;
                        }
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    result = false;
                }

                if (!result) {
                    final CommandsLanguage commandsLanguage = Commands.getInstance().getCommandLanguage();
                    commandsLanguage.sendMessage(commandSender, CommandsLanguage.LangKey.SOMETHINGS_FUCKED); //lol
                }

                return true;
            }

            @Override
            @NotNull
            public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String label, @NotNull String[] args) {
                try {
                    if (subCommandComplete != null) {
                        return subCommandComplete.complete(commandSender, Arrays.asList(args));
                    }

                    if (commandSender instanceof Player) {
                        return handlePlayerTab((Player) commandSender, args);
                    } else {
                        return handleConsoleComplete((ConsoleCommandSender) commandSender, args);
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
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
            if (playerCommand == null) {
                return false;
            }

            playerCommand.execute(player, convertedArgs);
        }

        if (args.length >= 1 && playerSubExecutors.keySet().size() <= 0) {
            commandsLanguage.sendMessage(player, CommandsLanguage.LangKey.COMMAND_DOES_NOT_EXISTS);
            return false;
        }

        if (args.length >= 1) {
            final String subCommandName = args[0].toLowerCase();
            final SubCommand subCommand = commandBase.getSubCommand(subCommandName);

            if (subCommand == null) {
                return false;
            }

            if (!player.hasPermission(subCommand.getPermission())) {
                commandsLanguage.sendMessage(player, CommandsLanguage.LangKey.NO_PERMISSIONS);
                return true;
            }

            final Execute.PlayerSubCommand<Player> playerSubCommand = playerSubExecutors.get(subCommand);
            if (playerSubCommand == null) {
                return false;
            }

            playerSubCommand.execute(player, convertedArgs);
        }
        return true;
    }

    private boolean handleConsoleCommand(ConsoleCommandSender console, String[] args) {
        List<String> convertedArgs = Arrays.asList(args);

        if (args.length == 0) {
            if (consoleCommand == null) {
                return false;
            }

            consoleCommand.execute(console, convertedArgs);
        }

        if (args.length >= 1 && playerSubExecutors.keySet().size() <= 0) {
            commandsLanguage.sendMessage(console, CommandsLanguage.LangKey.COMMAND_DOES_NOT_EXISTS);
            return false;
        }

        if (args.length >= 1) {
            final String subCommandName = args[0].toLowerCase();
            final SubCommand subCommand = commandBase.getSubCommand(subCommandName);

            if (subCommand == null) {
                return false;
            }

            final Execute.ConsoleSubCommand<ConsoleCommandSender> consoleSubCommand = consoleSubExecutors.get(subCommand);
            if (consoleSubCommand == null) {
                return false;
            }

            consoleSubCommand.execute(console, convertedArgs);
        }
        return true;
    }

    private List<String> handlePlayerTab(Player player, String[] args) {
        final List<String> convertedArgs = Arrays.asList(args);
        final List<String> toReturn = new LinkedList<>();

        if (args.length == 0) {
            if (playerCommandComplete == null) {
                return toReturn;
            }

            toReturn.addAll(playerCommandComplete.complete(player, convertedArgs));
        }

        if (args.length == 1) {
            final String subCommandName = args[0].toLowerCase();
            final SubCommand subCommand = commandBase.getSubCommand(subCommandName);

            for (SubCommand found : playerSubExecutors.keySet()) {
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

            final CompleteTab.PlayerSubCommandComplete<Player> playerComplete = playerSubCompletes.get(subCommand);
            if (playerComplete == null) {
                return toReturn;
            }

            toReturn.addAll(playerComplete.complete(player, convertedArgs));
        }

        return toReturn;
    }

    private List<String> handleConsoleComplete(ConsoleCommandSender console, String[] args) {
        final List<String> convertedArgs = Arrays.asList(args);
        final List<String> toReturn = new LinkedList<>();

        if (args.length == 0) {
            if (consoleCommandComplete == null) {
                return toReturn;
            }

            toReturn.addAll(consoleCommandComplete.complete(console, convertedArgs));
        }

        if (args.length == 1) {
            final String subCommandName = args[0].toLowerCase();

            for (SubCommand found : playerSubExecutors.keySet()) {
                if (subCommandName.startsWith(found.getName())) {
                    toReturn.add(found.getName());
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

            final CompleteTab.ConsoleSubCommandComplete<ConsoleCommandSender> consoleSubCommandComplete = consoleSubCompletes.get(subCommand);
            if (consoleSubCommandComplete == null) {
                return toReturn;
            }

            toReturn.addAll(consoleSubCommandComplete.complete(console, convertedArgs));
        }

        return toReturn;
    }
}
