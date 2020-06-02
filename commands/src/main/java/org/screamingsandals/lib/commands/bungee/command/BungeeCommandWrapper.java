package org.screamingsandals.lib.commands.bungee.command;

import lombok.Data;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.screamingsandals.lib.commands.Commands;
import org.screamingsandals.lib.commands.common.commands.SubCommand;
import org.screamingsandals.lib.commands.common.interfaces.Completable;
import org.screamingsandals.lib.commands.common.interfaces.Executable;
import org.screamingsandals.lib.commands.common.language.CommandLanguage;
import org.screamingsandals.lib.commands.common.wrapper.CommandWrapper;

import java.util.*;

@Data
public class BungeeCommandWrapper implements CommandWrapper<BungeeCommandBase, Command> {
    private final BungeeCommandBase commandBase;
    private Command commandInstance;

    public BungeeCommandWrapper(BungeeCommandBase commandBase) {
        this.commandBase = commandBase;
        commandInstance = createCommandInstance();
    }

    public Command createCommandInstance() {
        final var commandName = commandBase.getName();
        var aliases = commandBase.getAliases();

        if (aliases == null) {
            aliases = Collections.emptyList();
        }

        return new Builder(commandName, aliases.toArray(String[]::new)) {
            @Override
            public void execute(CommandSender sender, String[] args) {
                final var commandsLanguage = Commands.getInstance().getCommandLanguage();
                boolean result;
                try {
                    if (sender instanceof ProxiedPlayer) {
                        final var player = (ProxiedPlayer) sender;
                        if (!player.hasPermission(commandBase.getPermission())) {
                            commandsLanguage.sendMessage(player, CommandLanguage.Key.NO_PERMISSIONS);
                        }
                        result = handleCommand(player, args, commandBase.getPlayerExecutable(), commandBase.getPlayerSubExecutors());
                    } else {
                        try {
                            result = handleCommand(sender, args, commandBase.getConsoleExecutable(), commandBase.getConsoleSubExecutors());
                        } catch (Throwable ignored) {
                            commandsLanguage.sendMessage(sender, CommandLanguage.Key.NOT_FOR_CONSOLE);
                            result = true;
                        }
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    result = false;
                }

                if (!result) {
                    commandsLanguage.sendMessage(sender, CommandLanguage.Key.SOMETHINGS_FUCKED); //lol
                }
            }

            @Override
            public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
                if (sender instanceof ProxiedPlayer) {
                    return handleTab((ProxiedPlayer) sender, args, commandBase.getPlayerCompletable(), commandBase.getPlayerSubCompletes());
                } else {
                    return handleTab(sender, args, commandBase.getConsoleCompletable(), commandBase.getConsoleSubCompletes());
                }
            }

        };
    }

    private <T> boolean handleCommand(T sender, String[] args, Executable<T> command, Map<SubCommand, Executable<T>> subCommands) {
        final var commandsLanguage = Commands.getInstance().getCommandLanguage();
        final boolean areSubCommandsEmpty = commandBase.getSubCommands().isEmpty();
        final var convertedArgs = Arrays.asList(args);

        if (args.length >= 1) {
            final var subCommandName = args[0].toLowerCase();

            if (!areSubCommandsEmpty && !commandBase.isSubCommandRegistered(subCommandName)) {
                commandsLanguage.sendMessage(sender, CommandLanguage.Key.COMMAND_DOES_NOT_EXISTS);
                return true;
            }

            if (subCommands.size() < 1) {
                return false;
            }

            final var subCommand = commandBase.getSubCommand(subCommandName);

            if (areSubCommandsEmpty) {
                command.execute(sender, convertedArgs);
                return true;
            }

            if (subCommand == null) {
                return false;
            }

            if (sender instanceof ProxiedPlayer) {
                final var player = (ProxiedPlayer) sender;
                if (!player.hasPermission(subCommand.getPermission())) {
                    commandsLanguage.sendMessage(player, CommandLanguage.Key.NO_PERMISSIONS);
                    return true;
                }
            }

            final var subExecutor = subCommands.get(subCommand);
            if (subExecutor == null) {
                return false;
            }

            subExecutor.execute(sender, convertedArgs);
            return true;
        }

        if (command == null) {
            return false;
        }

        command.execute(sender, convertedArgs);
        return true;
    }

    private <T> List<String> handleTab(T sender, String[] args, Completable<T> complete, Map<SubCommand, Completable<T>> subCompletes) {
        final var convertedArgs = Arrays.asList(args);
        final var areSubCommandsEmpty = commandBase.getSubCommands().isEmpty();
        final var toReturn = new LinkedList<String>();

        if (args.length == 0) {
            if (complete == null) {
                return toReturn;
            }

            toReturn.addAll(complete.complete(sender, convertedArgs));
        }

        if (args.length == 1) {
            if (areSubCommandsEmpty) {
                toReturn.addAll(complete.complete(sender, convertedArgs));
                return toReturn;
            }

            final var typed = args[0].toLowerCase();
            for (var found : commandBase.getSubCommands()) {
                final var permission = found.getPermission();
                final var name = found.getName();

                if (sender instanceof ProxiedPlayer) {
                    final var player = (ProxiedPlayer) sender;
                    if (name.startsWith(typed)
                            && !permission.equals("")
                            && player.hasPermission(permission)) {
                        toReturn.add(name);
                    }
                } else {
                    toReturn.add(name);
                }
            }
            return toReturn;
        }

        if (args.length > 1) {
            if (commandBase.getSubCommands().isEmpty()) {
                toReturn.addAll(complete.complete(sender, convertedArgs));
                return toReturn;
            }

            final var subCommandName = args[0].toLowerCase();
            final var subCommand = commandBase.getSubCommand(subCommandName);

            if (subCommand == null) {
                return toReturn;
            }

            final var completable = subCompletes.get(subCommand);
            if (completable == null) {
                return toReturn;
            }
            toReturn.addAll(completable.complete(sender, convertedArgs));
        }
        return toReturn;
    }

    private abstract static class Builder extends Command implements TabExecutor {

        public Builder(String name, String[] aliases) {
            super(name, null, aliases);
        }

        @Override
        public void execute(CommandSender sender, String[] args) {

        }

        @Override
        public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
            return null;
        }
    }

}
