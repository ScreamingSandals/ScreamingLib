package org.screamingsandals.lib.commands.common.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.command.CommandSender;
import org.screamingsandals.lib.commands.common.interfaces.CompleteTab;
import org.screamingsandals.lib.commands.common.interfaces.Execute;
import org.screamingsandals.lib.debug.Debug;

import java.util.Collections;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
public abstract class CommandBase<T, Y> extends SubCommandBase<T, Y> {
    private String name;
    private String permission;
    private List<String> aliases;
    private String description;
    private String usage;

    //COMMANDS
    private Execute.PlayerCommand<T> playerCommandExecutor;
    private Execute.ConsoleCommand<Y> consoleCommandExecutor;
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

    public CommandBase<T, Y> handlePlayerCommand(Execute.PlayerCommand<T> execute) {
        if (handleSimpleAdding(playerCommandExecutor)) {
            Debug.info("Registerd command with name " + name, true);
            this.playerCommandExecutor = execute;
        } else {
            Debug.info("you are dumb as fuck!", true);
        }
        return this;
    }

    public CommandBase<T, Y> handleConsoleCommand(Execute.ConsoleCommand<Y> execute) {
        if (handleSimpleAdding(consoleCommandExecutor)) {
            this.consoleCommandExecutor = execute;
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

    public CommandBase<T, Y> addSubCommand(String name, String permission, List<String> aliases) {
        super.addSubCommand(name, permission, aliases);
        return this;
    }

    public CommandBase<T, Y> addSubCommand(String name, String permission) {
        addSubCommand(name, permission, Collections.emptyList());
        return this;
    }

    public CommandBase<T, Y> addSubCommand(String name) {
        addSubCommand(name, null, Collections.emptyList());
        return this;
    }

    public CommandBase<T, Y> addSubCommand(List<SubCommand> subCommands) {
        subCommands.forEach(subCommand -> addSubCommand(subCommand.getName(), subCommand.getPermission(), subCommand.getAliases()));
        return this;
    }

    public CommandBase<T, Y> handleSubPlayerCommand(String name, Execute.PlayerSubCommand<T> execute) {
        super.handleSubPlayerCommand(name, execute);
        return this;
    }

    public CommandBase<T, Y> handleSubConsoleCommand(String name, Execute.ConsoleSubCommand<Y> execute) {
        super.handleSubConsoleCommand(name, execute);
        return this;
    }

    public CommandBase<T, Y> handleSubPlayerTab(String name, CompleteTab.PlayerSubCommandComplete<T> complete) {
        super.handleSubPlayerTab(name, complete);
        return this;
    }

    public CommandBase<T, Y> handleSubConsoleTab(String name, CompleteTab.ConsoleSubCommandComplete<Y> complete) {
        super.handleSubConsoleTab(name, complete);
        return this;
    }

    public CommandBase<T, Y> handleAllSubTab(String name, CompleteTab.SubCommandComplete<CommandSender> complete) {
        super.handleAllSubTab(name, complete);
        return this;
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
