package org.screamingsandals.lib.commands.common.commands;

import lombok.Data;
import org.bukkit.command.CommandSender;
import org.screamingsandals.lib.commands.common.interfaces.CompleteTab;
import org.screamingsandals.lib.commands.common.interfaces.Execute;
import org.screamingsandals.lib.debug.Debug;

import java.util.*;

@Data
public abstract class SubCommandBase<T, Y> {
    private final List<SubCommand> subCommands = new LinkedList<>();

    //sub commands
    private Map<SubCommand, Execute.PlayerSubCommand<T>> playerSubExecutors = new HashMap<>();
    private Map<SubCommand, Execute.ConsoleSubCommand<Y>> consoleSubExecutor = new HashMap<>();
    private Map<SubCommand, CompleteTab.PlayerSubCommandComplete<T>> playerSubCompletes = new HashMap<>();
    private Map<SubCommand, CompleteTab.ConsoleSubCommandComplete<Y>> consoleSubCompletes = new HashMap<>();
    private Map<SubCommand, CompleteTab.SubCommandComplete<?>> handleAllSubCompletes = new HashMap<>();
    private CompleteTab.SubCommandComplete<?> subCommandComplete;

    public void register() {
    }

    public SubCommandBase<T, Y> addSubCommand(String name, String permission, List<String> aliases) {
        final var subCommand = new SubCommand(name, permission, aliases);

        if (isSubCommandRegistered(name)) {
            Debug.warn("SubCommand " + name + " is already registered!", true);
            return this;
        }

        subCommands.add(subCommand);

        return this;
    }

    public SubCommandBase<T, Y> addSubCommand(String name, String permission) {
        return addSubCommand(name, permission, Collections.emptyList());
    }

    public SubCommandBase<T, Y> addSubCommand(String name) {
        return addSubCommand(name, null, Collections.emptyList());
    }

    public SubCommandBase<T, Y> addSubCommand(List<SubCommand> subCommands) {
        subCommands.forEach(subCommand -> addSubCommand(subCommand.getName(), subCommand.getPermission(), subCommand.getAliases()));
        return this;
    }

    public SubCommandBase<T, Y> handleSubPlayerCommand(String name, Execute.PlayerSubCommand<T> execute) {
        final SubCommand subCommand = handleAdding(name, playerSubExecutors);

        if (subCommand != null) {
            playerSubExecutors.put(subCommand, execute);
        }
        return this;
    }

    public SubCommandBase<T, Y> handleSubConsoleCommand(String name, Execute.ConsoleSubCommand<Y> execute) {
        final SubCommand subCommand = handleAdding(name, consoleSubExecutor);

        if (subCommand != null) {
            consoleSubExecutor.put(subCommand, execute);
        }
        return this;
    }

    public SubCommandBase<T, Y> handleSubPlayerTab(String name, CompleteTab.PlayerSubCommandComplete<T> complete) {
        final SubCommand subCommand = handleAdding(name, playerSubCompletes);

        if (subCommand != null) {
            playerSubCompletes.put(subCommand, complete);
        }
        return this;
    }

    public SubCommandBase<T, Y> handleSubConsoleTab(String name, CompleteTab.ConsoleSubCommandComplete<Y> complete) {
        final SubCommand subCommand = handleAdding(name, consoleSubCompletes);

        if (subCommand != null) {
            consoleSubCompletes.put(subCommand, complete);
        }
        return this;
    }

    public SubCommandBase<T, Y> handleAllSubTab(String name, CompleteTab.SubCommandComplete<CommandSender> complete) {
        final SubCommand subCommand = handleAdding(name, handleAllSubCompletes);

        if (subCommand != null) {
            handleAllSubCompletes.put(subCommand, complete);
        }
        return this;
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
}
