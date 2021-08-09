package org.screamingsandals.lib.utils;

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

    public boolean isProxy() {
        return this == BUNGEE || this == VELOCITY;
    }

    public boolean isServer() {
        return !isProxy();
    }
}
