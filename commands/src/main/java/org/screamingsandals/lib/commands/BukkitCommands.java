package org.screamingsandals.lib.commands;

import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.commands.api.core.CommandsBase;
import org.screamingsandals.lib.commands.bukkit.BukkitBuilder;
import org.screamingsandals.lib.commands.bungee.BungeeBuilder;

import java.util.Arrays;
import java.util.LinkedList;

public class BukkitCommands extends CommandsBase {

    public BukkitCommands(Plugin plugin) {
        super(plugin);
    }


    public void doSomeething() {
        BukkitBuilder
                .create("shitstorm", "", "my.awesome.plugin", Arrays.asList("dickfest", "shitfest"))
                .setDescription("This is a test shitstorm!")
                .setUsage("/shitstorm")
                .executeByPlayer((player, args) -> {
                    if (args.size() > 1) {
                        player.sendMessage("WELL HELLO THERE, STARTING SHITSTORM!");
                    }
                })
                .completeTabByPlayer((player, args) -> new LinkedList<>())
                .executeByConsole((console, args) -> console.sendMessage("YOU CANT DO THIS."))
                .register();

        BungeeBuilder.create("name")
                .executeByPlayer((proxiedPlayer, args) -> {
                    if (args.size() > 1) {
                        proxiedPlayer.getChatMode();
                    }
                }).register();
    }
}
