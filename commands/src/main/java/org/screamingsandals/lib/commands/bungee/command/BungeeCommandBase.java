package org.screamingsandals.lib.commands.bungee.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.screamingsandals.lib.commands.common.commands.CommandBase;
import org.screamingsandals.lib.commands.common.environment.CommandEnvironment;

import java.util.ArrayList;
import java.util.List;

public class BungeeCommandBase extends CommandBase<ProxiedPlayer, CommandSender> {

    public BungeeCommandBase create(String name, String permission, List<String> aliases) {
        BungeeCommandBase bukkitCommandBase = new BungeeCommandBase();
        bukkitCommandBase.setName(name);
        bukkitCommandBase.setPermission(permission);
        bukkitCommandBase.setAliases(aliases);

        return bukkitCommandBase;
    }

    public BungeeCommandBase create(String name, String permissions) {
        return create(name, permissions, new ArrayList<>());
    }


    public BungeeCommandBase create(String name) {
        return create(name, "", new ArrayList<>());
    }

    @Override
    public void register() {
        final BungeeCommandWrapper bungeeCommandWrapper = new BungeeCommandWrapper(this);
        CommandEnvironment.getInstance().getCommandManager().registerCommand(bungeeCommandWrapper);
    }
}
