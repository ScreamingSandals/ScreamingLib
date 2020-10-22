package org.screamingsandals.commands.core.registry;

import java.util.HashMap;
import java.util.Map;

import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.registry.CommandRegistry;
import org.screamingsandals.commands.api.registry.ServerCommandRegistry;
import org.screamingsandals.commands.core.command.AbstractCommandWrapper;
import org.screamingsandals.lib.core.util.result.Result;

import com.google.inject.Inject;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 */
public class AbstractCommandRegistry<T> implements CommandRegistry<T> {
    private final Map<String, CommandNode> registeredNodes = new HashMap<>();
    private final AbstractCommandWrapper<T> wrapper;
    private final ServerCommandRegistry<T> serverRegistry;

    @Inject
    public AbstractCommandRegistry(AbstractCommandWrapper<T> wrapper, ServerCommandRegistry<T> serverRegistry)  {
        this.wrapper = wrapper;
        this.serverRegistry = serverRegistry;
    }

    @Override
    public Result register(CommandNode node) {
        if (registeredNodes.containsKey(node.getName())) {
            return Result.fail("Node already registered!");
        }

        final var registerResult = serverRegistry.register(wrapper.internalWrap(node).get());

        if (registerResult.isOk()) {
            registeredNodes.put(node.getName(), node);
            return Result.ok();
        }

        return registerResult;
    }

    @Override
    public CommandNode getByName(String name) {
        return null;
    }

    @Override
    public CommandNode getBySubNode(CommandNode node) {
        return null;
    }

    @Override
    public Result remove(CommandNode node) {
        return null;
    }
}
