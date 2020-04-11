package org.screamingsandals.lib.commands.bungee.command;

import lombok.Data;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.screamingsandals.lib.commands.Commands;
import org.screamingsandals.lib.commands.common.commands.SubCommand;
import org.screamingsandals.lib.commands.common.environment.CommandEnvironment;
import org.screamingsandals.lib.commands.common.functions.CompleteTab;
import org.screamingsandals.lib.commands.common.functions.Execute;
import org.screamingsandals.lib.commands.common.language.CommandsLanguage;
import org.screamingsandals.lib.commands.common.wrapper.CommandWrapper;

import java.util.*;

@Data
public class BungeeCommandWrapper implements CommandWrapper<BungeeCommandBase, Command> {
    private final CommandsLanguage commandsLanguage;
    private final BungeeCommandBase commandBase;
    private Command commandInstance;

    //Internal shits for handling commands
    private final Execute.PlayerCommand<ProxiedPlayer> playerCommand;
    private final Execute.ConsoleCommand<CommandSender> consoleCommand;
    private final CompleteTab.PlayerCommandComplete<ProxiedPlayer> playerCommandComplete;
    private final CompleteTab.ConsoleCommandComplete<CommandSender> consoleCommandComplete;

    //Internal shits for handling subcommands
    private final Map<SubCommand, Execute.PlayerSubCommand<ProxiedPlayer>> playerSubExecutors;
    private final Map<SubCommand, Execute.ConsoleSubCommand<CommandSender>> consoleSubExecutors;
    private final Map<SubCommand, CompleteTab.PlayerSubCommandComplete<ProxiedPlayer>> playerSubCompletes;
    private final Map<SubCommand, CompleteTab.ConsoleSubCommandComplete<CommandSender>> consoleSubCompletes;

    private final CompleteTab.SubCommandComplete<CommandSender> subCommandComplete;

    public BungeeCommandWrapper(BungeeCommandBase commandBase) {
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

        return new Builder(commandName, aliases.toArray(String[]::new)) {
            @Override
            public void execute(CommandSender sender, String[] args) {
                boolean result;
                try {
                    if (sender instanceof ProxiedPlayer) {
                        ProxiedPlayer player = (ProxiedPlayer) sender;
                        if (!player.hasPermission(commandBase.getPermission())) {
                            commandsLanguage.sendMessage(player, commandsLanguage.getNoPermissions());
                        }
                        result = handlePlayerCommand(player, args);
                    } else {
                        result = handleConsoleCommand(sender, args);
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    result = false;
                }

                if (!result) {
                    final CommandsLanguage commandsLanguage = Commands.getInstance().getCommandLanguage();
                    commandsLanguage.sendMessage(sender, commandsLanguage.getSomethingsFucked()); //lol
                }
            }

            @Override
            public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
                if (subCommandComplete != null) {
                    return subCommandComplete.complete(sender, Arrays.asList(args));
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
        List<String> convertedArgs = Arrays.asList(args);

        if (args.length == 0) {
            if (playerCommand == null) {
                return false;
            }

            playerCommand.execute(player, convertedArgs);
        }

        if (args.length >= 1 && playerSubExecutors.keySet().size() <= 0) {
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

            final Execute.PlayerSubCommand<ProxiedPlayer> playerSubCommand = playerSubExecutors.get(subCommand);
            if (playerSubCommand == null) {
                return false;
            }

            playerSubCommand.execute(player, convertedArgs);
        }
        return true;
    }

    private boolean handleConsoleCommand(CommandSender console, String[] args) {
        List<String> convertedArgs = Arrays.asList(args);

        if (args.length == 0) {
            if (consoleCommand == null) {
                return false;
            }

            consoleCommand.execute(console, convertedArgs);
        }

        if (args.length >= 1 && playerSubExecutors.keySet().size() <= 0) {
            console.sendMessage("Command not found!");
            return false;
        }

        if (args.length >= 1) {
            final String subCommandName = args[0].toLowerCase();
            final SubCommand subCommand = commandBase.getSubCommand(subCommandName);

            if (subCommand == null) {
                return false;
            }

            final Execute.ConsoleSubCommand<CommandSender> consoleSubCommand = consoleSubExecutors.get(subCommand);
            if (consoleSubCommand == null) {
                return false;
            }

            consoleSubCommand.execute(console, convertedArgs);
        }
        return true;
    }

    private List<String> handlePlayerTab(ProxiedPlayer player, String[] args) {
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

            final CompleteTab.PlayerSubCommandComplete<ProxiedPlayer> playerComplete = playerSubCompletes.get(subCommand);
            if (playerComplete == null) {
                return toReturn;
            }

            toReturn.addAll(playerComplete.complete(player, convertedArgs));
        }

        return toReturn;
    }

    private List<String> handleConsoleComplete(CommandSender console, String[] args) {
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

            final CompleteTab.ConsoleSubCommandComplete<CommandSender> consoleSubCommandComplete = consoleSubCompletes.get(subCommand);
            if (consoleSubCommandComplete == null) {
                return toReturn;
            }

            toReturn.addAll(consoleSubCommandComplete.complete(console, convertedArgs));
        }

        return toReturn;
    }

    private abstract class Builder extends Command implements TabExecutor {

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
