package org.screamingsandals.lib.commands.bungee;

import com.google.common.collect.ImmutableSet;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.List;

public abstract class BungeeCommandWrapper extends Command implements TabExecutor {

    public BungeeCommandWrapper(String name, String permission, List<String> aliases) {
        super(name, permission, aliases.toArray(new String[0]));
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {

    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return ImmutableSet.of();
    }
}
