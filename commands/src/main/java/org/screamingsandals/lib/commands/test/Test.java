package org.screamingsandals.lib.commands.test;

import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.lib.commands.BukkitCommands;

public class Test extends JavaPlugin {

    @Override
    public void onEnable() {
        BukkitCommands bukkitCommands = new BukkitCommands(this);
        bukkitCommands.load();

        bukkitCommands.doSomeething();
    }
}
