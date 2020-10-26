package org.screamingsandals.commands.api.handler;

import org.screamingsandals.commands.api.command.CommandContext;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 */
public interface CommandHandler {

    void handle(CommandContext context);
}
