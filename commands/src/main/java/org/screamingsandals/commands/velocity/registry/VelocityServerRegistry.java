package org.screamingsandals.commands.velocity.registry;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.RawCommand;
import org.screamingsandals.commands.api.registry.ServerRegistry;
import org.screamingsandals.commands.api.wrapper.WrappedCommand;
import org.screamingsandals.lib.core.util.result.Result;

import java.util.Map;

public class VelocityServerRegistry implements ServerRegistry<WrappedCommand<RawCommand>, RawCommand> {
    private final CommandManager manager;

    @Inject
    public VelocityServerRegistry(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public Result register(WrappedCommand<RawCommand> command) {
        if (isRegistered(command.getNode().getName())) {
            return Result.fail("Already registered!");
        }

        manager.register(command.getNode().getName(), command.getCommand());
        return Result.ok();
    }

    @Override
    public boolean isRegistered(String name) {
        return manager.hasCommand(name);
    }

    @Override
    public void remove(String name) {
        manager.unregister(name);
    }

    @Override
    public Map<String, RawCommand> getRegisteredCommands() {
        //todo
        return Map.of();
    }
}
