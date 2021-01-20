package org.screamingsandals.lib.plugin;

public enum PlatformType {
    // Java
    BUKKIT,
    MINESTOM,
    SPONGE,
    FABRIC,
    FORGE,
    BUNGEE,
    VELOCITY,
    // Bedrock
    NUKKIT;

    public static PlatformType get() {
        return PluginManager.getPlatformType();
    }
}
