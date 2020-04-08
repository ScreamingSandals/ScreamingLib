package org.screamingsandals.lib.commands.common;

import lombok.Data;
import org.screamingsandals.lib.commands.api.interfaces.CompleteTab;
import org.screamingsandals.lib.commands.api.interfaces.Execute;

import java.util.List;


@Data
public abstract class CommandBuilder<T, Y> {
    private String name;
    private String subName;
    private String permission;
    private List<String> aliases;
    private String description;
    private String usage;

    private Execute.Player<T> executeByPlayer;
    private Execute.Console<Y> executeByConsole;
    private CompleteTab.Player<T> completeByPlayer;
    private CompleteTab.Console<Y> completeByConsole;

    public CommandBuilder<T, Y> setSubCommand(String name) {
        this.subName = name;
        return this;
    }

    public CommandBuilder<T, Y> setPermissions(String permission) {
        this.permission = permission;
        return this;
    }

    public CommandBuilder<T, Y> setAliases(List<String> aliases) {
        this.aliases = aliases;
        return this;
    }

    public CommandBuilder<T, Y> setDescription(String description) {
        this.description = description;
        return this;
    }

    public CommandBuilder<T, Y> setUsage(String usage) {
        this.usage = usage;
        return this;
    }

    public CommandBuilder<T, Y> executeByPlayer(Execute.Player<T> execute) {
        this.executeByPlayer = execute;
        return this;
    }

    public CommandBuilder<T, Y> executeByConsole(Execute.Console<Y> execute) {
        this.executeByConsole = execute;
        return this;
    }

    public CommandBuilder<T, Y> completeTabByPlayer(CompleteTab.Player<T> complete) {
        this.completeByPlayer = complete;
        return this;
    }

    public CommandBuilder<T, Y> completeTabByConsole(CompleteTab.Console<Y> complete) {
        this.completeByConsole = complete;
        return this;
    }

    public void register() {

    }

}
