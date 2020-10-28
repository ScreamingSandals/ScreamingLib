package org.screamingsandals.commands.api.registry;

import org.screamingsandals.lib.core.util.result.Result;

import java.util.Map;

public interface ServerRegistry<R, C> {

    Result register(R command);

    boolean isRegistered(String name);

    void remove(String name);

    Map<String, C> getRegisteredCommands();
}
