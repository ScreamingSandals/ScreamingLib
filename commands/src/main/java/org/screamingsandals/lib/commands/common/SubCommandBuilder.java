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
        private CommandManager commandManager;
        private String name;
        private SubCommand subCommand;
        private BukkitCommandWrapper bukkitCommandWrapper;
        private BukkitCommandBase bukkitCommandBase;

        public BukkitSubCommand createSubCommand(String commandName, String name, String permission, List<String> aliases) {
            this.commandManager = CommandEnvironment.getInstance().getCommandManager();

            System.out.println("Is command registered? " + commandManager.isCommandRegistered(commandName));
            if (!commandManager.isCommandRegistered(commandName)) {
                Debug.warn("Main command is not registered! Command name: " + commandName, true);
                return this;
            }
            System.out.println(commandManager != null);
            System.out.println("Executing!");
            this.name = name;
            this.subCommand = new SubCommand(name, permission, aliases);
            this.bukkitCommandWrapper = (BukkitCommandWrapper) commandManager.getRegisteredCommand(commandName);
            System.out.println("Executing2");
            this.bukkitCommandBase = this.bukkitCommandWrapper.getCommandBase();
            System.out.println("Executing3");

            if (bukkitCommandBase.isSubCommandRegistered(name)) {
                Debug.warn("SubCommand " + name + " is already registered!", true);
                return this;
            }

            bukkitCommandBase.addSubCommand(subCommand);
            commandManager.registerSubCommand(bukkitCommandWrapper, subCommand);

            Debug.info(bukkitCommandBase.toString(), true);
            System.out.println(bukkitCommandWrapper.toString());

            return this;
        }

        public BukkitSubCommand handleSubPlayerCommand(Execute.PlayerSubCommand<Player> execute) {
            bukkitCommandWrapper.getPlayerSubExecutors().put(subCommand, execute);
            bukkitCommandBase.getPlayerSubExecutors().put(subCommand, execute);
            return this;
        }

        public BukkitSubCommand handleSubConsoleCommand(Execute.ConsoleSubCommand<ConsoleCommandSender> execute) {
            bukkitCommandWrapper.getConsoleSubExecutors().put(subCommand, execute);
            bukkitCommandBase.getConsoleSubExecutors().put(subCommand, execute);
            return this;
        }

        public BukkitSubCommand handleSubPlayerTab(CompleteTab.PlayerSubCommandComplete<Player> complete) {
            bukkitCommandWrapper.getPlayerSubCompletes().put(subCommand, complete);
            bukkitCommandBase.getPlayerSubCompletes().put(subCommand, complete);
            return this;
        }

        public BukkitSubCommand handleSubConsoleTab(CompleteTab.ConsoleSubCommandComplete<ConsoleCommandSender> complete) {
            bukkitCommandWrapper.getConsoleSubCompletes().put(subCommand, complete);
            bukkitCommandBase.getConsoleSubCompletes().put(subCommand, complete);
            return this;
        }

        public void register() {
            //bukkitCommandWrapper.reload();
        }
    }

    @Data
    public static class BungeeSubCommand {
        private CommandManager commandManager;
        private String name;
        private BungeeCommandWrapper bungeeCommandWrapper;
        private BungeeCommandBase bungeeCommandBase;

        public BungeeSubCommand createSubCommand(String commandName, String name, String permission, List<String> aliases) {
            this.commandManager = CommandEnvironment.getInstance().getCommandManager();
            System.out.println("Executing!");
            this.name = name;
            this.bungeeCommandWrapper = (BungeeCommandWrapper) commandManager.getRegisteredCommand(commandName);
            this.bungeeCommandBase = this.bungeeCommandWrapper.getCommandBase();
            bungeeCommandBase.addSubCommand(name, permission, aliases);

            Debug.info(bungeeCommandBase.toString(), true);
            System.out.println(bungeeCommandWrapper.toString());

            return this;
        }

        public BungeeSubCommand handleSubPlayerCommand(Execute.PlayerSubCommand<ProxiedPlayer> execute) {
            bungeeCommandBase.handleSubPlayerCommand(getName(), execute);
            return this;
        }

        public BungeeSubCommand handleSubConsoleCommand(Execute.ConsoleSubCommand<CommandSender> execute) {
            bungeeCommandBase.handleSubConsoleCommand(getName(), execute);
            return this;
        }

        public BungeeSubCommand handleSubPlayerTab(CompleteTab.PlayerSubCommandComplete<ProxiedPlayer> complete) {
            bungeeCommandBase.handleSubPlayerTab(getName(), complete);
            return this;
        }

        public BungeeSubCommand handleSubConsoleTab(CompleteTab.ConsoleSubCommandComplete<CommandSender> complete) {
            bungeeCommandBase.handleSubConsoleTab(getName(), complete);
            return this;
        }

        public void register() {
            bungeeCommandWrapper.reload();
        }
    }
}
