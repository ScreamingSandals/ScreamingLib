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
import org.screamingsandals.lib.commands.common.interfaces.CompleteTab;
import org.screamingsandals.lib.commands.common.interfaces.Execute;
import org.screamingsandals.lib.commands.common.language.CommandsLanguage;
import org.screamingsandals.lib.commands.common.wrapper.CommandWrapper;

import java.util.*;

@Data
public class BukkitCommandWrapper implements CommandWrapper<BukkitCommandBase, Command> {
    private final CommandsLanguage commandsLanguage;
    private final BukkitCommandBase commandBase;
    private Command commandInstance;

    public BukkitCommandWrapper(BukkitCommandBase commandBase) {
        this.commandsLanguage = CommandEnvironment.getInstance().getCommandLanguage();
        this.commandBase = commandBase;
        commandInstance = createCommandInstance();
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
                            commandsLanguage.sendMessage(player, CommandsLanguage.Key.NO_PERMISSIONS);
                        }
                        result = handlePlayerCommand((Player) commandSender, args);
                    } else {
                        try {
                            result = handleConsoleCommand((ConsoleCommandSender) commandSender, args);
                        } catch (Throwable ignored) {
                            commandsLanguage.sendMessage(commandSender, CommandsLanguage.Key.NOT_FOR_CONSOLE);
                            result = true;
                        }
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    result = false;
                }

                if (!result) {
                    final CommandsLanguage commandsLanguage = Commands.getInstance().getCommandLanguage();
                    commandsLanguage.sendMessage(commandSender, CommandsLanguage.Key.SOMETHINGS_FUCKED); //lol
                }

                return true;
            }

            @SuppressWarnings("unchecked")
            @Override
            @NotNull
            public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String label, @NotNull String[] args) {
                final var subComplete = (CompleteTab.SubCommandComplete<CommandSender>) commandBase.getSubCommandComplete();
                try {
                    if (subComplete != null) {
                        return subComplete.complete(commandSender, Arrays.asList(args));
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
        final var command = commandBase.getPlayerCommandExecutor();
        final var subExecutors = commandBase.getPlayerSubExecutors();
        final List<String> convertedArgs = Arrays.asList(args);

        if (args.length == 0) {
            if (command == null) {
                return false;
            }

            command.execute(player, convertedArgs);
        }

        if (args.length >= 1 && subExecutors.keySet().size() <= 0) {
            commandsLanguage.sendMessage(player, CommandsLanguage.Key.COMMAND_DOES_NOT_EXISTS);
            return false;
        }

        if (args.length >= 1) {
            final String subCommandName = args[0].toLowerCase();
            final SubCommand subCommand = commandBase.getSubCommand(subCommandName);

            if (subCommand == null) {
                return false;
            }

            if (!player.hasPermission(subCommand.getPermission())) {
                commandsLanguage.sendMessage(player, CommandsLanguage.Key.NO_PERMISSIONS);
                return true;
            }

            final Execute.PlayerSubCommand<Player> playerSubCommand = subExecutors.get(subCommand);
            if (playerSubCommand == null) {
                return false;
            }

            playerSubCommand.execute(player, convertedArgs);
        }
        return true;
    }

    private boolean handleConsoleCommand(ConsoleCommandSender console, String[] args) {
        final var command = commandBase.getConsoleCommandExecutor();
        final var subExecutors = commandBase.getConsoleSubExecutors();
        final List<String> convertedArgs = Arrays.asList(args);

        if (args.length == 0) {
            if (command == null) {
                return false;
            }

            command.execute(console, convertedArgs);
        }

        if (args.length >= 1 && subExecutors.keySet().size() <= 0) {
            commandsLanguage.sendMessage(console, CommandsLanguage.Key.COMMAND_DOES_NOT_EXISTS);
            return false;
        }

        if (args.length >= 1) {
            final String subCommandName = args[0].toLowerCase();
            final SubCommand subCommand = commandBase.getSubCommand(subCommandName);

            if (subCommand == null) {
                return false;
            }

            final Execute.ConsoleSubCommand<ConsoleCommandSender> consoleSubCommand = subExecutors.get(subCommand);
            if (consoleSubCommand == null) {
                return false;
            }

            consoleSubCommand.execute(console, convertedArgs);
        }
        return true;
    }

    private List<String> handlePlayerTab(Player player, String[] args) {
        final var complete = commandBase.getPlayerCommandComplete();
        final var subCompletes = commandBase.getPlayerSubCompletes();
        final List<String> convertedArgs = Arrays.asList(args);
        final List<String> toReturn = new LinkedList<>();

        if (args.length == 0) {
            if (complete == null) {
                return toReturn;
            }

            toReturn.addAll(complete.complete(player, convertedArgs));
        }

        if (args.length == 1) {
            final String typed = args[0].toLowerCase();

            for (SubCommand found : commandBase.getSubCommands()) {
                final var permission = found.getPermission();
                final var name = found.getName();

                if (name.startsWith(typed) && !permission.equals("")
                        && (player.hasPermission(permission) || player.hasPermission("sbw.*"))) {
                    toReturn.add(name);
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

            final CompleteTab.PlayerSubCommandComplete<Player> playerComplete = subCompletes.get(subCommand);
            if (playerComplete == null) {
                return toReturn;
            }

            toReturn.addAll(playerComplete.complete(player, convertedArgs));
        }

        return toReturn;
    }

    private List<String> handleConsoleComplete(ConsoleCommandSender console, String[] args) {
        final var complete = commandBase.getConsoleCommandComplete();
        final var subCompletes = commandBase.getConsoleSubCompletes();
        final List<String> convertedArgs = Arrays.asList(args);
        final List<String> toReturn = new LinkedList<>();

        if (args.length == 0) {
            if (complete == null) {
                return toReturn;
            }

            toReturn.addAll(complete.complete(console, convertedArgs));
        }

        if (args.length == 1) {
            final String typed = args[0].toLowerCase();

            for (SubCommand found : commandBase.getSubCommands()) {
                final var permission = found.getPermission();
                final var name = found.getName();

                if (name.startsWith(typed) && !permission.equals("")) {
                    toReturn.add(name);
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

            final CompleteTab.ConsoleSubCommandComplete<ConsoleCommandSender> consoleSubCommandComplete = subCompletes.get(subCommand);
            if (consoleSubCommandComplete == null) {
                return toReturn;
            }

            toReturn.addAll(consoleSubCommandComplete.complete(console, convertedArgs));
        }

        return toReturn;
    }
}
