package org.screamingsandals.lib.debug;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;

public class SDebug {
    private static Logger logger;

    @Getter
    @Setter
    private static boolean isDebug = false;
    @Getter
    @Setter
    private static String fallbackName = "Fallback";

    public static void init(Plugin plugin) {
        try {
            logger = plugin.getSLF4JLogger();
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println("You probably used bad debug boi :)");
        }
    }

    public static void init(net.md_5.bungee.api.plugin.Plugin plugin) {
        logger = plugin.getSLF4JLogger();
    }

    public static Logger get() {
        return logger;
    }
}
