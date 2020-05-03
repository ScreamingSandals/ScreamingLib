package org.screamingsandals.lib.commands.test;

import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.lib.commands.Commands;

public class Test extends JavaPlugin {
    private Commands commands;
    @Override
    public void onEnable() {
        commands = new Commands(this);
        commands.load();
    }

    @Override
    public void onDisable() {
        commands.destroy();
    }
}
