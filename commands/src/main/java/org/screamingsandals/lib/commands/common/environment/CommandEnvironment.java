package org.screamingsandals.lib.commands.common.environment;

import lombok.Data;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.commands.bukkit.BukkitManager;
import org.screamingsandals.lib.commands.bungee.BungeeManager;
import org.screamingsandals.lib.commands.common.RegisterCommand;
import org.screamingsandals.lib.commands.common.interfaces.ScreamingCommand;
import org.screamingsandals.lib.commands.common.language.CommandsLanguage;
import org.screamingsandals.lib.commands.common.language.DefaultLanguage;
import org.screamingsandals.lib.commands.common.manager.CommandManager;
import org.screamingsandals.lib.debug.Debug;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Data
public abstract class CommandEnvironment {
    private final Object plugin;
    private static CommandEnvironment instance;
    private CommandsLanguage commandLanguage;
    private CommandManager commandManager;

    public CommandEnvironment(Object plugin) {
        this.plugin = plugin;
        instance = this;
    }

    public void load() {
        try {
            Class.forName("org.bukkit.Server");
            commandManager = new BukkitManager((Plugin) plugin);
        } catch (Throwable ignored) {
            try {
                commandManager = new BungeeManager((net.md_5.bungee.api.plugin.Plugin) plugin);
                Class.forName("net.md_5.bungee.api.plugin.PluginManager");
            } catch (Throwable ignored2) {
                Debug.warn("Your server type is not supported!", true);
            }
        }

        commandLanguage = new DefaultLanguage();

        try {
            loadScreamingCommands(plugin.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        commandManager.destroy();
    }

    public void loadScreamingCommands(Class<?> toLoad) throws URISyntaxException, IOException {
        final JarFile jarFile = new JarFile(new File(toLoad.getProtectionDomain().getCodeSource().getLocation().toURI()));
        final String packageName = plugin.getClass().getPackage().getName().replaceAll("\\.", "/");
        final List<JarEntry> entries = Collections.list(jarFile.entries());
        final List<Object> subCommands = new LinkedList<>();

        entries.forEach(entry -> {
            try {
                if (!entry.getName().endsWith(".class") || !entry.getName().contains(packageName)) {
                    return;
                }

                final Class<?> clazz = Class.forName(entry.getName()
                        .replace("/", ".")
                        .replace(".class", ""));

                if (!ScreamingCommand.class.isAssignableFrom(clazz) || clazz.getDeclaredAnnotation(RegisterCommand.class) == null) {
                    return;
                }

                final Constructor<?> constructor = clazz.getConstructor();
                final Object object = constructor.newInstance();

                if (clazz.getDeclaredAnnotation(RegisterCommand.class).subCommand()) {
                    subCommands.add(object);
                    return;
                }

                invoke(object);
            } catch (Exception ignored) {
            }
        });

        subCommands.forEach(this::invoke);
    }

    public static CommandEnvironment getInstance() {
        return instance;
    }

    private void invoke(Object object) {
        try {
            object.getClass().getDeclaredMethod("register").invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
