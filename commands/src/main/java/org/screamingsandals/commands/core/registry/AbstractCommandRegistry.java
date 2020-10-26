package org.screamingsandals.commands.core.registry;

import org.screamingsandals.commands.api.command.CommandBase;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.registry.CommandRegistry;
import org.screamingsandals.commands.api.registry.ServerCommandRegistry;
import org.screamingsandals.commands.api.wrapper.WrappedCommand;
import org.screamingsandals.commands.core.command.AbstractCommandWrapper;
import org.screamingsandals.lib.core.util.result.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 */
public abstract class AbstractCommandRegistry<T> implements CommandRegistry<T> {
    private final Map<String, CommandBase> registeredNodes = new HashMap<>();
    private final AbstractCommandWrapper<T> wrapper;
    private final ServerCommandRegistry<T> serverRegistry;

    public AbstractCommandRegistry(AbstractCommandWrapper<T> wrapper, ServerCommandRegistry<T> serverRegistry)  {
        this.wrapper = wrapper;
        this.serverRegistry = serverRegistry;
    }

    @Override
    public Result register(CommandNode node) {
        if (registeredNodes.containsKey(node.getName())) {
            return Result.fail("Node already registered!");
        }

        final var registerResult = serverRegistry.register(wrapper.get(node).getCommand());

        if (registerResult.isOk()) {
            registeredNodes.put(node.getName(), node);
            return Result.ok();
        }

        return registerResult;
    }

    @Override
    public CommandBase getByName(String name) {
        return registeredNodes.getOrDefault(name, null);
    }

    @Override
    public Result remove(CommandNode node) {
        if (!registeredNodes.containsKey(node.getName())) {
            return Result.fail("Node is not registered");
        }

        registeredNodes.remove(node.getName());
        return Result.ok();
    }

    @Override
    public WrappedCommand<T> getWrappedCommand(CommandNode node) {
        return wrapper.get(node);
    }
}
