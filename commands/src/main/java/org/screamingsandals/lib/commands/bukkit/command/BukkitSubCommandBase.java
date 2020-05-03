package org.screamingsandals.lib.commands.bukkit.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.commands.common.commands.SubCommand;
import org.screamingsandals.lib.commands.common.commands.SubCommandBase;
import org.screamingsandals.lib.commands.common.environment.CommandEnvironment;
import org.screamingsandals.lib.commands.common.interfaces.CompleteTab;
import org.screamingsandals.lib.commands.common.interfaces.Execute;
import org.screamingsandals.lib.commands.common.manager.CommandManager;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class BukkitSubCommandBase extends SubCommandBase<Player, ConsoleCommandSender> {
    private final CommandManager commandManager = CommandEnvironment.getInstance().getCommandManager();
    private SubCommand subCommand;
    private BukkitCommandWrapper bukkitCommandWrapper;

    public static BukkitSubCommandBase createSubCommand(String commandName, String name, String permission, List<String> aliases) {
        final BukkitSubCommandBase bukkitSubCommandBase = new BukkitSubCommandBase();

        bukkitSubCommandBase.setSubCommand(new SubCommand(name, permission, aliases));
        bukkitSubCommandBase.setBukkitCommandWrapper((BukkitCommandWrapper) bukkitSubCommandBase.getCommandManager().getRegisteredCommand(commandName));
        return bukkitSubCommandBase;
    }

    public BukkitSubCommandBase handleSubPlayerCommand(String name, Execute.PlayerSubCommand<Player> execute) {
        super.handleSubPlayerCommand(name, execute);
        return this;
    }

    public BukkitSubCommandBase handleSubConsoleCommand(String name, Execute.ConsoleSubCommand<ConsoleCommandSender> execute) {
        super.handleSubConsoleCommand(name, execute);
        return this;
    }

    public BukkitSubCommandBase handleSubPlayerTab(String name, CompleteTab.PlayerSubCommandComplete<Player> complete) {
        super.handleSubPlayerTab(name, complete);
        return this;
    }

    public BukkitSubCommandBase handleSubConsoleTab(String name, CompleteTab.ConsoleSubCommandComplete<ConsoleCommandSender> complete) {
        super.handleSubConsoleTab(name, complete);
        return this;
    }

    @Override
    public void register() {
        bukkitCommandWrapper.getCommandBase().getSubCommands().add(subCommand);

        bukkitCommandWrapper.getPlayerSubCompletes().putAll(getPlayerSubCompletes());
        bukkitCommandWrapper.getConsoleSubCompletes().putAll(getConsoleSubCompletes());
    }
}
