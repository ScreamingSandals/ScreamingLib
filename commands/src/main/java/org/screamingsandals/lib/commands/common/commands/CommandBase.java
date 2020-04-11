package org.screamingsandals.lib.commands.common.commands;

import lombok.Data;
import org.bukkit.command.CommandSender;
import org.screamingsandals.lib.commands.common.functions.CompleteTab;
import org.screamingsandals.lib.commands.common.functions.Execute;
import org.screamingsandals.lib.debug.Debug;

import java.util.*;


@Data
public abstract class CommandBase<T, Y> {
    private String name;
    private String permission;
    private List<String> aliases;
    private String description;
    private String usage;
    private List<SubCommand> subCommands = new LinkedList<>();

    //sub commands
    private Map<SubCommand, Execute.PlayerSubCommand<T>> playerSubCommands = new HashMap<>();
    private Map<SubCommand, Execute.ConsoleSubCommand<Y>> consoleSubCommands = new HashMap<>();
    private Map<SubCommand, CompleteTab.PlayerSubCommandComplete<T>> playerSubCompletes = new HashMap<>();
    private Map<SubCommand, CompleteTab.ConsoleSubCommandComplete<Y>> consoleSubCompletes = new HashMap<>();
    private Map<SubCommand, CompleteTab.SubCommandComplete<?>> handleAllSubCompletes = new HashMap<>();

    //COMMANDS
    private Execute.PlayerCommand<T> playerCommand;
    private Execute.ConsoleCommand<Y> consoleCommand;
    private CompleteTab.PlayerCommandComplete<T> playerCommandComplete;
    private CompleteTab.ConsoleCommandComplete<Y> consoleCommandComplete;

    public CommandBase<T, Y> setPermissions(String permission) {
        this.permission = permission;
        return this;
    }

    public CommandBase<T, Y> setAliases(List<String> aliases) {
        this.aliases = aliases;
        return this;
    }

    public CommandBase<T, Y> setDescription(String description) {
        this.description = description;
        return this;
    }

    public CommandBase<T, Y> setUsage(String usage) {
        this.usage = usage;
        return this;
    }

    public CommandBase<T, Y> registerSubCommand(String name, String permission, List<String> aliases) {
        SubCommand subCommand = new SubCommand();
        subCommand.setName(name);
        subCommand.setPermission(permission);
        subCommand.setAliases(aliases);

        if (!isSubCommandRegistered(name)) {
            subCommands.add(subCommand);
        } else {
            Debug.warn("SubCommand " + name + " is already registered!", true);
        }

        Debug.info("Registered sub command " + subCommand.getName(), true);

        return this;
    }

    public CommandBase<T, Y> registerSubCommand(String name, String permission) {
        registerSubCommand(name, permission, Collections.emptyList());
        return this;
    }

    public CommandBase<T, Y> registerSubCommand(String name) {
        registerSubCommand(name, null, Collections.emptyList());
        return this;
    }

    public CommandBase<T, Y> registerSubCommand(List<SubCommand> subCommands) {
        subCommands.forEach(subCommand -> registerSubCommand(subCommand.getName(), subCommand.getPermission(), subCommand.getAliases()));
        return this;
    }

    public CommandBase<T, Y> handlePlayerCommand(Execute.PlayerCommand<T> execute) {
        if (handleSimpleAdding(playerCommand)) {
            Debug.info("Registerd command with name " + name, true);
            this.playerCommand = execute;
        } else {
            Debug.info("you are dumb as fuck!", true);
        }
        return this;
    }

    public CommandBase<T, Y> handleConsoleCommand(Execute.ConsoleCommand<Y> execute) {
        if (handleSimpleAdding(consoleCommand)) {
            this.consoleCommand = execute;
        }
        return this;
    }

    public CommandBase<T, Y> handlePlayerTab(CompleteTab.PlayerCommandComplete<T> complete) {
        if (handleSimpleAdding(playerCommandComplete)) {
            this.playerCommandComplete = complete;
        }
        return this;
    }

    public CommandBase<T, Y> handleConsoleTab(CompleteTab.ConsoleCommandComplete<Y> complete) {
        if (handleSimpleAdding(consoleCommandComplete)) {
            this.consoleCommandComplete = complete;
        }
        return this;
    }

    public CommandBase<T, Y> handleSubPlayerCommand(String name, Execute.PlayerSubCommand<T> execute) {
        final SubCommand subCommand = handleAdding(name, playerSubCommands);

        if (subCommand != null) {
            playerSubCommands.put(subCommand, execute);
        }
        return this;
    }

    public CommandBase<T, Y> handleSubConsoleCommand(String name, Execute.ConsoleSubCommand<Y> execute) {
        final SubCommand subCommand = handleAdding(name, consoleSubCommands);

        if (subCommand != null) {
            consoleSubCommands.put(subCommand, execute);
        }
        return this;
    }

    public CommandBase<T, Y> handleSubPlayerTab(String name, CompleteTab.PlayerSubCommandComplete<T> complete) {
        final SubCommand subCommand = handleAdding(name, playerSubCompletes);

        if (subCommand != null) {
            playerSubCompletes.put(subCommand, complete);
        }
        return this;
    }

    public CommandBase<T, Y> handleSubConsoleTab(String name, CompleteTab.ConsoleSubCommandComplete<Y> complete) {
        final SubCommand subCommand = handleAdding(name, consoleSubCompletes);

        if (subCommand != null) {
            consoleSubCompletes.put(subCommand, complete);
        }
        return this;
    }

    public CommandBase<T, Y> handleAllSubCompletes(String name, CompleteTab.SubCommandComplete<CommandSender> complete) {
        final SubCommand subCommand = handleAdding(name, handleAllSubCompletes);

        if (subCommand != null) {
            handleAllSubCompletes.put(subCommand, complete);
        }
        return this;
    }

    public void register() {

    }

    public boolean isSubCommandRegistered(String name) {
        for (var subCommand : subCommands) {
            if (name.equalsIgnoreCase(subCommand.getName())) {
                return true;
            }
        }
        return false;
    }

    public SubCommand getSubCommand(String name) {
        for (var subCommand : subCommands) {
            if (name.equalsIgnoreCase(subCommand.getName())) {
                return subCommand;
            }
        }
        return null;
    }

    private SubCommand handleAdding(String name, Map<?, ?> map) {
        if (!isSubCommandRegistered(name)) {
            Debug.warn("SubCommand " + name + " is not registered!", true);
            Debug.warn("Can't handle anything.", true);
            return null;
        }

        final SubCommand subCommand = getSubCommand(name);
        if (map.containsKey(subCommand)) {
            Debug.warn("You can't use another handler, one is already defined!", true);
            Debug.warn("SubCommand: " + name, true);
            return null;
        }

        return subCommand;
    }

    private boolean handleSimpleAdding(Object handler) {
        if (handler != null) {
            Debug.warn("You can't use another handler, one is already defined!", true);
            Debug.warn("Command: " + name, true);
            return false;
        }
        return true;
    }
}
