package org.screamingsandals.commands.api.command;

@FunctionalInterface
public interface CommandCallback {

    void handle(CommandContext context);

    //KEEP OR NO
    enum Priority {
        HIGHEST,
        HIGH,
        NORMAL,
        LOW,
        LOWEST;
    }
}
