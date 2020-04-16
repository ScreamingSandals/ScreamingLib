package org.screamingsandals.lib.commands.test;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.lib.commands.Commands;
import org.screamingsandals.lib.commands.common.CommandBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Test extends JavaPlugin {

    @Override
    public void onEnable() {
        Commands commands = new Commands(this);
        commands.load();

        registerCommands();
    }

    public void registerCommands() {
        CommandBuilder.bukkitCommand().create("shitstorm", "my.awesome.plugin", Arrays.asList("dickfest", "shitfest"))
                .setDescription("This is a test shitstorm!")
                .setUsage("/shitstorm")
                .registerSubCommand("biatch", "my.awesome.plugin.biatch")
                .registerSubCommand("kill", "my.awesome.plugin.kill")
                .registerSubCommand("idk", "my.awesome.plugin.idk")
                .registerSubCommand("oi", "my.awesome.plugin.idk")
                .handlePlayerCommand((player, args) -> player.sendMessage("oi"))
                .handleSubPlayerCommand("biatch", (player, args) -> {
                    player.sendMessage("WHAT THE HELLL");
                    player.sendMessage(args.toString());
                })
                .handleSubPlayerCommand("kill", (player, args) -> player.setHealth(0))
                .handlePlayerTab((player, args) -> new LinkedList<>())
                .handleConsoleCommand((console, args) -> console.sendMessage("YOU CANT DO THIS."))
                .handleSubPlayerCommand("oi", ((player, args) -> player.sendMessage("asda")))
                .handleSubPlayerTab("oi", ((player, args) -> Collections.emptyList()))
                .register();

    }

    public void execute(Player player, List<String> args) {

    }
}