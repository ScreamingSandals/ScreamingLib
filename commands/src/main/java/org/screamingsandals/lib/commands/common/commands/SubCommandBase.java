package org.screamingsandals.lib.commands.common.commands;

import lombok.Data;
import org.screamingsandals.lib.commands.common.interfaces.Completable;
import org.screamingsandals.lib.commands.common.interfaces.CompleteTab;
import org.screamingsandals.lib.commands.common.interfaces.Executable;
import org.screamingsandals.lib.commands.common.interfaces.Execute;
import org.screamingsandals.lib.debug.Debug;

import java.util.*;

@Data
public abstract class SubCommandBase<T, Y> {
    private final List<SubCommand> subCommands = new LinkedList<>();

    //sub commands
    private Map<SubCommand, Executable<T>> playerSubExecutors = new HashMap<>();
    private Map<SubCommand, Executable<Y>> consoleSubExecutors = new HashMap<>();
    private Map<SubCommand, Completable<T>> playerSubCompletes = new HashMap<>();
    private Map<SubCommand, Completable<Y>> consoleSubCompletes = new HashMap<>();

    public void register() {
    }

    public SubCommandBase<T, Y> addSubCommand(SubCommand subCommand) {
        final var name = subCommand.getName();
        if (isSubCommandRegistered(name)) {
            Debug.warn("SubCommand " + name + " is already registered!", true);
            return this;
        }

        subCommands.add(subCommand);
        return this;
    }

    public SubCommandBase<T, Y> addSubCommand(String name, String permission, List<String> aliases) {
        final var subCommand = new SubCommand(name, permission, aliases);
        return addSubCommand(subCommand);
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
        final var subCommand = handleAdding(name, playerSubExecutors);

        if (subCommand != null) {
            playerSubExecutors.put(subCommand, execute);
        }
        return this;
    }

    public SubCommandBase<T, Y> handleSubConsoleCommand(String name, Execute.ConsoleSubCommand<Y> execute) {
        final var subCommand = handleAdding(name, consoleSubExecutors);

        if (subCommand != null) {
            consoleSubExecutors.put(subCommand, execute);
        }
        return this;
    }

    public SubCommandBase<T, Y> handleSubPlayerTab(String name, CompleteTab.PlayerSubCommandComplete<T> complete) {
        final var subCommand = handleAdding(name, playerSubCompletes);

        if (subCommand != null) {
            playerSubCompletes.put(subCommand, complete);
        }
        return this;
    }

    public SubCommandBase<T, Y> handleSubConsoleTab(String name, CompleteTab.ConsoleSubCommandComplete<Y> complete) {
        final var subCommand = handleAdding(name, consoleSubCompletes);

        if (subCommand != null) {
            consoleSubCompletes.put(subCommand, complete);
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

        final var subCommand = getSubCommand(name);
        if (map.containsKey(subCommand)) {
            Debug.warn("You can't use another handler, one is already defined!", true);
            Debug.warn("SubCommand: " + name, true);
            return null;
        }

        return subCommand;
    }
}
