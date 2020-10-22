package org.screamingsandals.commands.api.registry;

import java.util.Map;

import org.screamingsandals.lib.core.util.result.Result;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 */
public interface ServerCommandRegistry<T> {

    Result register(T command);

    boolean isRegistered(T command);

    Result remove(T command);

    Map<String, T> getRegisteredCommands();
}
