package org.screamingsandals.lib.core.util;

import org.screamingsandals.lib.core.wrapper.plugin.PluginType;

public class PluginTypeGetter {
    private static PluginType TYPE = null;

    public static PluginType getType() {
        if (TYPE == null) {
            try {
                Class.forName("org.bukkit.command.defaults.BukkitCommand");
                TYPE = PluginType.BUKKIT;
                return TYPE;
            } catch (Throwable ignored) {
            }

            try {
                Class.forName("net.md_5.bungee.protocol.AbstractPacketHandler");
                TYPE = PluginType.BUNGEE;
                return TYPE;
            } catch (Throwable ignored) {
            }

            try {
                Class.forName("com.velocitypowered.api.plugin.Plugin");
                TYPE = PluginType.VELOCITY;
                return TYPE;
            } catch (Throwable ignored) {
            }

            return PluginType.UNKNOWN;
        }

        return TYPE;
    }
}
