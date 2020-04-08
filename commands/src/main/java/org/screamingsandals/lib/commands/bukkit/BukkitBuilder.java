package org.screamingsandals.lib.commands.bukkit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.commands.api.core.CommandsBase;
import org.screamingsandals.lib.commands.common.CommandBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class BukkitBuilder extends CommandBuilder<Player, ConsoleCommandSender> {

    public static BukkitBuilder create(String name, String subCommandName, String permission, List<String> aliases) {
        BukkitBuilder bukkitBuilder = new BukkitBuilder();
        bukkitBuilder.setName(name);
        bukkitBuilder.setSubCommand(subCommandName);
        bukkitBuilder.setPermission(permission);
        bukkitBuilder.setAliases(aliases);

        return bukkitBuilder;
    }

    public static BukkitBuilder create(String name, String subCommandName, String permission) {
        return create(name, subCommandName, permission, new ArrayList<>());
    }

    public static BukkitBuilder create(String name, String subCommandName) {
        return create(name, subCommandName, "", new ArrayList<>());
    }

    public static BukkitBuilder create(String name) {
        return create(name, "", "", new ArrayList<>());
    }

    @Override
    public void register() {
        final BukkitCommandFrame bukkitCommandFrame = new BukkitCommandFrame(this);
        CommandsBase.getInstance().getCommandManager().registerCommand(bukkitCommandFrame);
    }
}
