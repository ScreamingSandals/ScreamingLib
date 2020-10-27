package org.screamingsandals.commands.api.command;

@FunctionalInterface
public interface CommandCallback {

    /**
     * Callback for handling command context.
     * @param context context of the command
     */
    void handle(CommandContext context);
}
