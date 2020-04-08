package org.screamingsandals.lib.commands.bungee;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.screamingsandals.lib.commands.api.core.CommandsBase;
import org.screamingsandals.lib.commands.common.CommandBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class BungeeBuilder extends CommandBuilder<ProxiedPlayer, CommandSender> {

    public static BungeeBuilder create(String name, String subCommandName, String permission, List<String> aliases) {
        BungeeBuilder bungeeBuilder = new BungeeBuilder();
        bungeeBuilder.setName(name);
        bungeeBuilder.setSubCommand(subCommandName);
        bungeeBuilder.setPermission(permission);
        bungeeBuilder.setAliases(aliases);

        return bungeeBuilder;
    }

    public static BungeeBuilder create(String name, String subCommandName, String permission) {
        return create(name, subCommandName, permission, new ArrayList<>());
    }

    public static BungeeBuilder create(String name, String subCommandName) {
        return create(name, subCommandName, "", new ArrayList<>());
    }

    public static BungeeBuilder create(String name) {
        return create(name, "", "", new ArrayList<>());
    }

    @Override
    public void register() {
        final BungeeCommandFrame bungeeCommandFrame = new BungeeCommandFrame(this);
        CommandsBase.getInstance().getCommandManager().registerCommand(bungeeCommandFrame);
    }
}
