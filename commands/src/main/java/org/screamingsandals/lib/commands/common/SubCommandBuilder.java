package org.screamingsandals.lib.commands.common;

import lombok.Data;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.commands.bukkit.command.BukkitCommandBase;
import org.screamingsandals.lib.commands.bukkit.command.BukkitCommandWrapper;
import org.screamingsandals.lib.commands.bungee.command.BungeeCommandBase;
import org.screamingsandals.lib.commands.bungee.command.BungeeCommandWrapper;
import org.screamingsandals.lib.commands.common.commands.SubCommand;
import org.screamingsandals.lib.commands.common.environment.CommandEnvironment;
import org.screamingsandals.lib.commands.common.interfaces.CompleteTab;
import org.screamingsandals.lib.commands.common.interfaces.Execute;
import org.screamingsandals.lib.commands.common.manager.CommandManager;
import org.screamingsandals.lib.debug.Debug;

import java.util.List;

public class SubCommandBuilder {

    public static BukkitSubCommand bukkitSubCommand() {
        return new BukkitSubCommand();
    }

    public static BungeeSubCommand bungeeSubCommand() {
        return new BungeeSubCommand();
    }

    @Data
    public static class BukkitSubCommand {
        private final CommandManager commandManager = CommandEnvironment.getInstance().getCommandManager();
        private String name;
        private SubCommand subCommand;
        private BukkitCommandWrapper commandWrapper;
        private BukkitCommandBase commandBase;

        public BukkitSubCommand createSubCommand(String commandName, String name, String permission, List<String> aliases) {
            if (!commandManager.isCommandRegistered(commandName)) {
                Debug.warn("Main command is not registered! Command name: " + commandName, true);
                return this;
            }

            this.name = name;
            this.subCommand = new SubCommand(name, permission, aliases);
            this.commandWrapper = (BukkitCommandWrapper) commandManager.getRegisteredCommand(commandName);

            if (commandWrapper == null) {
                Debug.warn("Command named " + commandName + " does not exists, can't register sub command!");
                return this;
            }

            this.commandBase = commandWrapper.getCommandBase();

            if (commandBase.isSubCommandRegistered(name)) {
                Debug.warn("SubCommand " + name + " is already registered!", true);
                return this;
            }

            commandBase.addSubCommand(subCommand);
            return this;
        }

        public BukkitSubCommand handleSubPlayerCommand(Execute.PlayerSubCommand<Player> execute) {
            commandBase.getPlayerSubExecutors().put(subCommand, execute);
            return this;
        }

        public BukkitSubCommand handleSubConsoleCommand(Execute.ConsoleSubCommand<ConsoleCommandSender> execute) {
            commandBase.getConsoleSubExecutors().put(subCommand, execute);
            return this;
        }

        public BukkitSubCommand handleSubPlayerTab(CompleteTab.PlayerSubCommandComplete<Player> complete) {
            commandBase.getPlayerSubCompletes().put(subCommand, complete);
            return this;
        }

        public BukkitSubCommand handleSubConsoleTab(CompleteTab.ConsoleSubCommandComplete<ConsoleCommandSender> complete) {
            commandBase.getConsoleSubCompletes().put(subCommand, complete);
            return this;
        }
    }

    @Data
    public static class BungeeSubCommand {
        private final CommandManager commandManager = CommandEnvironment.getInstance().getCommandManager();
        private String name;
        private SubCommand subCommand;
        private BungeeCommandWrapper commandWrapper;
        private BungeeCommandBase commandBase;

        public BungeeSubCommand createSubCommand(String commandName, String name, String permission, List<String> aliases) {
            if (!commandManager.isCommandRegistered(commandName)) {
                Debug.warn("Main command is not registered! Command name: " + commandName, true);
                return this;
            }

            this.name = name;
            this.subCommand = new SubCommand(name, permission, aliases);
            this.commandWrapper = (BungeeCommandWrapper) commandManager.getRegisteredCommand(commandName);

            if (commandWrapper == null) {
                Debug.warn("Command named " + commandName + " does not exists, can't register sub command!");
                return this;
            }

            this.commandBase = commandWrapper.getCommandBase();

            if (commandBase.isSubCommandRegistered(name)) {
                Debug.warn("SubCommand " + name + " is already registered!", true);
                return this;
            }

            commandBase.addSubCommand(subCommand);
            return this;
        }

        public BungeeSubCommand handleSubPlayerCommand(Execute.PlayerSubCommand<ProxiedPlayer> execute) {
            commandBase.handleSubPlayerCommand(getName(), execute);
            return this;
        }

        public BungeeSubCommand handleSubConsoleCommand(Execute.ConsoleSubCommand<CommandSender> execute) {
            commandBase.handleSubConsoleCommand(getName(), execute);
            return this;
        }

        public BungeeSubCommand handleSubPlayerTab(CompleteTab.PlayerSubCommandComplete<ProxiedPlayer> complete) {
            commandBase.handleSubPlayerTab(getName(), complete);
            return this;
        }

        public BungeeSubCommand handleSubConsoleTab(CompleteTab.ConsoleSubCommandComplete<CommandSender> complete) {
            commandBase.handleSubConsoleTab(getName(), complete);
            return this;
        }
    }
}
