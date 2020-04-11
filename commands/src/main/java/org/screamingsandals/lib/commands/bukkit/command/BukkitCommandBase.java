package org.screamingsandals.lib.commands.bukkit.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.commands.common.environment.CommandEnvironment;
import org.screamingsandals.lib.commands.common.commands.CommandBase;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class BukkitCommandBase extends CommandBase<Player, ConsoleCommandSender> {

    public BukkitCommandBase create(String name, String permission, List<String> aliases) {
        BukkitCommandBase bukkitCommandBase = new BukkitCommandBase();
        bukkitCommandBase.setName(name);
        bukkitCommandBase.setPermission(permission);
        bukkitCommandBase.setAliases(aliases);

        return bukkitCommandBase;
    }

    public BukkitCommandBase create(String name, String permissions) {
        return create(name, permissions, new ArrayList<>());
    }


    public BukkitCommandBase create(String name) {
        return create(name, "", new ArrayList<>());
    }

    @Override
    public void register() {
        final BukkitCommandWrapper bukkitCommandWrapper = new BukkitCommandWrapper(this);
        CommandEnvironment.getInstance().getCommandManager().registerCommand(bukkitCommandWrapper);
    }
}
