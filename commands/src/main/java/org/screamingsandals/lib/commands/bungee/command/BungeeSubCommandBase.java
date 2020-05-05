package org.screamingsandals.lib.commands.bungee.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.screamingsandals.lib.commands.common.commands.SubCommand;
import org.screamingsandals.lib.commands.common.commands.SubCommandBase;
import org.screamingsandals.lib.commands.common.environment.CommandEnvironment;
import org.screamingsandals.lib.commands.common.interfaces.CompleteTab;
import org.screamingsandals.lib.commands.common.interfaces.Execute;
import org.screamingsandals.lib.commands.common.manager.CommandManager;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class BungeeSubCommandBase extends SubCommandBase<ProxiedPlayer, CommandSender> {
    private final CommandManager commandManager = CommandEnvironment.getInstance().getCommandManager();
    private SubCommand subCommand;
    private BungeeCommandWrapper bungeeCommandWrapper;

    public BungeeSubCommandBase createSubCommand(String commandName, String name, String permission, List<String> aliases) {
        subCommand = new SubCommand(name, permission, aliases);
        bungeeCommandWrapper = (BungeeCommandWrapper) commandManager.getRegisteredCommand(commandName);
        return this;
    }

    public BungeeSubCommandBase handleSubPlayerCommand(String name, Execute.PlayerSubCommand<ProxiedPlayer> execute) {
        super.handleSubPlayerCommand(name, execute);
        return this;
    }

    public BungeeSubCommandBase handleSubConsoleCommand(String name, Execute.ConsoleSubCommand<CommandSender> execute) {
        super.handleSubConsoleCommand(name, execute);
        return this;
    }

    public BungeeSubCommandBase handleSubPlayerTab(String name, CompleteTab.PlayerSubCommandComplete<ProxiedPlayer> complete) {
        super.handleSubPlayerTab(name, complete);
        return this;
    }

    public BungeeSubCommandBase handleSubConsoleTab(String name, CompleteTab.ConsoleSubCommandComplete<CommandSender> complete) {
        super.handleSubConsoleTab(name, complete);
        return this;
    }

    @Override
    public void register() {
        bungeeCommandWrapper.getCommandBase().getSubCommands().add(subCommand);

        bungeeCommandWrapper.getPlayerSubCompletes().putAll(getPlayerSubCompletes());
        bungeeCommandWrapper.getConsoleSubCompletes().putAll(getConsoleSubCompletes());
    }
}
