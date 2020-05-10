package org.screamingsandals.lib.commands.bungee.command;

import lombok.Data;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.screamingsandals.lib.commands.Commands;
import org.screamingsandals.lib.commands.common.commands.SubCommand;
import org.screamingsandals.lib.commands.common.environment.CommandEnvironment;
import org.screamingsandals.lib.commands.common.interfaces.CompleteTab;
import org.screamingsandals.lib.commands.common.interfaces.Execute;
import org.screamingsandals.lib.commands.common.language.CommandsLanguage;
import org.screamingsandals.lib.commands.common.wrapper.CommandWrapper;

import java.util.*;

@Data
public class BungeeCommandWrapper implements CommandWrapper<BungeeCommandBase, Command> {
    private final CommandsLanguage commandsLanguage;
    private final BungeeCommandBase commandBase;
    private Command commandInstance;

    public BungeeCommandWrapper(BungeeCommandBase commandBase) {
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

        return new Builder(commandName, aliases.toArray(String[]::new)) {
            @Override
            public void execute(CommandSender sender, String[] args) {
                boolean result;
                try {
                    if (sender instanceof ProxiedPlayer) {
                        ProxiedPlayer player = (ProxiedPlayer) sender;
                        if (!player.hasPermission(commandBase.getPermission())) {
                            commandsLanguage.sendMessage(player, CommandsLanguage.Key.NO_PERMISSIONS);
                        }
                        result = handlePlayerCommand(player, args);
                    } else {
                        try {
                            result = handleConsoleCommand(sender, args);
                        } catch (Throwable ignored) {
                            commandsLanguage.sendMessage(sender, CommandsLanguage.Key.NOT_FOR_CONSOLE);
                            result = true;
                        }
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    result = false;
                }

                if (!result) {
                    final CommandsLanguage commandsLanguage = Commands.getInstance().getCommandLanguage();
                    commandsLanguage.sendMessage(sender, CommandsLanguage.Key.SOMETHINGS_FUCKED); //lol
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
                final var subComplete = (CompleteTab.SubCommandComplete<CommandSender>) commandBase.getSubCommandComplete();
                if (subComplete != null) {
                    return subComplete.complete(sender, Arrays.asList(args));
                }

                if (sender instanceof ProxiedPlayer) {
                    return handlePlayerTab((ProxiedPlayer) sender, args);
                } else {
                    return handleConsoleComplete(sender, args);
                }
            }

        };
    }

    private boolean handlePlayerCommand(ProxiedPlayer player, String[] args) {
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

            final Execute.PlayerSubCommand<ProxiedPlayer> playerSubCommand = subExecutors.get(subCommand);
            if (playerSubCommand == null) {
                return false;
            }

            playerSubCommand.execute(player, convertedArgs);
        }
        return true;
    }

    private boolean handleConsoleCommand(CommandSender console, String[] args) {
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

            final Execute.ConsoleSubCommand<CommandSender> consoleSubCommand = subExecutors.get(subCommand);
            if (consoleSubCommand == null) {
                return false;
            }

            consoleSubCommand.execute(console, convertedArgs);
        }
        return true;
    }

    private List<String> handlePlayerTab(ProxiedPlayer player, String[] args) {
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

            final CompleteTab.PlayerSubCommandComplete<ProxiedPlayer> playerComplete = subCompletes.get(subCommand);
            if (playerComplete == null) {
                return toReturn;
            }

            toReturn.addAll(playerComplete.complete(player, convertedArgs));
        }

        return toReturn;
    }

    private List<String> handleConsoleComplete(CommandSender console, String[] args) {
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

            final CompleteTab.ConsoleSubCommandComplete<CommandSender> consoleSubCommandComplete = subCompletes.get(subCommand);
            if (consoleSubCommandComplete == null) {
                return toReturn;
            }

            toReturn.addAll(consoleSubCommandComplete.complete(console, convertedArgs));
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
