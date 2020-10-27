package org.screamingsandals.commands.velocity.registry;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.RawCommand;
import org.screamingsandals.commands.api.registry.ServerRegistry;
import org.screamingsandals.lib.core.util.result.Result;

import java.util.Map;

public class VelocityServerRegistry implements ServerRegistry<RawCommand> {
    private final CommandManager manager;

    @Inject
    public VelocityServerRegistry(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public Result register(RawCommand command) {
        return null;
    }

    @Override
    public boolean isRegistered(String name) {
        return false;
    }

    @Override
    public void remove(String name) {

    }

    @Override
    public Map<String, RawCommand> getRegisteredCommands() {
        return null;
    }
}
