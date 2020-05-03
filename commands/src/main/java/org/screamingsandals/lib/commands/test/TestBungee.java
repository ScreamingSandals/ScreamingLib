package org.screamingsandals.lib.commands.test;

import net.md_5.bungee.api.plugin.Plugin;
import org.screamingsandals.lib.commands.Commands;
import org.screamingsandals.lib.commands.common.CommandBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class TestBungee extends Plugin {

    @Override
    public void onEnable() {
        Commands commands = new Commands(this);
        commands.load();

        CommandBuilder.bungeeCommand().create("shitstorm", "my.awesome.plugin", Arrays.asList("dickfest", "shitfest"))
                .setDescription("This is a test shitstorm!")
                .setUsage("/shitstorm")
                .addSubCommand("biatch", "my.awesome.plugin.biatch")
                .addSubCommand("kill", "my.awesome.plugin.kill")
                .addSubCommand("idk", "my.awesome.plugin.idk")
                .addSubCommand("oi", "my.awesome.plugin.idk")
                .handlePlayerCommand((player, args) -> player.sendMessage("oi"))
                .handleSubConsoleCommand("biatch", (player, args) -> {
                    player.sendMessage("WHAT THE HELLL");
                    player.sendMessage(args.toString());
                })
                .handleSubPlayerCommand("kill", (player, args) -> player.sendMessage("JSEŠ MRTVEJ ČUPKO"))
                .handlePlayerTab((player, args) -> new LinkedList<>())
                .handleConsoleCommand((console, args) -> console.sendMessage("YOU CANT DO THIS."))
                .handleSubPlayerCommand("oi", ((player, args) -> player.sendMessage("asda")))
                .handleSubPlayerTab("oi", ((player, args) -> Collections.emptyList()))
                .register();
    }

    @Override
    public void onDisable() {

    }
}
