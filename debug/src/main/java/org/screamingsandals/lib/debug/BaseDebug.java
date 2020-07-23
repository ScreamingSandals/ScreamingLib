package org.screamingsandals.lib.debug;

import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseDebug {
    protected Logger logger;

    public BaseDebug(Plugin plugin) {
        try {
            logger = plugin.getSLF4JLogger();
        } catch (Throwable t) {
            logger = LoggerFactory.getLogger(plugin.getName());
            logger.warn("You are not using Waterfall, using our own logger.");
        }
    }

    public BaseDebug(net.md_5.bungee.api.plugin.Plugin plugin) {
        try {
            logger = plugin.getSLF4JLogger();
        } catch (Throwable t) {
            logger = LoggerFactory.getLogger(plugin.getDescription().getName());
            logger.warn("You are not using Waterfall, using our own logger.");
        }
    }

    public BaseDebug(String name) {
        logger = LoggerFactory.getLogger(name);
    }

    public static Logger get(String name) {
        return LoggerFactory.getLogger(name);
    }
}
