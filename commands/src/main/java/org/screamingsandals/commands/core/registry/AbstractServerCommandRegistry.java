package org.screamingsandals.commands.core.registry;

import org.screamingsandals.commands.api.registry.ServerCommandRegistry;
import org.screamingsandals.lib.core.util.result.Result;

import java.util.Map;

public class AbstractServerCommandRegistry<T> implements ServerCommandRegistry<T> {
    @Override
    public Result register(T command) {
        return null;
    }

    @Override
    public boolean isRegistered(T command) {
        return false;
    }

    @Override
    public Result remove(T command) {
        return null;
    }

    @Override

    public Map<String, T> getRegisteredCommands() {
        return null;
    }
}
