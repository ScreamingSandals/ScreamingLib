package org.screamingsandals.commands.api.registry;

import org.screamingsandals.lib.core.util.result.Result;

import java.util.Map;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 */
public interface ServerCommandRegistry<T> {

    Result register(T command);

    boolean isRegistered(String name);

    void remove(String name);

    Map<String, T> getRegisteredCommands();
}
