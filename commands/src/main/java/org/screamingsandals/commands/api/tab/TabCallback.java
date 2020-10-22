package org.screamingsandals.commands.api.tab;

import org.screamingsandals.commands.api.command.CommandContext;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 */
@FunctionalInterface
public interface TabCallback {

    void handle(CommandContext context);
}
