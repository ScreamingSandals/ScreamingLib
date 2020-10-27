package org.screamingsandals.commands.core.auto;

import com.google.inject.Inject;
import org.screamingsandals.commands.api.auto.ScreamingCommand;
import org.screamingsandals.lib.core.reflect.SReflect;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.jar.JarFile;

public class CommandClassScanner {
    private final Logger log = LoggerFactory.getLogger(CommandClassScanner.class);

    @Inject
    public CommandClassScanner(PluginWrapper pluginWrapper) {
        scan(pluginWrapper.getPlugin().getClass());
    }

    public void scan(Class<?> toScan) {
        try {
            final var jarFile = new JarFile(
                    new File(toScan.getProtectionDomain().getCodeSource().getLocation().toURI()));

            final var packageName = toScan.getPackage().getName().replaceAll("\\.", "/");
            final var entries = Collections.list(jarFile.entries());

            entries.forEach(entry -> {
                try {
                    if (!entry.getName().endsWith(".class")
                            || !entry.getName().contains(packageName)) {
                        return;
                    }

                    final var clazz = Class.forName(entry.getName()
                            .replace("/", ".")
                            .replace(".class", ""));

                    if (!ScreamingCommand.class.isAssignableFrom(clazz)
                            || clazz.isInterface()) {
                        return;
                    }

                    final var command = clazz.getConstructor().newInstance();

                    SReflect.fastInvoke(command, "register");
                } catch (Throwable ignored) {
                }
            });
        } catch (Throwable e) {
            log.trace("Caught exception during scanning for commands! {}", e.getMessage(), e);
        }
    }
}
