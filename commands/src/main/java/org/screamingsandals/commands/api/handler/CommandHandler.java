package org.screamingsandals.commands.api.handler;

import org.screamingsandals.commands.api.command.CommandContext;

public interface CommandHandler {

    void handle(CommandContext context);
}
